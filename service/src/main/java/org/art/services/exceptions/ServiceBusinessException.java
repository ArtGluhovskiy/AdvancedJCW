package org.art.services.exceptions;

/**
 * {@code ServiceBusinessException} is thrown in case of problems
 * connected with entity finding in the database (if no entity
 * was found in the database)
 */
public class ServiceBusinessException extends ServiceException {

    public ServiceBusinessException(String message) {
        super(message);
    }

    public ServiceBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
