package com.andrei.database;

import com.andrei.model.Account;
import com.andrei.model.Transaction;
import com.andrei.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

import static com.andrei.database.Constants.*;

/**
 * Created by shubham on 03/12/16.
 */
public class DBConnection {

    private static final Logger logger = LogManager.getLogger("DBConnection");

    private static Connection dbConnection;
    public static java.sql.Statement statement;
    public ResultSet resultSet;

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + Constants.TABLE_USER + "("
            + Constants.USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, "
            + Constants.USER_COLUMN_NAME + " VARCHAR(50) NOT NULL, "
            + Constants.USER_COLUMN_EMAIL + " VARCHAR(50) NOT NULL UNIQUE, "
            + Constants.USER_COLUMN_PD + " VARCHAR(50) NOT NULL, "
            + Constants.USER_COLUMN_ADDRESS + " VARCHAR(150) NOT NULL"
            + ");";

    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS "
            + Constants.TABLE_ACCOUNT + "("
            + Constants.ACCOUNT_COLUMN_ACCOUNT_NUMBER + " INTEGER PRIMARY KEY AUTO_INCREMENT, "
            + Constants.ACCOUNT_COLUMN_BALANCE + " REAL NOT NULL DEFAULT 0, "
            + Constants.ACCOUNT_COLUMN_TYPE + " VARCHAR(50) NOT NULL DEFAULT '" + Constants.ACCOUNT_TYPE_SAVINGS + "', "
            + Constants.ACCOUNT_COLUMN_USER_ID + " INTEGER, "
            + "FOREIGN KEY (" + Constants.ACCOUNT_COLUMN_USER_ID + ") REFERENCES "
            + Constants.TABLE_USER + "(" + Constants.USER_COLUMN_ID + ")"
            + ");";

    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS "
            + Constants.TABLE_TRANSACTION + "("
            + Constants.TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, "
            + Constants.TRANSACTION_COLUMN_FROM_ACCOUNT + " INTEGER NOT NULL, "
            + Constants.TRANSACTION_COLUMN_TO_ACCOUNT + " INTEGER NOT NULL, "
            + Constants.TRANSACTION_COLUMN_AMOUNT + " REAL NOT NULL, "
            + Constants.TRANSACTION_COLUMN_TIME + " INTEGER NOT NULL"
            + ");";

    public static Connection setDbConnection() throws SQLException {

        try {
            Class.forName(Constants.DRIVER_NAME);
            dbConnection = DriverManager.getConnection(Constants.DATABASE_URL,
                    Constants.DATABASE_USERNAME, Constants.DATABASE_PD);
        } catch (ClassNotFoundException ex) {

            logger.error("Exception at setDbConnection : {}", ex);
        }

        return dbConnection;
    }

    public ResultSet getResultSet(String query, Connection connection) {

        logger.warn("Query : {}", query);

        dbConnection = connection;

        try {

            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {

            logger.error("Exception at getResultSet : {}", ex);
        }

        return resultSet;
    }

    public static void createTables() throws SQLException {

        dbConnection = setDbConnection();
        statement = dbConnection.createStatement();

        logger.warn(CREATE_TABLE_USER);
        logger.warn(CREATE_TABLE_ACCOUNT);
        logger.warn(CREATE_TABLE_TRANSACTION);

        statement.execute(CREATE_TABLE_USER);
        statement.execute(CREATE_TABLE_ACCOUNT);
        statement.execute(CREATE_TABLE_TRANSACTION);

        statement.close();
        dbConnection.close();
    }

    public User registerUser(String email, String name, String address, String password, Connection connection) {

        dbConnection = connection;
        PreparedStatement pst = null;
        User user = null;

        String insertUserQuery = "INSERT INTO " + Constants.TABLE_USER
                + "(" + Constants.USER_COLUMN_NAME
                + ", " + Constants.USER_COLUMN_EMAIL
                + ", " + Constants.USER_COLUMN_PD
                + ", " + Constants.USER_COLUMN_ADDRESS
                + ") VALUES(?, ?, ?, ?)";

        try {

            pst = dbConnection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setString(4, address);
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int lastInsertedUserId = rs.getInt(1);
                logger.warn("registerUser lastInsertedUserId : {}", lastInsertedUserId);
                user = getUserById(lastInsertedUserId, dbConnection);
            }

        } catch (SQLException ex) {

            logger.error("Exception at registerUser : {}", ex);

        } finally {
            try {

                if (pst != null) {
                    pst.close();
                }
                dbConnection.close();

            } catch (SQLException ex) {
                logger.error("Exception at closing dbConnection registerUser : {}", ex);
            }
        }

        return user;
    }

    public User getUserById(int id, Connection connection) {

        String query = "SELECT * FROM "
                + Constants.TABLE_USER + " WHERE "
                + Constants.USER_COLUMN_ID + "='" + id + "'";

        User user = null;

        resultSet = getResultSet(query, connection);

        try {

            if (resultSet.next()) {

                user = new User();
                user.setId(resultSet.getLong(USER_COLUMN_ID));
                user.setName(resultSet.getString(USER_COLUMN_NAME));
                user.setAddress(resultSet.getString(USER_COLUMN_ADDRESS));
                user.setEmail(resultSet.getString(USER_COLUMN_EMAIL));
            }
        } catch (SQLException ex) {

            logger.error("Exception at checkLogin during User retrieve : {}", ex);
        }

        return user;
    }

    public ArrayList<Account> getAllAccountsOfUser(int id) {

        return null;
    }

    public ArrayList<Transaction> getAllTransactionOfAccount(int id) {

        return null;
    }
}
