package org.art.dao.impl;

import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.utils.EMUtil;
import org.art.entities.DifficultyGroup;
import org.art.entities.JavaTask;
import org.art.entities.TestEntity;
import org.art.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.sql.SQLException;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestUserDaoImpl {

    static ApplicationContext context;

    @BeforeAll
    static void initAll() throws SQLException {
        EMUtil.initEMFactory("org.art.dao.test");
        context = new ClassPathXmlApplicationContext("beans-dao.xml");
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
    @DisplayName("Java task persistence test")
    void test3() throws Exception {

        JavaTask task = new JavaTask();

        EntityManager em = EMUtil.createEntityManager();
        em.getTransaction().begin();

        em.persist(task);
        assertNotNull(task.getId());

        em.getTransaction().commit();
        em.close();
    }

    @Test
    @DisplayName("Spring Data with TestEntity test")
    void test4() throws DAOSystemException {
        TestEntityDaoImpl testEntityDao = context.getBean("testEntityDaoImpl", TestEntityDaoImpl.class);
        assertNotNull(testEntityDao);
        TestEntity testEntity = new TestEntity(null, "Hello from test entity");
        testEntityDao.save(testEntity);


    }

    @AfterAll
    static void tearDown() throws SQLException {
        EMUtil.closeEMFactory();
    }
}
