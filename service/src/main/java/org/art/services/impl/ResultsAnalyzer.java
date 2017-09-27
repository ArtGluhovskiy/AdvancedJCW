package org.art.services.impl;

import org.art.entities.JavaTask;
import org.art.services.impl.StringCompilerService.TaskResults;

/**
 * This class provides results analyzing of solved by user task
 */
public class ResultsAnalyzer {

    /**
     * This method check the correctness of user algorithm result with
     * the task result
     *
     * @param task   java task from the database
     * @param result result of the user code compilation
     * @return true if user solved the task correctly (false - in the opposite case)
     */
    public static boolean analyzeResults(JavaTask task, StringCompilerService.TaskResults result) {
        if (task.getResult() instanceof int[]) {
            if (equalsArrays((int[]) task.getResult(), (int[]) result.getMethodResult()) && task.getElapsedTime() > result.getElapsedTime()) {
                System.out.println("Congratulations!!! The task was successfully completed!");
                System.out.println("Elapsed time of your algorithm: " + result.getElapsedTime() + " mcs");
                return true;
            } else {
                System.out.println("Unfortunately you have a mistake in your code!!!");
                System.out.println(result.getMethodResult());
                return false;
            }
        } else {
            if (task.getResult().equals(result.getMethodResult()) && task.getElapsedTime() > result.getElapsedTime()) {
                System.out.println("Congratulations!!! The task was successfully completed!");
                System.out.println("Elapsed time of your algorithm: " + result.getElapsedTime() + " mcs");
                return true;
            } else {
                System.out.println("Unfortunately you have a mistake in your code!!!");
                System.out.println(result.getMethodResult());
                return false;
            }
        }
    }

    /**
     * Method for arrays comparison by elements
     *
     * @param arr1 the first array to compare
     * @param arr2 the second array to compare
     * @return false if arrays are not equals (true - in the opposite case)
     */
    private static boolean equalsArrays(int[] arr1, int[] arr2) {
        int amount = Math.min(arr1.length, arr2.length);
        for (int i = 0; i < amount; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }
}
