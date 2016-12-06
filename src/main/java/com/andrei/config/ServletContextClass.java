package com.andrei.config;

import com.andrei.database.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

/**
 * Created by shubham on 03/12/16.
 */
public class ServletContextClass implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger("ServletContextClass");

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        logger.warn("At contextInitialized.");
        try {
            DBConnection.createTables();
        } catch (SQLException e) {
            logger.error("Error during creating database : {}", e);
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
