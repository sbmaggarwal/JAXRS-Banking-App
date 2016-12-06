package com.andrei.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
