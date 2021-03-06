package org.art.dao;

import org.art.dao.exceptions.DAOSystemException;
import org.art.dto.OrderDTO;
import org.art.entities.TaskOrder;
import org.art.entities.User;

import java.util.List;

/**
 * TaskOrderDao interface with special methods.
 */
public interface TaskOrderDao extends DAO<TaskOrder> {

    /**
     * Returns the list of the orders {@link OrderDTO} with the tasks solved by user
     *
     * @param id user ID
     * @return the list of the tasks solved by user
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the task orders reading from the database
     */
    List<OrderDTO> getUserSolvedTaskOrders(Long id) throws DAOSystemException;

    /**
     * Returns the list of all user's orders {@link OrderDTO}
     *
     * @param id user ID
     * @return the list of the task order
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the task orders reading from the database
     */
    List<OrderDTO> getAllUserSolvedTaskOrders(Long id) throws DAOSystemException;

    /**
     * Finds the order with the task which has not already solved in
     * the "task_orders" table
     *
     * @param user the user whose order you need to find
     * @return task order with the task which has not already solved
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the task order reading from the database
     */
    TaskOrder getNotSolvedOrder(User user) throws DAOSystemException;

    /**
     * Finds all user's task orders
     *
     * @param user the user whose order you need to find
     * @return task order with the task which has not already solved
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the task order reading from the database
     */
    List<TaskOrder> getOrders(User user) throws DAOSystemException;
}
