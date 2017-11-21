package org.art.dao.impl;

import org.art.dao.JavaTaskDao;
import org.art.dao.TaskOrderDao;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.utils.EMUtil;
import org.art.dto.OrderDTO;
import org.art.entities.DifficultyGroup;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;
import static org.junit.jupiter.api.Assertions.*;

class TestTaskOrderDaoImpl {

    static ApplicationContext context;
    static UserDao userDao;
    static TaskOrderDao orderDao;
    static JavaTaskDao taskDao;

    @BeforeAll
    static void initAll() throws SQLException {
        EMUtil.initEMFactory("org.art.dao.test");
        context = new ClassPathXmlApplicationContext("beans-dao.xml");
        orderDao = context.getBean("taskOrderDaoImpl", TaskOrderDao.class);
        assertNotNull(orderDao);
        userDao = context.getBean("userDaoImpl", UserDao.class);
        assertNotNull(userDao);
        taskDao = context.getBean("javaTaskDaoImpl", JavaTaskDao.class);
    }

    @Test
    @DisplayName("Task order CRUD test")
    void test1() {
        User user = new User("Sparky", "godners", "82730okgds", "Allen",
                "Swift", "swift@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());
        TaskOrder order1 = new TaskOrder("SOLVED", user, null);
        TaskOrder order2 = new TaskOrder("SOLVED", user, null);
        TaskOrder order3 = new TaskOrder("NOT SOLVED", user, null);
        user.getOrders().add(order1);
        user.getOrders().add(order2);
        user.getOrders().add(order3);

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        Long userId = user.getUserID();
        em.getTransaction().commit();
        em.clear();

        em.getTransaction().begin();
        User u = em.find(User.class, userId);
        assertNotNull(u);
        assertAll(() -> assertEquals("Sparky", u.getClanName()),
                () -> assertEquals(3, u.getOrders().size()));

        em.remove(u);
        em.flush();
        em.clear();
        User readUser = em.find(User.class, userId);
        assertNull(readUser);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    @DisplayName("Get not solved tasks test")
    void test2() throws DAOSystemException {
        User user = new User("Sparky1", "godners1", "82730okgds1", "Allen",
                "Swift", "swift1@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());
        TaskOrder order1 = new TaskOrder("SOLVED", user, null);
        TaskOrder order2 = new TaskOrder("SOLVED", user, null);
        TaskOrder order3 = new TaskOrder("NOT SOLVED", user, null);
        user.getOrders().add(order1);
        user.getOrders().add(order2);
        user.getOrders().add(order3);

        userDao.save(user);
        TaskOrder order = orderDao.getNotSolvedOrder(user);
        assertNotNull(order);
        assertEquals("NOT SOLVED", order.getStatus());
    }

    @Test
    @DisplayName("Get solved task orders test")
    void test3() throws DAOSystemException {
        User user = new User("Sparky2", "godners2", "82730okgds2", "Allen",
                "Swift", "swift2@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());
        JavaTask task1 = new JavaTask(10);
        JavaTask task2 = new JavaTask(15);
        JavaTask task3 = new JavaTask(20);
        taskDao.save(task1);
        taskDao.save(task2);
        taskDao.save(task3);
        TaskOrder order1 = new TaskOrder("SOLVED", user, task1);
        TaskOrder order2 = new TaskOrder("SOLVED", user, task2);
        TaskOrder order3 = new TaskOrder("NOT SOLVED", user, task3);
        user.getOrders().add(order1);
        user.getOrders().add(order2);
        user.getOrders().add(order3);

        userDao.save(user);
        List<OrderDTO> orders = orderDao.getUserSolvedTaskOrders(user.getUserID());
        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertAll(() -> assertEquals("SOLVED", orders.get(0).getOrderStatus()),
                () -> assertEquals("SOLVED", orders.get(1).getOrderStatus()));
    }

    @Test
    @DisplayName("Simple task order test with java task")
    void test4() throws DAOSystemException {

        User user = new User("Sparky4", "godners4", "82730o4kgds2", "Allen",
                "Swift", "swift4@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());
        userDao.save(user);
        JavaTask task = new JavaTask(10);
        taskDao.save(task);
        TaskOrder order1 = new TaskOrder("SOLVED", user, task);

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();
        em.persist(order1);
        Long orderId = order1.getOrderID();
        em.getTransaction().commit();
        em.clear();
        em.getTransaction().begin();
        TaskOrder readedOrder = em.find(TaskOrder.class, orderId);
        assertNotNull(readedOrder);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    @DisplayName("Null tests")
    void test5() throws DAOSystemException {

        User user = new User("Spafrky4", "godfners4", "82730fo4kgds2", "Allen",
                "Swift", "swifft4@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());
        userDao.save(user);

        assertNull(orderDao.get(999L));
        assertNull(orderDao.getNotSolvedOrder(user));
        assertTrue(orderDao.getOrders(user).size() == 0);
        assertTrue(orderDao.getUserSolvedTaskOrders(999L).size() == 0);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        EMUtil.closeEMFactory();
        ((ClassPathXmlApplicationContext) context).close();
    }
}
