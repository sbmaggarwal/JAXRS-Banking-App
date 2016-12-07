package com.andrei.database;

/**
 * Created by shubham on 03/12/16.
 */
public class Constants {

    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/bankapp";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PD = "";

    public static final String TABLE_USER = "user";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_PD = "password";
    public static final String USER_COLUMN_ADDRESS = "address";

    public static final String TABLE_ACCOUNT = "account";
    public static final String ACCOUNT_COLUMN_ACCOUNT_NUMBER = "account_number";
    public static final String ACCOUNT_COLUMN_BALANCE = "balance";
    public static final String ACCOUNT_COLUMN_TYPE= "type";
    public static final String ACCOUNT_COLUMN_USER_ID = "user_id";
    public static final String ACCOUNT_TYPE_SAVINGS = "savings";
    public static final String ACCOUNT_TYPE_CURRENT = "current";

    public static final String TABLE_TRANSACTION = "transaction";
    public static final String TRANSACTION_COLUMN_ID = "id";
    public static final String TRANSACTION_COLUMN_FROM_ACCOUNT = "from_account";
    public static final String TRANSACTION_COLUMN_TO_ACCOUNT = "to_account";
    public static final String TRANSACTION_COLUMN_AMOUNT = "amount";
    public static final String TRANSACTION_COLUMN_TIME = "timestamp";

    public static final String ACCOUNT_ADDED = "Account added. Refresh page to see changes.";
}