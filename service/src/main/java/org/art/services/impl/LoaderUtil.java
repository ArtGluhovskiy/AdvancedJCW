package org.art.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.entities.JavaTask;

import java.io.*;

/**
 * Helper class. Loads java tasks from file with specified path.
 */
public class LoaderUtil {

    private static final Logger LOG = LogManager.getLogger(LoaderUtil.class);

    public static String loadCodeStringFromTextFile(String codeFilePath) {
        String initCodeString = null;
        try (BufferedReader in = new BufferedReader(new FileReader(new File(codeFilePath)));
             StringWriter out = new StringWriter()) {
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
            }
            initCodeString = out.toString();
        } catch (FileNotFoundException e) {
            LOG.error("File not found: {}", codeFilePath, e);
        } catch (IOException e) {
            LOG.error("IOException while task loading has been caught!", e);
        }
        return initCodeString;
    }

    public static JavaTask loadTaskFromFile(String codeFilePath) {
        JavaTask loadedTask = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(codeFilePath)))) {
            loadedTask = (JavaTask) in.readObject();
        } catch (FileNotFoundException e) {
            LOG.error("File not found: {}", codeFilePath, e);
        } catch (ClassNotFoundException e) {
            LOG.error("ClassNotFoundException while task loading has been caught!", e);
        } catch (IOException e) {
            LOG.error("IOException while task loading has been caught!", e);
        }
        return loadedTask;
    }
}
