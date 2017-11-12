package org.art.dao.impl;

import org.art.dao.utils.EMUtil;
import org.art.entities.JavaTask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.sql.SQLException;

class TestJavaTaskDaoImpl {

    static ApplicationContext context;

    @BeforeAll
    static void initAll() throws SQLException {
        EMUtil.initEMFactory("org.art.dao.test");
        context = new ClassPathXmlApplicationContext("beans-dao.xml");
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

    @AfterAll
    static void tearDown() throws SQLException {
        EMUtil.closeEMFactory();
    }
}
