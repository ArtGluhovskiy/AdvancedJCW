package org.art.services.exceptions;

import org.art.dao.exceptions.DAOSystemException;

/**
 * {@code ServiceException} is actually a wrapper for {@link DAOSystemException}
 * and some other types of exceptions in Service classes.
 */
public class ServiceSystemException extends ServiceException {

    public ServiceSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
