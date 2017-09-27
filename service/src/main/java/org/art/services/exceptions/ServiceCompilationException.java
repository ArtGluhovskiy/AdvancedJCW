package org.art.services.exceptions;

/**
 * {@code ServiceCompilationException} is thrown in case of problems
 * connected with the compiling of string (with java task) by means of
 * {@link javax.tools.JavaCompiler}
 */
public class ServiceCompilationException extends ServiceBusinessException {

    public ServiceCompilationException(String message) {
        super(message);
    }

    public ServiceCompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
