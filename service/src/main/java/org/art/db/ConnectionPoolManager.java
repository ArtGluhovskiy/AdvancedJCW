package org.art.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.db.exceptions.DbManagerException;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides the usage of Connection poop (C3P0)
 * while getting connection with the database
 */
public class ConnectionPoolManager {

    private static ConnectionPoolManager INSTANCE;
    public static final Logger log = LogManager.getLogger(ConnectionPoolManager.class);
    private ComboPooledDataSource cpds;
    private static ResourceBundle rb;
    private final String DRIVER;
    private final String URL;
    private final String USER;
    private final String PASSWORD;
    private ThreadLocal<Connection> threadCache;

    private ConnectionPoolManager() throws DbManagerException {

        try {
            rb = ResourceBundle.getBundle("database");
        } catch (MissingResourceException e) {
            throw new DbManagerException("Missing resource file!", e);
        }
        DRIVER = rb.getString("driver");
        URL = rb.getString("url");
        USER = rb.getString("user");
        PASSWORD = rb.getString("password");
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(DRIVER);
            cpds.setJdbcUrl(URL);
            cpds.setUser(USER);
            cpds.setPassword(PASSWORD);
        } catch (PropertyVetoException e) {
            throw new DbManagerException("Properties for connection pool represent an unacceptable values!", e);
        }
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);
        cpds.setMaxStatementsPerConnection(2);

        threadCache = new ThreadLocal<>();
    }

    public ThreadLocal<Connection> getThreadCache() {
        return threadCache;
    }

    public static ConnectionPoolManager getInstance() throws DbManagerException {
        ConnectionPoolManager dataSource = INSTANCE;
        if (dataSource == null) {
            synchronized (ConnectionPoolManager.class) {
                dataSource = INSTANCE;
                if (dataSource == null) {
                    INSTANCE = dataSource = new ConnectionPoolManager();
                }
            }
        }
        return dataSource;
    }

    public Connection getConnection() {
        try {
            return this.cpds.getConnection();
        } catch (SQLException e) {
            throw new DbManagerException("Could not get new connection!", e);
        }
    }

    public static void close(AutoCloseable res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (Exception e) {
            log.info("Exception while connection closing!", e);
            //It's ok because C3P0 provides automatic connection closing
        }
    }
}
