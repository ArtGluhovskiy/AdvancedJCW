package org.art.services;

import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.JavaTask;
import org.art.entities.User;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;

import java.util.List;

public interface JavaTaskService extends Service<JavaTask> {

    /**
     * This service method finds <i>next</i> java task in the database (with
     * appropriate difficulty) with ID going <b>after</b> the ID
     * of the solved task
     *
     * @param user         user with specified level (can be "BEGINNER",
     *                     "EXPERIENCED", "EXPERT")
     * @param solvedTaskID ID of the solved task
     * @return JavaTask with "id" (with appropriate difficulty) going
     * after the task which was solved by the user
     * @throws ServiceBusinessException if no java task was found in the database
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the task reading from the database and its deserialization
     */
    JavaTask getNextTaskByDiffGroup(User user, long solvedTaskID) throws ServiceBusinessException, ServiceSystemException;

    /**
     * This service method read not solved task in accordance with task_orders table
     *
     * @param user user whose not solved task you need to get
     * @return not solved java task
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the task reading from the database and its deserialization
     * @throws ServiceBusinessException if no java task was found in the database
     */
    JavaTask getNotSolvedTask(User user) throws ServiceSystemException, ServiceBusinessException;

    /**
     * Method finds the most popular (with the highest popularity) tasks in the database
     *
     * @param taskAmount amount (from top list) of popular tasks
     * @return the list of the most popular java tasks
     * @throws ServiceBusinessException if no java tasks were found in the database
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the tasks reading from the database and its deserialization
     */
    List<JavaTask> getPopularJavaTasks(int taskAmount) throws ServiceBusinessException, ServiceSystemException;

    /**
     * Get all tasks from the database
     *
     * @return the list of all tasks from database
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the tasks reading from the database and its deserialization
     * @throws ServiceBusinessException if no java tasks were found in the database
     */
    List<JavaTask> getAll() throws ServiceSystemException, ServiceBusinessException;

    /**
     * Creation of the table "java_tasks" in the database
     *
     * @throws ServiceSystemException if {@link DAOSystemException} was thrown during
     *                                the creation of "java_tasks" table in the database
     */
    void createJavaTasksTable() throws ServiceSystemException;

    /**
     * Deleting of the table "java_tasks" from the database
     *
     * @throws ServiceSystemException if {@link DAOSystemException} was thrown during
     *                                the deleting of "java_tasks" table from the database
     */
    void deleteJavaTasksTable() throws ServiceSystemException;
}
