package org.art.services.impl;

import org.art.entities.DifficultyGroup;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.art.services.JavaTaskService;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestOrderServiceImpl {

    static ApplicationContext context;
    static TaskOrderService orderService;
    static UserService userService;
    static JavaTaskService taskService;

    @BeforeAll
    static void initAll() throws SQLException {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
        orderService = context.getBean("taskOrderServiceImpl", TaskOrderService.class);
        assertNotNull(orderService);
        userService  = context.getBean("userServiceImpl", UserService.class);
        assertNotNull(userService);
        taskService = context.getBean("javaTaskServiceImpl", JavaTaskService.class);
        assertNotNull(taskService);
    }

    @Test
    @DisplayName("Create new order complex test")
    void test1() throws ServiceSystemException, ServiceBusinessException {

        User user = new User("Spoons", "haricks", "87jhkry12", "Harry",
                "Jane", "harryk2@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.EXPERT.toString());

        int initTaskAmount;
        try {
            initTaskAmount = taskService.getAll().size();
        } catch (Exception e) {
            //If no tasks were found
            initTaskAmount = 0;
        }

        JavaTask task1 = new JavaTask(10);
        JavaTask task2 = new JavaTask(15);
        JavaTask task3 = new JavaTask(20);
        JavaTask task4 = new JavaTask(23);
        task4.setDifficultyGroup(DifficultyGroup.EXPERT.toString());
        taskService.save(task1);
        taskService.save(task2);
        taskService.save(task3);
        taskService.save(task4);
        TaskOrder order1 = new TaskOrder("SOLVED", user, task1);
        TaskOrder order2 = new TaskOrder("SOLVED", user, task2);
        TaskOrder order3 = new TaskOrder("NOT SOLVED", user, task3);
        user.getOrders().add(order1);
        user.getOrders().add(order2);
        user.getOrders().add(order3);

        int updTaskAmount = taskService.getAll().size();
        assertEquals(initTaskAmount + 4, updTaskAmount);

        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        List<TaskOrder> orders = orderService.getOrders(user);
        assertEquals(3, orders.size());

        //CreateNewOrder method testing
        orderService.createNewOrder(user, task3, 10000);
        List<TaskOrder> newOrders = orderService.getOrders(user);

        assertAll(() -> assertNotNull(newOrders),
                () -> assertEquals(newOrders.size(), 4),
                () -> assertEquals(newOrders.get(0).getStatus(), "SOLVED"),
                () -> assertEquals(newOrders.get(1).getStatus(), "SOLVED"),
                () -> assertEquals(newOrders.get(2).getStatus(), "SOLVED"),
                () -> assertEquals(newOrders.get(3).getStatus(), "NOT SOLVED"));
    }
}
