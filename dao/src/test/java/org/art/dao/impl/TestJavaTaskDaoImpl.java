package org.art.dao.impl;

import org.art.dao.JavaTaskDao;
import org.art.dao.TaskOrderDao;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.utils.EMUtil;
import org.art.entities.DifficultyGroup;
import org.art.entities.JavaTask;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestJavaTaskDaoImpl {

    static ApplicationContext context;
    static UserDao userDao;
    static TaskOrderDao orderDao;
    static JavaTaskDao taskDao;

    static Long taskId1 = 1L;

    @BeforeAll
    static void initAll() throws SQLException {
        EMUtil.initEMFactory("org.art.dao.test");
        context = new ClassPathXmlApplicationContext("beans-dao.xml");
        orderDao = context.getBean("taskOrderDaoImpl", TaskOrderDao.class);
        assertNotNull(orderDao);
        userDao = context.getBean("userDaoImpl", UserDao.class);
        assertNotNull(userDao);
        taskDao = context.getBean("javaTaskDaoImpl", JavaTaskDao.class);
        assertNotNull(taskDao);
    }

    @Test
    @DisplayName("Java task simple test")
    void test1() {

        JavaTask task = new JavaTask(10);
        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();
        em.persist(task);
        em.getTransaction().commit();
    }

    @Test
    @DisplayName("Java task save/read test")
    void test2() throws DAOSystemException {

        // TODO: get task from DB or create new?
        JavaTask task = new JavaTask();
        task.setDifficultyGroup(DifficultyGroup.EXPERIENCED.toString());
        JavaTask savedTask = taskDao.save(task);

        assertNotNull(savedTask);

        Long taskId = savedTask.getTaskId();
        JavaTask readedTask = taskDao.get(taskId);
        assertNotNull(readedTask);
        assertEquals(DifficultyGroup.EXPERIENCED.toString(), readedTask.getDifficultyGroup());
    }

    @Test
    @DisplayName("Get next task by difficulty group test")
    void test3() throws DAOSystemException {

        JavaTask task1 = new JavaTask();
        JavaTask task2 = new JavaTask();
        JavaTask task3 = new JavaTask();
        JavaTask task4 = new JavaTask();
        task1.setDifficultyGroup(DifficultyGroup.EXPERIENCED.toString());
        task2.setDifficultyGroup(DifficultyGroup.BEGINNER.toString());
        task3.setDifficultyGroup(DifficultyGroup.EXPERIENCED.toString());
        task4.setDifficultyGroup(DifficultyGroup.EXPERIENCED.toString());
        taskDao.save(task1);
        taskDao.save(task2);
        taskDao.save(task3);
        taskDao.save(task4);
        Long taskId1 = task1.getTaskId();
        Long taskId3 = task3.getTaskId();
        JavaTask readedTask = taskDao.getNextTaskByDiffGroup(DifficultyGroup.EXPERIENCED.toString(), taskId1);
        assertEquals(taskId3, readedTask.getTaskId());
    }

    @Test
    @DisplayName("Get popular tasks")
    void test4() throws DAOSystemException {
        JavaTask task1 = new JavaTask();
        JavaTask task2 = new JavaTask();
        JavaTask task3 = new JavaTask();
        JavaTask task4 = new JavaTask();
        task1.setPopularity(1);
        task2.setPopularity(3);
        task3.setPopularity(1000);
        task4.setPopularity(999);
        taskDao.save(task1);
        taskDao.save(task2);
        taskDao.save(task3);
        taskDao.save(task4);

        List<JavaTask> taskList;
        taskList = taskDao.getPopularJavaTasks(2);
        assertNotNull(taskList);
        assertEquals(2, taskList.size());
        assertAll(() -> assertEquals(1000, taskList.get(0).getPopularity()),
                () -> assertEquals(999, taskList.get(1).getPopularity()));
    }

    @Test
    @DisplayName("Get all tasks test")
    void test5() throws DAOSystemException {

        //Initial task amount in the DB
        int initialTaskAmount = taskDao.getAll().size();

        //Adding tasks
        JavaTask task1 = new JavaTask();
        JavaTask task2 = new JavaTask();
        JavaTask task3 = new JavaTask();
        JavaTask task4 = new JavaTask();
        taskDao.save(task1);
        taskDao.save(task2);
        taskDao.save(task3);
        taskDao.save(task4);

        int updatedTaskAmount = taskDao.getAll().size();
        assertEquals(initialTaskAmount + 4, updatedTaskAmount);
    }

    @Test
    @DisplayName("Null tests")
    void test6() throws DAOSystemException {
        assertNull(taskDao.get(999L));
        assertNull(taskDao.getNextTaskByDiffGroup(DifficultyGroup.EXPERT.toString(), 999L));
    }

    @AfterAll
    static void tearDown() throws SQLException {
        EMUtil.closeEMFactory();
        ((ClassPathXmlApplicationContext) context).close();
    }
}
