package org.art.services;

import org.art.dao.exceptions.DAOSystemException;
import org.art.dto.OrderDTO;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;

import java.util.List;

public interface TaskOrderService extends Service<TaskOrder> {

    /**
     * This service method returns the list of the orders {@link OrderDTO}
     * with the tasks solved by user
     *
     * @param id user ID
     * @return the list of the tasks solved by user
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the task orders reading from the database
     * @throws ServiceBusinessException if no java task orders were found in the database
     */
    List<OrderDTO> getUserSolvedTaskOrders(Long id) throws ServiceSystemException, ServiceBusinessException;

    /**
     * This service method returns the list of all user's orders {@link OrderDTO}
     *
     * @param id user ID
     * @return the list of the task orders
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the task orders reading from the database
     * @throws ServiceBusinessException if no java task orders were found in the database
     */
    List<OrderDTO> getAllUserSolvedTaskOrders(Long id) throws ServiceSystemException, ServiceBusinessException;

    /**
     * Service method for creation of task order with a new task
     * with appropriate difficulty group. This method also provides
     * modifying of old task order (modifying of task order status,
     * algorithm execution time)
     *
     * @param user     user for whom you need to create a new task order
     * @param task     solved task
     * @param execTime algorithm execution time of solved task
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the creation of new task order in the database
     * @throws ServiceBusinessException if there are no more tasks for user
     */
    void createNewOrder(User user, JavaTask task, long execTime) throws ServiceSystemException, ServiceBusinessException;

    /**
     * Finds all user's task orders
     *
     * @param user the user whose order you need to find
     * @return task order with the task which has not already solved
     * @throws ServiceSystemException   if {@link DAOSystemException} was thrown during
     *                                  the creation of new task order in the database
     * @throws ServiceBusinessException if java task order was not found in the database
     */
    List<TaskOrder> getOrders(User user) throws ServiceSystemException, ServiceBusinessException;
}
