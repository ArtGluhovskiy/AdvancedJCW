package org.art.dao.impl;

import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.utils.EMUtil;
import org.art.entities.DifficultyGroup;
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

class TestUserDaoImpl {

    static ApplicationContext context;

    @BeforeAll
    static void initAll() throws SQLException {
        EMUtil.initEMFactory("org.art.dao.test");
        context = new ClassPathXmlApplicationContext("beans-dao.xml");
    }

    @Test
    @DisplayName("User persistence test")
    void test1() throws Exception {

        User user = new User("Sharks", "gooder", "8273gds", "Allen",
                "Swift", "swift@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();

        user.setRating(1);
        em.persist(user);
        Long userId = user.getUserID();
        em.getTransaction().commit();
        java.sql.Date regDate = user.getRegDate();
        em.clear();

        //Attempt to change regDate, which is annotated as @CreationTimeStamp
        em.getTransaction().begin();
        User user1 = em.find(User.class, userId);
        user1.setRegDate(new Date(System.currentTimeMillis() + 1000000000));
        user1.setRating(2);
        em.getTransaction().commit();
        em.clear();

        User user2 = em.find(User.class, userId);
        assertEquals(regDate.toLocalDate().getDayOfYear(), user2.getRegDate().toLocalDate().getDayOfYear());
        em.close();

        assertAll(() -> assertEquals("Sharks", user2.getClanName()),
                () -> assertEquals("gooder", user2.getLogin()),
                () -> assertEquals("ACTIVE", user2.getStatus()));
    }

    @Test
    @DisplayName("User CRUD operations test")
    void test2() throws DAOSystemException {
        User user1 = new User("Spoons", "harick", "87jhy", "Harry",
                "Jane", "harry@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());
        User user2 = new User("Sharks", "rooben", "uieu749", "Robert",
                "Nolen", "robert@gmail.com", new Date(System.currentTimeMillis() + 10000000), "user",
                "ACTIVE", toSQLDate("19-07-1993"), DifficultyGroup.EXPERIENCED.toString());
        User user3 = new User("Missles", "browns", "87hioly", "Bob",
                "Roven", "bob@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("13-01-1996"), DifficultyGroup.EXPERT.toString());

        UserDao userDao = context.getBean("userDaoImpl", UserDaoImpl.class);
        assertNotNull(userDao);

        //Save operations
        user1 = userDao.save(user1);
        user2 = userDao.save(user2);
        user3 = userDao.save(user3);
        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);

        Long id1 = user1.getUserID();
        Long id2 = user2.getUserID();
        Long id3 = user3.getUserID();

        //Read operations
        assertAll(() -> assertNotNull("harry@gmail.com", userDao.get(id1).getEmail()),
                () -> assertNotNull("robert@gmail.com", userDao.get(id2).getEmail()),
                () -> assertNotNull("bob@gmail.com", userDao.get(id3).getEmail()));

        //Update operations
        user1.setRating(10);
        user2.setLogin("login");
        user3.setFName("Art");

        userDao.save(user1);
        userDao.save(user2);
        userDao.save(user3);

        assertAll(() -> assertEquals(10, userDao.get(id1).getRating()),
                () -> assertEquals("login", userDao.get(id2).getLogin()),
                () -> assertEquals("Art", userDao.get(id3).getFName()));

        //Delete operations
        userDao.delete(id1);
        assertNull(userDao.get(id1));
    }

    @Test
    @DisplayName("GetUsersByClanName test")
    void test3() throws DAOSystemException {
        User user = new User("Spoons", "harick", "87jhy", "Harry",
                "Jane", "harry@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());

        UserDao userDao = context.getBean("userDaoImpl", UserDaoImpl.class);
        assertNotNull(userDao);

        User u = userDao.save(user);
        assertNotNull(u);
        List<User> users = userDao.getUsersByClanName("Spoons");
        assertEquals(1, users.size());
        assertEquals(users.get(0).getFName(), "Harry");
    }

    @Test
    @DisplayName("GetUserByLogin test")
    void test4() throws DAOSystemException {
        User user = new User("Sparks", "patrick", "87jhy", "Harry",
                "Jane", "harry@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());

        UserDao userDao = context.getBean("userDaoImpl", UserDaoImpl.class);
        assertNotNull(userDao);

        User u = userDao.save(user);
        assertNotNull(u);
        User rUser = userDao.getUserByLogin("patrick");
        assertEquals("Sparks", rUser.getClanName());
    }

    @Test
    @DisplayName("GetTopUsers test")
    void test5() throws DAOSystemException {
        User user1 = new User("Spons1", "harick1", "87jhy1", "Harry",
                "Jane", "harry1@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());
        User user2 = new User("Shacks1", "rooben1", "uieu7491", "Robert",
                "Nolen", "robert1@gmail.com", new Date(System.currentTimeMillis() + 10000000), "user",
                "ACTIVE", toSQLDate("19-07-1993"), DifficultyGroup.EXPERIENCED.toString());
        User user3 = new User("Missles1", "browns1", "87hioly1", "Bob",
                "Roven", "bob1@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("13-01-1996"), DifficultyGroup.EXPERT.toString());
        user1.setRating(2);
        user2.setRating(1);
        user3.setRating(5);

        UserDao userDao = context.getBean("userDaoImpl", UserDaoImpl.class);
        assertNotNull(userDao);
        userDao.save(user1);
        userDao.save(user2);
        userDao.save(user3);

        List<User> users = userDao.getTopUsers(2);
        assertAll(() -> assertEquals(2, users.size()),
                () -> assertTrue(users.get(0).getRating() >= users.get(1).getRating()));
    }

    @Test
    @DisplayName("GetAllUsers test")
    void test6() throws DAOSystemException {
        User user1 = new User("Spons2", "harick2", "87jhy12", "Harry",
                "Jane", "harry2@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());

        UserDao userDao = context.getBean("userDaoImpl", UserDaoImpl.class);
        assertNotNull(userDao);

        userDao.save(user1);
        List<User> users = userDao.getAllUsers();
        assertNotEquals(0, users.size());
    }

    @Test
    @DisplayName("Get all task orders test")
    void test7() {
        User user = new User("Spons3", "harick3", "87jhy1267", "Harry",
                "Jane", "harry2@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());
        TaskOrder order1 = new TaskOrder("NOT SOLVED");
        TaskOrder order2 = new TaskOrder("NOT SOLVED");
        TaskOrder order3 = new TaskOrder("NOT SOLVED");
        order1.setUser(user);
        order2.setUser(user);
        order3.setUser(user);
        user.getOrders().add(order1);
        user.getOrders().add(order2);
        user.getOrders().add(order3);

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        Long id = user.getUserID();
        em.getTransaction().commit();
        em.clear();

        User u = em.find(User.class, id);
        assertNotNull(u);
        assertEquals(3, u.getOrders().size());
    }

    @Test
    @DisplayName("Delete user with his orders test")
    void test8() {
        User user = new User("Spons4", "harick4", "87jhy12647", "Harry",
                "Jane", "harry4@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());
        TaskOrder order1 = new TaskOrder("NOT SOLVED");
        TaskOrder order2 = new TaskOrder("NOT SOLVED");
        TaskOrder order3 = new TaskOrder("NOT SOLVED");
        order1.setUser(user);
        order2.setUser(user);
        order3.setUser(user);
        user.getOrders().add(order1);
        user.getOrders().add(order2);
        user.getOrders().add(order3);

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        Long id = user.getUserID();
        em.flush();
        em.clear();

        user = em.merge(user);
        em.remove(user);
        em.getTransaction().commit();
        em.clear();

        User u = em.find(User.class, id);
        assertNull(u);
    }


    @AfterAll
    static void tearDown() throws SQLException {
        EMUtil.closeEMFactory();
    }
}
