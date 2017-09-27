package org.art.dao.exceptions;

/**
 * {@code TestDbManagerException} is thrown in case of problems while
 * getting connection for the database
 */
public class TestDbManagerException extends RuntimeException {

    public TestDbManagerException(String message) {
        super(message);
    }

    public TestDbManagerException(Throwable e) {
        super(e);
    }

    public TestDbManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
