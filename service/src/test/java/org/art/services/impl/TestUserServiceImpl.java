package org.art.services.impl;

import org.art.entities.DifficultyGroup;
import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;
import java.sql.SQLException;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;
import static org.junit.jupiter.api.Assertions.*;

class TestUserServiceImpl {

    private static ApplicationContext context;

    private static UserService userService;

    @BeforeAll
    static void initAll() {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
        userService = context.getBean("userServiceImpl", UserService.class);
        assertNotNull(userService);
    }

    @Test
    @Disabled
    @DisplayName("Service CRUD operations test")
    void test1() throws ServiceSystemException, ServiceBusinessException {
        User user1 = new User("Spons2", "harick2", "87jhy12", "Harry",
                "Jane", "harry2@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());
        User user2 = new User("Missles1", "browns1", "87hioly1", "Bob",
                "Roven", "bob1@gmail.com", new Date(System.currentTimeMillis() + 1000000000), "user",
                "ACTIVE", toSQLDate("13-01-1996"), DifficultyGroup.EXPERT.toString());

        //Save operations
        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);
        Long id1 = savedUser1.getUserID();
        Long id2 = savedUser2.getUserID();
        assertAll(() -> assertEquals("Spons2", savedUser1.getClanName()),
                () -> assertEquals("Roven", savedUser2.getLName()));

        //Read operations
        User readUser1 = userService.get(id1);
        User readUser2 = userService.get(id2);
        assertAll(() -> assertEquals("harick2", readUser1.getLogin()),
                () -> assertEquals("browns1", readUser2.getLogin()));

        //Update operations
        User updUser = userService.get(id1);
        updUser.setRating(15);
        userService.save(updUser);
        assertEquals(15, userService.get(id1).getRating());

        //Delete operations
        userService.delete(id1);
        assertThrows(ServiceBusinessException.class, () -> userService.get(id1));
        assertThrows(Exception.class, () -> userService.delete(1000L));
    }

    @Test
    @Disabled
    @DisplayName("Null tests")
    void test2() {

        assertThrows(ServiceBusinessException.class, () -> userService.get(999L));
        assertThrows(ServiceBusinessException.class, () -> userService.getUserByLogin("lllll"));
        assertThrows(ServiceBusinessException.class, () -> userService.getUsersByClanName("ddddd"));
    }

    @AfterAll
    static void tearDownAll() {
        ((ClassPathXmlApplicationContext) context).close();
    }
}
