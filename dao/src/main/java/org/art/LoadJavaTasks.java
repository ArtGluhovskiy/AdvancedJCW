package org.art;

import org.art.dao.JavaTaskDao;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.DifficultyGroup;
import org.art.entities.JavaTask;
import org.art.entities.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.sql.Date;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;

public class LoadJavaTasks {
    public static void main(String[] args) throws IOException, ClassNotFoundException, DAOSystemException {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-dao.xml");
        JavaTaskDao taskDao = context.getBean("javaTaskDaoImpl", JavaTaskDao.class);
        UserDao userDao = context.getBean("userDaoImpl", UserDao.class);
//        JavaTask task;
//        String filePathPrefix = "C:\\Users\\HomePC\\IdeaProjects\\AdvancedJCW\\dao\\src\\main\\resources\\files\\serial-tasks\\task";
//        File file;
//        int a = 3;
//        for (int i = 0; i < 1; i++) {
//            file = new File(filePathPrefix + a + a + ".txt");
//            byte[] buffer = new byte[(int) file.length()];
//            InputStream in = new FileInputStream(file);
//            OutputStream out = new ByteArrayOutputStream();
//            in.read(buffer);
//            ObjectInputStream src = new ObjectInputStream(new ByteArrayInputStream(buffer));
//            task = (JavaTask) src.readObject();
//            taskDao.save(task);
//            a++;
//        }

//        JavaTask task = new JavaTask();
//        taskDao.save(task);

//        User user = new User("Snows", "Snowman", "8g27f3gds", "Max",
//                "Plank", "plank@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
//                "ACTIVE", toSQLDate("09-01-1991"), DifficultyGroup.EXPERIENCED.toString());
//        user.setRating(5);
//        userDao.save(user);

//        User user1 = new User("Browns", "rivens", "8g2g7f3gds", "Poll",
//                "Yoker", "yoker@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
//                "ACTIVE", toSQLDate("09-01-1978"), DifficultyGroup.BEGINNER.toString());
//        user1.setRating(3);
//        userDao.save(user1);

//        User user2 = new User("Swimmers", "shark", "8g2g7ff3gds", "Harry",
//                "Potter", "potter@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
//                "ACTIVE", toSQLDate("04-01-1977"), DifficultyGroup.EXPERT.toString());
//        user2.setRating(15);
//        userDao.save(user2);

//        User user3 = new User("Kalsen", "koller", "8g2fg7ffg3gds", "Merins",
//                "Jonson", "jonson@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
//                "ACTIVE", toSQLDate("01-12-1975"), DifficultyGroup.EXPERT.toString());
//        user3.setRating(5);
//        userDao.save(user3);

//        User user4 = new User("Kalsen", "kolller", "8g2fg7ffg3gds", "Merins",
//                "Jonson", "jonson@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
//                "ACTIVE", toSQLDate("01-12-1975"), DifficultyGroup.EXPERT.toString());
//        user4.setRating(17);
//        userDao.save(user4);

//        User user5 = new User("Kalsen", "Richardson", "8g2fg7fffg3gds", "Merins",
//                "Jonson", "jonson@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
//                "ACTIVE", toSQLDate("01-12-1975"), DifficultyGroup.EXPERT.toString());
//        user5.setRating(26);
//        userDao.save(user5);

//        User user6 = new User("Loony", "lonney", "8g2fgc7fffg3gds", "Merins",
//                "Jonson", "jonson@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
//                "ACTIVE", toSQLDate("01-12-1975"), DifficultyGroup.EXPERT.toString());
//        user6.setRating(33);
//        userDao.save(user6);
    }
}

