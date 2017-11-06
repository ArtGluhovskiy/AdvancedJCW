package org.art.dao.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.exceptions.TestDbManagerException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DbcpConnectionPool {

    public static final Logger log = LogManager.getLogger(DbcpConnectionPool.class);

    private static DbcpConnectionPool INSTANCE;
    private static BasicDataSource dataSource;
    private static ResourceBundle rb;

    private final String DRIVER;
    private final String URL;
    private final String USER;
    private final String PASSWORD;
    private ThreadLocal<Connection> threadCache;

    private DbcpConnectionPool() throws TestDbManagerException {

        try {
            rb = ResourceBundle.getBundle("testDatabase");
        } catch (MissingResourceException e) {
            throw new TestDbManagerException("Missing resource file!", e);
        }
        DRIVER = rb.getString("driver");
        URL = rb.getString("url");
        USER = rb.getString("user");
        PASSWORD = rb.getString("password");
        dataSource = new BasicDataSource();
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);

        threadCache = new ThreadLocal<>();
    }

    public ThreadLocal<Connection> getThreadCache() {
        return threadCache;
    }

    public static DbcpConnectionPool getInstance() throws TestDbManagerException {
        DbcpConnectionPool dataSource = INSTANCE;
        if (dataSource == null) {
            synchronized (DbcpConnectionPool.class) {
                dataSource = INSTANCE;
                if (dataSource == null) {
                    INSTANCE = dataSource = new DbcpConnectionPool();
                }
            }
        }
        return dataSource;
    }

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new TestDbManagerException("Could not get new connection!", e);
        }

    }

    public static void close(AutoCloseable res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (Exception e) {
            log.info("Exception while connection closing!", e);
            //It's ok, because DBCP provides automatic connection closing
        }
    }
}
