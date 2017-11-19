package org.art.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.JavaTaskDao;
import org.art.dao.TaskOrderDao;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dto.OrderDTO;
import org.art.entities.DifficultyGroup;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.art.services.TaskOrderService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskOrderServiceImpl implements TaskOrderService {

    private static final Logger log = LogManager.getLogger(TaskOrderServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private TaskOrderDao orderDao;

    @Autowired
    private JavaTaskDao taskDao;

    @Override
    public TaskOrder save(TaskOrder order) throws ServiceSystemException {
        TaskOrder savedOrder;
        try {
            savedOrder = orderDao.save(order);
        } catch (DAOSystemException e) {
            log.info("Exception while saving task order into the database!", e);
            throw new ServiceSystemException("Exception while saving task order into the database!", e);
        }
        return savedOrder;
    }

    @Override
    public TaskOrder get(Long id) throws ServiceSystemException, ServiceBusinessException {
        TaskOrder order;
        try {
            order = orderDao.get(id);
            if (order == null) {
                throw new ServiceBusinessException("No order was found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting task order from the database!", e);
            throw new ServiceSystemException("Exception while getting task order from the database!", e);
        }
        return order;
    }

    @Override
    public TaskOrder update(TaskOrder order) throws ServiceSystemException, ServiceBusinessException {
        TaskOrder updOrder;
        try {
            updOrder = orderDao.update(order);
            if (updOrder == null) {
                throw new ServiceBusinessException("No task order with such ID was found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while updating task order in the database!", e);
            throw new ServiceSystemException("Exception while updating task order in the database!", e);
        }
        return updOrder;
    }

    @Override
    public void delete(Long id) throws ServiceSystemException, ServiceBusinessException {
        try {
            orderDao.delete(id);
        } catch (DAOSystemException e) {
            log.info("Exception while deleting task order from the database! ID: " + id, e);
            throw new ServiceSystemException("Exception while deleting task order from the database! ID: " + id, e);
        }
    }

    @Override
    public List<OrderDTO> getUserSolvedTaskOrders(Long id) throws ServiceSystemException, ServiceBusinessException {
        List<OrderDTO> complOrders;
        try {
            complOrders = orderDao.getUserSolvedTaskOrders(id);
            if (complOrders.size() == 0) {
                throw new ServiceBusinessException("No orders were found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting task orders from the database! ID: " + id, e);
            throw new ServiceSystemException("Exception while getting task orders from the database! ID: " + id, e);
        }
        return complOrders;
    }

    @Override
    public void createNewOrder(User user, JavaTask task, long execTime) throws ServiceSystemException, ServiceBusinessException {
        TaskOrder order;
        JavaTask newTask;
        try {
            //User rating modifying
            user.setRating(user.getRating() + task.getValue());
            userDao.update(user);
            //Updating task (for solved task) popularity by 1
            task.setPopularity(task.getPopularity() + 1);
            taskDao.update(task);
            //Updating task order
            order = orderDao.getNotSolvedOrder(user);
            order.setStatus("SOLVED");
            order.setExecTime(execTime);
            orderDao.update(order);
            //Getting new task
            newTask = getNewTask(user, task);
            if (newTask == null) {
                //If there are no more tasks for EXPERT user
                throw new ServiceBusinessException("There is no more tasks for user");
            }
            //Creation of new "NOT SOLVED" task order with a new task
            order = new TaskOrder("NOT SOLVED", user, task);
            orderDao.save(order);
        } catch (DAOSystemException e) {
            log.info("Cannot update user in the database!", e);
            throw new ServiceSystemException("Cannot update user in the database!", e);
        }
    }

    @Override
    public List<TaskOrder> getOrders(User user) throws ServiceSystemException, ServiceBusinessException {
        List<TaskOrder> orders;
        try {
            orders = orderDao.getOrders(user);
            if (orders.size() == 0) {
                throw new ServiceBusinessException("No orders were found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting task orders from the database! ID: " + user.getUserID(), e);
            throw new ServiceSystemException("Exception while getting task orders from the database! ID: " + user.getUserID(), e);
        }
        return orders;
    }

    /**
     * This service method reads new task from the database
     * and increases the user's level if no tasks with user's
     * level were found
     *
     * @param user user for whom you need to find a new task
     * @param task solved task
     * @return a new java task with appropriate difficulty
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the operations with the database
     * @throws ServiceBusinessException if no java task was found in the database
     */
    private JavaTask getNewTask(User user, JavaTask task) throws DAOSystemException, ServiceBusinessException {
        JavaTask newTask;
        newTask = taskDao.getNextTaskByDiffGroup(user.getLevel(), task.getTaskId());
        if (newTask == null) {
            //There is no more tasks with such difficulty in the database
            //So, we change the user level to a higher one
            user = togleUserLevel(user);
            //Method returns null if user already has EXPERT level
            if (user == null) {
                return newTask; // == null
            }
            //Updating user with a new level in database
            userDao.update(user);
            newTask = taskDao.getNextTaskByDiffGroup(user.getLevel(), task.getTaskId());
        }
        return newTask;
    }

    /**
     * This method is called when there is no more tasks for user with <b>his</b> level
     *
     * @param user
     * @return user with updated level
     */
    private User togleUserLevel(User user) {
        if (user.getLevel().equals(DifficultyGroup.BEGINNER.toString())) {
            user.setLevel(DifficultyGroup.EXPERIENCED.toString());
            return user;
        } else if (user.getLevel().equals(DifficultyGroup.EXPERIENCED.toString())) {
            user.setLevel(DifficultyGroup.EXPERT.toString());
            return user;
        } else {
            return null;
        }
    }
}
