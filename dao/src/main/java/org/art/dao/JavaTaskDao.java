package org.art.dao;

import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.JavaTask;

import java.util.List;

/**
 * JavaTaskDao interface with special methods.
 */
public interface JavaTaskDao extends DAO<JavaTask> {

    /**
     * Finds <i>next</i> java task in the database (with
     * appropriate difficulty) with ID going <b>after</b> the ID
     * of the solved task
     *
     * @param difGroup group of difficulty of the task (can be "BEGINNER",
     *                 "EXPERIENCED", "EXPERT")
     * @param taskID ID of the solved task
     * @return JavaTask with "id" (with appropriate difficulty) going
     * after the task which was solved by the user
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the task reading from the database
     */
    JavaTask getNextTaskByDiffGroup(String difGroup, Long taskID) throws DAOSystemException;

    /**
     * This method finds the most popular (with the highest popularity) tasks in the database
     *
     * @param taskAmount amount (from top list) of popular tasks
     * @return the list of the most popular java tasks
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the tasks reading from the database
     */
    List<JavaTask> getPopularJavaTasks(int taskAmount) throws DAOSystemException;

    /**
     * Increases task popularity by one
     *
     * @param task java task which popularity should be increased
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the tasks reading from the database
     */
    void increaseTaskPopularity(JavaTask task) throws DAOSystemException;

    /**
     * Gets all tasks from the database
     *
     * @return the list of all tasks from database
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the tasks reading from the database
     */
    List<JavaTask> getAll() throws DAOSystemException;
}
