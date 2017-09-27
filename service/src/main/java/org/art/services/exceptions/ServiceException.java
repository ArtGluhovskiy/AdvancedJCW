package org.art.services.exceptions;

/**
 * {@code ServiceException} is actually a super class
 * for some other types of exceptions in Service classes
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
