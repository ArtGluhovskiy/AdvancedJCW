package org.art.dao.impl;

import org.art.dao.utils.EMUtil;
import org.art.entities.DifficultyGroup;
import org.art.entities.TestEntity;
import org.art.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.sql.SQLException;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUserDaoImpl {

    @BeforeAll
    static void initAll() throws SQLException {
        EMUtil.initEMFactory("org.art.dao.test");
    }

    @Test
    @DisplayName("Hibernate configuration test")
    void test1() throws Exception {

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();

        TestEntity entity = new TestEntity(null, "Hello test entity");

        em.persist(entity);

        em.getTransaction().commit();
    }

    @Test
    @DisplayName("User persistence test")
    void test2() throws Exception {

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();

        User user = new User("Sharks", "gooder", "8273gds", "Allen",
                "Swift", "swift@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());

        user.setRating(1);
        em.persist(user);
        Long userId = user.getUserID();
        System.out.println(userId);
        em.getTransaction().commit();
        em.clear();

        User user1 = em.find(User.class, userId);
        em.close();

        assertAll(() -> assertEquals("Sharks", user1.getClanName()),
                () -> assertEquals("gooder", user1.getLogin()),
                () -> assertEquals("ACTIVE", user1.getStatus()));
    }

    @Test
    @DisplayName("User getting test")
    void getUserTest() throws Exception {


//        User user = new User("Sharks", "gooder", "8273gds", "Allen",
//                "Swift", "swift@gmail.com", new Date(System.currentTimeMillis()), "user",
//                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());
//
//        User user1 = userDao.save(user);
//        User user2 = userDao.get(user1.getUserID());
//        assertEquals(user1, user2);

    }

    @AfterAll
    static void tearDown() throws SQLException {
        EMUtil.closeEMFactory();
    }
}
