package com.andrei.controllers.user;

import com.andrei.database.DBConnection;
import com.andrei.model.Account;
import com.andrei.model.Transaction;
import com.andrei.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.andrei.database.Constants.*;

/**
 * Created by shubham on 03/12/16.
 */
public class UserService {

    private static final Logger logger = LogManager.getLogger("UserService");
    private static Connection dbConnection;

    private static UserService instance = null;

    //a private constructor so no instances can be made outside this class
    private UserService() {
    }

    //Everytime you need an instance, call this
    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService();

        return instance;
    }

    public static User checkLogin(String email, String password) {

        String query = "SELECT * FROM user WHERE email='" + email + "' AND password = '" + password + "'";

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at checkLogin : {}", ex);
        }
        ResultSet resultSet = connection.getResultSet(query, dbConnection);
        User user = null;

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
        } finally {

            try {
                resultSet.close();

                if (dbConnection != null)
                    dbConnection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    public User register(String email, String name, String address, String password) throws SQLException {

        DBConnection connection = new DBConnection();

        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {
            logger.error("Exception at opening dbConnection at register : {}", ex);
        }

        return connection.registerUser(email, name, address, password, dbConnection);
    }

    public void addNewAccount(String id, String type) {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at addNewAccount : {}", ex);
        }

        connection.addNewAccount(id, type, dbConnection);
    }

    public List<Account> getUserAccounts(long user) {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at addNewAccount : {}", ex);
        }

        return connection.getAllAccountsOfUser((int) user, dbConnection);
    }

    public List<Transaction> getUserTransactions(long user) {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at getUserTransactions : {}", ex);
        }

        return connection.getAllUserTransactions((int) user, dbConnection);
    }

    public String makeTransaction(String fromAccount, String toAccount, String amount) throws SQLException {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at makeTransaction : {}", ex);
        }

        return connection.makeTransaction(fromAccount, toAccount, amount, dbConnection);
    }

    public String addMoney(String toAccount, String amount) {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at addMoney : {}", ex);
        }

        return connection.addMoney(toAccount, Long.valueOf(amount), dbConnection);
    }

    public String withdrawMoney(String toAccount, String amount) {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at addMoney : {}", ex);
        }

        long money = Long.valueOf(amount);

        money = -1 * money;
        return connection.addMoney(toAccount, money, dbConnection);
    }

    public String getBalance(String userid, String account) throws SQLException {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at getBalance : {}", ex);
        }

        return connection.getBalance(userid, account, dbConnection);
    }

    public String lodgement(String userId, String account, String amount) throws SQLException {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at lodgement : {}", ex);
        }

        return connection.lodgement(userId, account, amount, dbConnection);
    }

    public String transfer(String fromAccount, String toAccount, String amount) throws SQLException {

        DBConnection connection = new DBConnection();
        try {
            dbConnection = DBConnection.setDbConnection();
        } catch (SQLException ex) {

            logger.error("Exception at opening dbConnection at lodgement : {}", ex);
        }

        return connection.makeTransaction(fromAccount, toAccount, amount, dbConnection);
    }
}
