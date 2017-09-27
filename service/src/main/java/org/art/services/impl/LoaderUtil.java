package org.art.services.impl;

import org.art.entities.JavaTask;

import java.io.*;

/**
 * Deprecated class!
 */
public class LoaderUtil {

    public static String loadCodeStringFromTextFile(String initCodePath) {
        String line;
        String initCodeString = null;
        try (BufferedReader in = new BufferedReader(new FileReader(new File(initCodePath)));
             StringWriter out = new StringWriter()) {
            while ((line = in.readLine()) != null) {
                out.write(line);
            }
            initCodeString = out.toString();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return initCodeString;
    }

    public static JavaTask loadTaskFromFile(String filePath) {

        JavaTask loadedTask = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(filePath)))) {
            loadedTask = (JavaTask) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return loadedTask;
    }
}
