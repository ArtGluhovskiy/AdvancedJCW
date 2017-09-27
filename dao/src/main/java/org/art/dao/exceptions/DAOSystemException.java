package org.art.dao.exceptions;

/**
 * {@code DAOSystemException} is actually
 * a wrapper (in most cases) for {@link java.sql.SQLException} in DAO classes
 */
public class DAOSystemException extends Exception {

    public DAOSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
