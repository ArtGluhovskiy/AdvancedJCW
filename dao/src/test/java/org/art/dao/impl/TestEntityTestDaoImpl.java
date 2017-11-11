package org.art.dao.impl;

import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.utils.EMUtil;
import org.art.entities.TestEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TestEntityTestDaoImpl {
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
    @DisplayName("TestEntity test (with Spring Data)")
    void test2() throws DAOSystemException {
        TestEntityDaoImpl testEntityDao = context.getBean("testEntityDaoImpl", TestEntityDaoImpl.class);
        assertNotNull(testEntityDao);
        TestEntity testEntity1 = new TestEntity(null, "Hello from test entity 1");
        TestEntity testEntity2 = new TestEntity(null, "Hello from test entity 2");
        testEntity1 = testEntityDao.save(testEntity1);
        testEntity2 = testEntityDao.save(testEntity2);

        assertNotNull(testEntity1);
        assertNotNull(testEntity2);

        Long id1 = testEntity1.getId();
        Long id2 = testEntity2.getId();

        TestEntity t1 = testEntityDao.get(id1);
        assertEquals("Hello from test entity 1", t1.getName());
        TestEntity t2 = testEntityDao.get(id2);
        assertEquals("Hello from test entity 2", t2.getName());

        t1.setName("Modified name");
        testEntityDao.save(t1);

        TestEntity tMod = testEntityDao.get(id1);
        assertEquals("Modified name", t1.getName());

        testEntityDao.delete(id2);
        TestEntity tDel = testEntityDao.get(id2);
        assertNull(tDel);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        EMUtil.closeEMFactory();
    }
}
