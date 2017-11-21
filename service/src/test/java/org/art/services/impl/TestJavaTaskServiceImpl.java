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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJavaTaskServiceImpl {

    static ApplicationContext context;
    static JavaTaskService taskService;
    static UserService userService;
    static TaskOrderService orderService;

    @BeforeAll
    static void initAll() throws SQLException {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
        taskService = context.getBean("javaTaskServiceImpl", JavaTaskService.class);
        userService = context.getBean("userServiceImpl", UserService.class);
        orderService = context.getBean("taskOrderServiceImpl", TaskOrderService.class);
    }

    @Test
    @DisplayName("Java task solution test")
    void test1() throws ServiceSystemException, ServiceBusinessException {
        StringCompilerService stringCompiler = new StringCompilerService(false);
        // TODO: get task from DB or create new?
        JavaTask task = new JavaTask();
        //Save task to DB
        task = taskService.save(task);
        Long taskId = task.getTaskId();
        //Read task from DB
        task = taskService.get(taskId);

        //User solution of the task
        String userSolution = "public class AdvancedArraySorting {\n" +
                "    private int[] numbers;\n" +
                "    private int number;\n" +
                "    public int[] sort(int[] array) {\n" +
                "        this.numbers = array;\n" +
                "        this.number = array.length;\n" +
                "        quickSort(0, number - 1);\n" +
                "        return array;\n" +
                "    }\n" +
                "    private void quickSort(int low, int high) {\n" +
                "        int i = low, j = high;\n" +
                "        int pivot = numbers[low + (high-low)/2];\n" +
                "        while (i <= j) {\n" +
                "            while (numbers[i] < pivot) {\n" +
                "                i++;\n" +
                "            }\n" +
                "            while (numbers[j] > pivot) {\n" +
                "                j--;\n" +
                "            }\n" +
                "            if (i <= j) {\n" +
                "                exchange(i, j);\n" +
                "                i++;\n" +
                "                j--;\n" +
                "            }\n" +
                "        }\n" +
                "        if (low < j)\n" +
                "            quickSort(low, j);\n" +
                "        if (i < high)\n" +
                "            quickSort(i, high);\n" +
                "    }\n" +
                "    private void exchange(int i, int j) {\n" +
                "        int temp = numbers[i];\n" +
                "        numbers[i] = numbers[j];\n" +
                "        numbers[j] = temp;\n" +
                "    }\n" +
                "}";

        //Compilation of the task
        StringCompilerService.TaskResults results = stringCompiler.compileTask(task, userSolution);
        ResultsAnalyzer.analyzeResults(task, results);

        //Results analyzing
        assertArrayEquals((int[]) task.getResult(), (int[]) results.getMethodResult());
    }

    @Test
    @DisplayName("Get next task by difficulty group test")
    void test2() throws ServiceSystemException, ServiceBusinessException {

        User user = new User("Sponss2", "haricks2", "87jhy1s2", "Harry",
                "Jane", "harrys2@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("21-05-1993"), DifficultyGroup.EXPERIENCED.toString());
        userService.save(user);
        JavaTask task1 = new JavaTask();
        JavaTask task2 = new JavaTask();
        JavaTask task3 = new JavaTask();
        task1.setDifficultyGroup(DifficultyGroup.EXPERIENCED.toString());
        task2.setDifficultyGroup(DifficultyGroup.EXPERT.toString());
        task3.setDifficultyGroup(DifficultyGroup.EXPERIENCED.toString());
        task3.setShortDescr("Next task");
        taskService.save(task1);
        taskService.save(task2);
        taskService.save(task3);
        Long taskId = task1.getTaskId();

        TaskOrder order1 = new TaskOrder("SOLVED", user, task1);
        TaskOrder order2 = new TaskOrder("SOLVED", user, task2);
        orderService.save(order1);
        orderService.save(order2);

        int initUserOrdersAmount = orderService.getOrders(user).size();
        JavaTask newTask = taskService.getNextTaskByDiffGroup(user, taskId);
        //New user task order with "NOT SOLVED" status must be created
        List<TaskOrder> userOrders = orderService.getOrders(user);
        int updatedUserOrdersAmount = userOrders.size();

        assertEquals("Next task", newTask.getShortDescr());
        assertEquals(initUserOrdersAmount + 1, updatedUserOrdersAmount);
        assertEquals("NOT SOLVED", userOrders.get(updatedUserOrdersAmount - 1).getStatus());
    }

    @AfterAll
    static void tearDownAll() throws SQLException {

    }
}
