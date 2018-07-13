package org.art.dao.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.exceptions.DbManagerException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DbcpConnectionPool {

    public static final Logger LOG = LogManager.getLogger(DbcpConnectionPool.class);

    private static volatile DbcpConnectionPool INSTANCE;

    private static BasicDataSource dataSource;

    private ThreadLocal<Connection> threadCache;

    private DbcpConnectionPool() throws DbManagerException {
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle("testDatabase");
        } catch (MissingResourceException e) {
            LOG.error("Error: Missing resource file!", e);
            throw new DbManagerException("Error: Missing resource file!", e);
        }
        String DRIVER = rb.getString("driver");
        String URL = rb.getString("url");
        String USER = rb.getString("user");
        String PASSWORD = rb.getString("password");
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

    public static DbcpConnectionPool getInstance() throws DbManagerException {
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
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOG.error("Cannot get new connection!", e);
            throw new DbManagerException("Cannot get new connection!", e);
        }

    }

    public static void close(AutoCloseable res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (Exception e) {
            LOG.info("Exception while connection closing!", e);
            //It's ok, DBCP provides automatic connection closing
        }
    }
}
