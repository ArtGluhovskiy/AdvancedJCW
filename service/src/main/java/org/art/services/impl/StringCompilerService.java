package org.art.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.entities.JavaTask;
import org.art.services.exceptions.ServiceCompilationException;

import javax.tools.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * This class provides string (acquired from the web-site) compilation
 * using special system class {@code JavaCompiler} into .class resource.
 * After string compilation .class resource is loaded by the
 * {@code URLClassLoader}. And finally a specified target method from loaded
 * class is invoked by means of <i>Reflection API</i> methods.
 */
public class StringCompilerService<T> {

    private boolean diagnosticsOn = false;
    public static final Logger log = LogManager.getLogger(StringCompilerService.class);

    public StringCompilerService(boolean diagnosticsOn) {
        this.diagnosticsOn = diagnosticsOn;
    }

    /**
     * <p>{@link JavaTask} compiling with the returning of result (after method
     * invocation.
     *
     * @param task
     * @param initCodeString
     * @return
     */
    public TaskResults compileTask(JavaTask task, String initCodeString) throws ServiceCompilationException {

        TaskResults result;
        String targetClassName = task.getClassName();
        String targetMethodName = task.getMethodName();
        Class[] targetMethodParametersType = task.getMethodParametersType();
        Object[] targetTestMethodParameters = task.getTestMethodParameters();

        result = compilingOperation(targetClassName, targetMethodName, targetMethodParametersType,
                targetTestMethodParameters, initCodeString, diagnosticsOn, task);
        return result;
    }

    private TaskResults compilingOperation(String className, String methodName, Class[] methodParametersType,
                                           Object[] testMethodParameters, String source, boolean diagnosticsOn, JavaTask jTask) throws ServiceCompilationException {
        long elapsedTime = 0;
        T methodResult = null;
        Class loadedClass;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileObject file = new JavaSourceFromString(className, source);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

        boolean success = task.call();
        if (diagnosticsOn) {
            printDiagnostics(diagnostics);
        }
//        System.out.println("Success: " + success);
        if (success) {
            try {
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File("").toURI().toURL()});
                loadedClass = Class.forName(className, true, classLoader);
                Method loadedMethod = loadedClass.getDeclaredMethod(methodName, methodParametersType);
                long start = System.nanoTime();
                methodResult = (T) loadedMethod.invoke(loadedClass.newInstance(), testMethodParameters);
                long end = System.nanoTime();
                elapsedTime = (end - start) / 1000;
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
                    | MalformedURLException | InstantiationException e) {
                log.info("Exception while task compilation!", e);
                throw new ServiceCompilationException("Exception while task compilation!", e);
            }
        }
        return new TaskResults(elapsedTime, methodResult);
    }

    private void printDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics) {

        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));
        }
    }

    public class TaskResults {
        private long elapsedTime;
        private T methodResult;

        TaskResults(long elapsedTime, T methodResult) {
            this.elapsedTime = elapsedTime;
            this.methodResult = methodResult;
        }

        public T getMethodResult() {
            return methodResult;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }

        @Override
        public String toString() {
            return "TaskResults {" +
                    "elapsed time = " + elapsedTime + " ms" +
                    ", method result = " + methodResult +
                    '}';
        }
    }
}
