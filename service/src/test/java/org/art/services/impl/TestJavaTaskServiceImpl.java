package org.art.services.impl;

import org.art.entities.JavaTask;
import org.art.services.JavaTaskService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestJavaTaskServiceImpl {

    static ApplicationContext context;

    @BeforeAll
    static void initAll() throws SQLException {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
    }

    @Test
    @DisplayName("Java task solution test")
    void test1() throws ServiceSystemException, ServiceBusinessException {
        StringCompilerService stringCompiler = new StringCompilerService(false);
        JavaTaskService taskService = context.getBean("javaTaskServiceImpl", JavaTaskService.class);
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

    @AfterAll
    static void tearDownAll() throws SQLException {

    }
}
