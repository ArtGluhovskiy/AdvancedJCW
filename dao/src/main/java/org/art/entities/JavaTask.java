package org.art.entities;

import lombok.Data;

import java.io.*;
import java.sql.Date;
import java.util.Arrays;

/**
 * This is an implementation of a java task for application. This class contains
 * all information needed for user, class loader, method invocation etc.
 */
@Data
public class JavaTask implements Serializable {

    private static final long SerialVersionUID = 1L;

    // Transient fields should be explicitly defined in the java_tasks table of the database
    private transient long taskID; //auto increment field
    private transient int popularity;
    private transient String difficultyGroup = DifficultyGroup.EXPERT.toString();
    private transient String shortDescr = "Advanced array sorting";
    private transient Date regDate = new Date(System.currentTimeMillis());
    // Efficient time of the algorithm (in mcs)
    private long elapsedTime = 100000;

    // Task description and test info for user
    private String description = "Congratulations! You have reached the highest level.<br>" +
            "So, it's time to try hard! Enough school tasks!<br>" +
            "This time you need to write an algorithm of array sorting...<br>" +
            "The task seems too easy at first sight.<br>" +
            "But! You need to write more efficient algorithm than simple bubble sort." +
            "Forget it! And all that O(n2) staff! You should use O(n logn) algorithms if " +
            "you want to complete this task successfully! I recommend you quick or merge sort!";

    private String topics = "Java Core, Sorting algorithms, Arrays";

    private String testInfo = "import org.junit.Test;<br>" +
            "import static org.junit.Assert.*;<br>" +
            "<br>" +
            "public class FactorialTest {<br>" +
            "&#8195;&#8195;@Test<br>" +
            "&#8195;&#8195;public void factorial() throws Exception {<br>" +
            "&#8195;&#8195;&#8195;&#8195;AdvancedArraySorting sorter = new AdvancedArraySorting();<br>" +
            "&#8195;&#8195;&#8195;&#8195;assertEquals(new int[-32, 2, 5, 32, 90], sorter.sort(new int[2, 32, 5, -32, 90]));<br>" +
            "&#8195;&#8195;}<br>" +
            "}";

    // Value of the task
    private int value = 10;

    // Parameters for class loader and method invocation
    private int parametersNumber = 1;
    private Object result;
    private String className = "AdvancedArraySorting";
    private String methodName = "sort";
    private String methodString = "public int[] sort(int[] array) {";
    private Object[] testMethodParameters;
    private Class[] methodParametersType = new Class[]{int[].class};

    public JavaTask() {
        int[] array = new int[100_000];
        int k = 0;
        for (int i = array.length; i > 0; i--) {
            array[k] = i + 2;
            k++;
        }
        this.testMethodParameters = new Object[] {array};
        int[] arrayCopy = Arrays.copyOf(array, array.length);
        Arrays.sort(arrayCopy);
        this.result = arrayCopy;
    }

    @Override
    public String toString() {
        return new StringBuilder("*** JavaTask info:\n")
                .append("* task id: " + taskID + ";\n")
                .append("* popularity: " + popularity + ";\n")
                .append("* task value: " + value + ";\n")
                .append("* task difficulty: " + difficultyGroup + ";\n")
                .append("* short task description:\n \"" + shortDescr + "\"\n")
                .append("* full description of the task:\n \"" + description + "\"\n")
                .append("* test information:\n" + testInfo + "\n")
                .append("* elapsed time: " + elapsedTime + ";\n")
                .append("* registration date of the task: \"" + regDate + "\";\n")
                .append("* class name: \"" + className + "\";\n")
                .append("* name of the target method: \"" + methodName + "\";\n")
                .append("* number of the method parameters: " + parametersNumber + ";\n")
                .append("* method parameters for testing: " + Arrays.toString(testMethodParameters) + ";\n")
                .append("* types of method parameters: " + Arrays.toString(methodParametersType) + ";\n")
                .append("* required method result: " + result + ".\n")
                .append("***")
                .toString();
    }
}
