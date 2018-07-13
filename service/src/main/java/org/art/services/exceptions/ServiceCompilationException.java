package org.art.services.exceptions;

/**
 * {@code ServiceCompilationException} is thrown in case of problems
 * related to the string compilation (with java task) with the help of
 * {@link javax.tools.JavaCompiler}.
 */
public class ServiceCompilationException extends ServiceBusinessException {

    public ServiceCompilationException(String message) {
        super(message);
    }

    public ServiceCompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
