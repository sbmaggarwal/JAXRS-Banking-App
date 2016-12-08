package com.andrei.database;

import com.andrei.model.Account;
import com.andrei.model.Transaction;
import com.andrei.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
            + TABLE_USER + "("
            + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, "
            + USER_COLUMN_NAME + " VARCHAR(50) NOT NULL, "
            + USER_COLUMN_EMAIL + " VARCHAR(50) NOT NULL UNIQUE, "
            + USER_COLUMN_PD + " VARCHAR(50) NOT NULL, "
            + USER_COLUMN_ADDRESS + " VARCHAR(150) NOT NULL"
            + ");";

    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ACCOUNT + "("
            + ACCOUNT_COLUMN_ACCOUNT_NUMBER + " INTEGER PRIMARY KEY AUTO_INCREMENT, "
            + ACCOUNT_COLUMN_BALANCE + " REAL NOT NULL DEFAULT 0, "
            + ACCOUNT_COLUMN_TYPE + " VARCHAR(50) NOT NULL DEFAULT '" + ACCOUNT_TYPE_SAVINGS + "', "
            + ACCOUNT_COLUMN_USER_ID + " INTEGER, "
            + "FOREIGN KEY (" + ACCOUNT_COLUMN_USER_ID + ") REFERENCES "
            + TABLE_USER + "(" + USER_COLUMN_ID + ")"
            + ");";

    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRANSACTION + "("
            + TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, "
            + TRANSACTION_COLUMN_FROM_ACCOUNT + " VARCHAR(50), "
            + TRANSACTION_COLUMN_TO_ACCOUNT + " VARCHAR(50), "
            + TRANSACTION_COLUMN_AMOUNT + " REAL NOT NULL, "
            + TRANSACTION_COLUMN_TIME + " DATETIME "
            + ");";

    public static Connection setDbConnection() throws SQLException {

        try {
            Class.forName(DRIVER_NAME);
            dbConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PD);
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

        String insertUserQuery = "INSERT INTO " + TABLE_USER
                + "(" + USER_COLUMN_NAME
                + ", " + USER_COLUMN_EMAIL
                + ", " + USER_COLUMN_PD
                + ", " + USER_COLUMN_ADDRESS
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

            closeEverything(pst, dbConnection);
        }

        return user;
    }

    public User getUserById(int id, Connection connection) {

        String query = "SELECT * FROM "
                + TABLE_USER + " WHERE "
                + USER_COLUMN_ID + "='" + id + "'";

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

    public ArrayList<Account> getAllAccountsOfUser(int id, Connection connection) {

        String query = "SELECT * FROM "
                + TABLE_ACCOUNT + " WHERE "
                + ACCOUNT_COLUMN_USER_ID + "='" + id + "'";

        ArrayList<Account> accountList = new ArrayList<Account>();

        resultSet = getResultSet(query, connection);

        try {

            while (resultSet.next()) {

                Account account = new Account();
                account.setAccountNumber(resultSet.getString(ACCOUNT_COLUMN_ACCOUNT_NUMBER));
                account.setAccountType(resultSet.getString(ACCOUNT_COLUMN_TYPE));
                account.setBalance(resultSet.getLong(ACCOUNT_COLUMN_BALANCE));
                account.setUserId(resultSet.getLong(ACCOUNT_COLUMN_USER_ID));

                accountList.add(account);
            }
        } catch (SQLException ex) {

            logger.error("Exception at checkLogin during User retrieve : {}", ex);
        }

        return accountList;
    }

    public void addNewAccount(String id, String type, Connection connection) {

        this.dbConnection = connection;
        PreparedStatement pst = null;
        Account account = null;

        String insertUserQuery = "INSERT INTO " + TABLE_ACCOUNT
                + "(" + ACCOUNT_COLUMN_USER_ID
                + ", " + ACCOUNT_COLUMN_TYPE
                + ") VALUES(?, ?)";
        try {

            pst = dbConnection.prepareStatement(insertUserQuery);

            pst.setString(1, id);
            pst.setString(2, type);
            pst.executeUpdate();
        } catch (SQLException ex) {

            logger.error("Exception at addNewAccount : {}", ex);

        } finally {

            closeEverything(pst, dbConnection);
        }
    }

    public List<Transaction> getAllUserTransactions(int user, Connection dbConnection) {

        /* Example query :
            SELECT * FROM transaction
            WHERE transaction.from_account IN (SELECT account.account_number FROM account WHERE account.user_id = 1) OR
            transaction.to_account IN (SELECT account.account_number FROM account WHERE account.user_id = 1)
         */
        String query = "SELECT * FROM " + TABLE_TRANSACTION +
                " WHERE " + TABLE_TRANSACTION + "." + TRANSACTION_COLUMN_FROM_ACCOUNT + " IN (SELECT " +

                TABLE_ACCOUNT + "." + ACCOUNT_COLUMN_ACCOUNT_NUMBER + " FROM " + TABLE_ACCOUNT +
                " WHERE " + TABLE_ACCOUNT + "." + ACCOUNT_COLUMN_USER_ID + "='" + user + "') OR " +
                TABLE_TRANSACTION + "." + TRANSACTION_COLUMN_TO_ACCOUNT + " IN (SELECT " +

                TABLE_ACCOUNT + "." + ACCOUNT_COLUMN_ACCOUNT_NUMBER + " FROM " + TABLE_ACCOUNT +
                " WHERE " + TABLE_ACCOUNT + "." + ACCOUNT_COLUMN_USER_ID + "='" + user + "')";

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        resultSet = getResultSet(query, dbConnection);

        try {

            while (resultSet.next()) {

                String date = resultSet.getString(TRANSACTION_COLUMN_TIME);
                date = date.substring(0, date.indexOf(' '));

                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getLong(TRANSACTION_COLUMN_ID));
                transaction.setFromAccountId(resultSet.getLong(TRANSACTION_COLUMN_FROM_ACCOUNT));
                transaction.setToAccountId(resultSet.getLong(TRANSACTION_COLUMN_TO_ACCOUNT));
                transaction.setTimeStamp(date);
                transaction.setAmount(resultSet.getLong(TRANSACTION_COLUMN_AMOUNT));

                transactions.add(transaction);
            }
        } catch (SQLException ex) {

            logger.error("Exception at checkLogin during User retrieve : {}", ex);
        }

        return transactions;
    }

    private void closeEverything(PreparedStatement pst, Connection dbConnection) {

        try {

            if (pst != null) {
                pst.close();
            }
            dbConnection.close();

        } catch (SQLException ex) {
            logger.error("Exception at closeEverything : {}", ex);
        }
    }

    public String makeTransaction(String fromAccountNumber, String toAccountNumber,
                                  String amount, Connection dbConnection) throws SQLException {

        /**
         * Check if fromAccount and toAccount are valid accounts, otherwise return error.
         * Query balance of fromAccount. If balance of fromAccount is less than amount, return error.
         * If all correct, make transaction.
         */

        statement = dbConnection.createStatement();
        PreparedStatement pst = null;

        String fromAccountQuery = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + ACCOUNT_COLUMN_ACCOUNT_NUMBER + "='" + fromAccountNumber + "'";
        String toAccountQuery = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + ACCOUNT_COLUMN_ACCOUNT_NUMBER + "='" + toAccountNumber + "'";

        Account fromAccount = null;
        Account toAccount = null;

        resultSet = getResultSet(fromAccountQuery, dbConnection);
        if (resultSet.next()) {

            fromAccount = new Account();
            fromAccount.setAccountNumber(resultSet.getString(ACCOUNT_COLUMN_ACCOUNT_NUMBER));
            fromAccount.setAccountType(resultSet.getString(ACCOUNT_COLUMN_TYPE));
            fromAccount.setBalance(resultSet.getLong(ACCOUNT_COLUMN_BALANCE));
            fromAccount.setUserId(resultSet.getLong(ACCOUNT_COLUMN_USER_ID));
        }

        resultSet = getResultSet(toAccountQuery, dbConnection);
        if (resultSet.next()) {

            toAccount = new Account();
            toAccount.setAccountNumber(resultSet.getString(ACCOUNT_COLUMN_ACCOUNT_NUMBER));
            toAccount.setAccountType(resultSet.getString(ACCOUNT_COLUMN_TYPE));
            toAccount.setBalance(resultSet.getLong(ACCOUNT_COLUMN_BALANCE));
            toAccount.setUserId(resultSet.getLong(ACCOUNT_COLUMN_USER_ID));
        }

        if (fromAccount == null || toAccount == null) {
            return TRANSACTION_INVALID_ACCOUNTS;
        } else if (fromAccount.getBalance() < Long.valueOf(amount)) {
            return INSUFFICIENT_BALANCE;
        } else {

            fromAccount.setBalance(fromAccount.getBalance() - Long.valueOf(amount));
            toAccount.setBalance(toAccount.getBalance() + Long.valueOf(amount));

            String changeFromQuery = "UPDATE " + TABLE_ACCOUNT + " SET "
                    + ACCOUNT_COLUMN_BALANCE + "='" + fromAccount.getBalance() + "' WHERE "
                    + ACCOUNT_COLUMN_ACCOUNT_NUMBER + "='" + fromAccountNumber + "'";
            String changeToQuery = "UPDATE " + TABLE_ACCOUNT + " SET "
                    + ACCOUNT_COLUMN_BALANCE + "='" + toAccount.getBalance() + "' WHERE "
                    + ACCOUNT_COLUMN_ACCOUNT_NUMBER + "='" + toAccountNumber + "'";

            Transaction transaction = new Transaction();
            transaction.setFromAccountId(Long.valueOf(fromAccountNumber));
            transaction.setToAccountId(Long.valueOf(toAccountNumber));
            transaction.setAmount(Long.valueOf(amount));

            String transactionQuery = "INSERT INTO " + TABLE_TRANSACTION
                    + "(" + TRANSACTION_COLUMN_FROM_ACCOUNT
                    + ", " + TRANSACTION_COLUMN_TO_ACCOUNT
                    + ", " + TRANSACTION_COLUMN_AMOUNT
                    + ", " + TRANSACTION_COLUMN_TIME
                    + ") VALUES(?, ?, ?, ?)";

            try {

                logger.warn("Transaction time : {}", new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                pst = dbConnection.prepareStatement(transactionQuery);

                pst.setString(1, fromAccountNumber);
                pst.setString(2, toAccountNumber);
                pst.setString(3, amount);
                pst.setDate(4, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                pst.executeUpdate();
                statement.executeUpdate(changeFromQuery);
                statement.executeUpdate(changeToQuery);
            } catch (SQLException ex) {
                logger.error("Exception at makeTransaction : {}", ex);
            } finally {
                closeEverything(pst, dbConnection);
            }

            return TRANSACTION_DONE;
        }
    }

    public String addMoney(String accountNumber, Long amount, Connection dbConnection) {

        String result = "";
        PreparedStatement pst = null;

        Account toAccount = null;
        try {
            statement = dbConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            result = SOME_ERROR;
        }

        String toAccountQuery = "SELECT * FROM " + TABLE_ACCOUNT +
                " WHERE " + ACCOUNT_COLUMN_ACCOUNT_NUMBER + "='" + accountNumber + "'";

        resultSet = getResultSet(toAccountQuery, dbConnection);

        try {
            if (resultSet.next()) {

                toAccount = new Account();
                toAccount.setAccountNumber(resultSet.getString(ACCOUNT_COLUMN_ACCOUNT_NUMBER));
                toAccount.setAccountType(resultSet.getString(ACCOUNT_COLUMN_TYPE));
                toAccount.setBalance(resultSet.getLong(ACCOUNT_COLUMN_BALANCE));
                toAccount.setUserId(resultSet.getLong(ACCOUNT_COLUMN_USER_ID));
            }
        } catch (SQLException ex) {
            logger.error("Exception at addMoney : {}", ex);
            result = SOME_ERROR;
        }

        logger.warn("amount : {}", amount);
        logger.warn("setBalance before : {}", toAccount.getBalance());
        toAccount.setBalance(toAccount.getBalance() + amount);
        logger.warn("setBalance after : {}", toAccount.getBalance());

        String changeToQuery = "UPDATE " + TABLE_ACCOUNT + " SET "
                + ACCOUNT_COLUMN_BALANCE + "='" + toAccount.getBalance() + "' WHERE "
                + ACCOUNT_COLUMN_ACCOUNT_NUMBER + "='" + accountNumber + "'";

        String transactionQuery = "INSERT INTO " + TABLE_TRANSACTION
                + "(" + TRANSACTION_COLUMN_FROM_ACCOUNT
                + ", " + TRANSACTION_COLUMN_TO_ACCOUNT
                + ", " + TRANSACTION_COLUMN_AMOUNT
                + ", " + TRANSACTION_COLUMN_TIME
                + ") VALUES(?, ?, ?, ?)";

        try {

            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            logger.warn("addMoney : date {}", date);

            pst = dbConnection.prepareStatement(transactionQuery);

            pst.setString(1, "000");
            pst.setString(2, accountNumber);
            pst.setString(3, String.valueOf(amount));
            pst.setDate(4, date);
            pst.executeUpdate();

            statement.executeUpdate(changeToQuery);
            result = "Done";
        } catch (SQLException e) {
            e.printStackTrace();
            result = SOME_ERROR;
        } finally {
            closeEverything(pst, dbConnection);
        }

        logger.warn("result : {}", result);

        return result;
    }
}
