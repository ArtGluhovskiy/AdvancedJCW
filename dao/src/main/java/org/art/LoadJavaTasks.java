package org.art;

import org.art.dao.JavaTaskDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.JavaTask;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;

public class LoadJavaTasks {
    public static void main(String[] args) throws IOException, ClassNotFoundException, DAOSystemException {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-dao.xml");
        JavaTaskDao taskDao = context.getBean("javaTaskDaoImpl", JavaTaskDao.class);
        JavaTask task;
        String filePathPrefix = "C:\\Users\\HomePC\\IdeaProjects\\AdvancedJCW\\dao\\src\\main\\resources\\files\\serial-tasks\\task";
        File file;
        int a = 3;
        for (int i = 0; i < 1; i++) {
            file = new File(filePathPrefix + a + a + ".txt");
            byte[] buffer = new byte[(int) file.length()];
            InputStream in = new FileInputStream(file);
            OutputStream out = new ByteArrayOutputStream();
            in.read(buffer);
            ObjectInputStream src = new ObjectInputStream(new ByteArrayInputStream(buffer));
            task = (JavaTask) src.readObject();
            taskDao.save(task);
            a++;
        }
    }
}

