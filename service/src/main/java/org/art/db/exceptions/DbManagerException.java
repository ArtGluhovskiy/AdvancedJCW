package org.art.db.exceptions;

/**
 * {@code DbManagerException} is thrown in case of problems while
 * getting connection with the database (with the usage of {@link java.sql.DriverManager})
 */
public class DbManagerException extends RuntimeException {

    public DbManagerException(String message) {
        super(message);
    }

    public DbManagerException(Throwable e) {
        super(e);
    }

    public DbManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
