package org.art.db;

import org.art.db.exceptions.DbManagerException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

/**
 * This class provides connection with the database with
 * conventional methods of {@link DriverManager} (without the usage of Connection pools)
 */
public class ConnectionManager {

    private static volatile boolean isDriverLoaded = false;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        //Resource loading
        ResourceBundle rb = ResourceBundle.getBundle("database");
        if (rb == null) {
            URL = "UNDEFINED";
            USER = "UNDEFINED";
            PASSWORD = "UNDEFINED";
            System.out.println("Resource bundle wasn't initialized!");
        } else {
            URL = rb.getString("url");
            USER = rb.getString("user");
            PASSWORD = rb.getString("password");

            //Drivers loading
            ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class);
            //Drivers enumeration
            Enumeration<Driver> myDrivers = DriverManager.getDrivers();
            System.out.println("Loaded drivers:");
            while (myDrivers.hasMoreElements()) {
                Driver driver = myDrivers.nextElement();
                System.out.println(driver);
                if (driver.getClass().getName().toString().equals(rb.getString("driver"))) {
                    isDriverLoaded = true;
                }
            }
        }
    }

    public static Connection getConnection() throws DbManagerException {

        if(!isDriverLoaded) {
            throw new DbManagerException("Driver wasn't loaded!");
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DbManagerException("Error: could not get connection!");
        }
    }
}
