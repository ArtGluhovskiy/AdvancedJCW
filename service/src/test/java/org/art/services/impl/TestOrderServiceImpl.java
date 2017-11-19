package org.art.services.impl;

import org.art.entities.DifficultyGroup;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.art.services.TaskOrderService;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestOrderServiceImpl {

    static ApplicationContext context;

    @BeforeAll
    static void initAll() throws SQLException {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
    }

    @Test
    @DisplayName("Create new order test")
    void test1() throws ServiceSystemException, ServiceBusinessException {
        TaskOrderService orderService = context.getBean("taskOrderServiceImpl", TaskOrderService.class);
        assertNotNull(orderService);
        UserService userService  = context.getBean("userServiceImpl", UserService.class);
        assertNotNull(userService);

        User user = new User("Spoons", "haricks", "87jhkry12", "Harry",
                "Jane", "harryk2@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.BEGINNER.toString());
        JavaTask task1 = new JavaTask(10);
        JavaTask task2 = new JavaTask(15);
        JavaTask task3 = new JavaTask(20);
        TaskOrder order1 = new TaskOrder("SOLVED", user, task1);
        TaskOrder order2 = new TaskOrder("SOLVED", user, task2);
        TaskOrder order3 = new TaskOrder("NOT SOLVED", user, task3);
        user.getOrders().add(order1);
        user.getOrders().add(order2);
        user.getOrders().add(order3);

        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        List<TaskOrder> orders = orderService.getOrders(user);
        assertEquals(3, orders.size());

        //CreateNewOrder method testing
        orderService.createNewOrder(user, task3, 10000);



    }
}
