package org.art.services.impl;

import org.art.dao.*;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.impl.*;
import org.art.db.ConnectionPoolManager;
import org.art.dto.OrderDTO;
import org.art.entities.*;
import org.art.services.exceptions.*;
import org.art.services.*;

import java.sql.Connection;
import java.util.List;

public class TaskOrderServiceImpl extends TransactionManager implements TaskOrderService {

    private static TaskOrderService INSTANCE;

    private TaskOrderDao orderDao;
    private JavaTaskDao taskDao;
    private UserDao userDao;
    private ConnectionPoolManager connPool;

    private TaskOrderServiceImpl() {
        connPool = ConnectionPoolManager.getInstance();
        threadCache = connPool.getThreadCache();
        orderDao = TaskOrderDaoImpl.getInstance();
        ((TaskOrderDaoImpl) orderDao).setThreadCache(threadCache);
        userDao = UserDaoImpl.getInstance();
        ((UserDaoImpl) userDao).setThreadCache(threadCache);
        taskDao = JavaTaskDaoImpl.getInstance();
        ((JavaTaskDaoImpl) taskDao).setThreadCache(threadCache);
    }

    public static TaskOrderService getInstance() {
        TaskOrderService taskOrderService = INSTANCE;
        if (taskOrderService == null) {
            synchronized (TaskOrderServiceImpl.class) {
                taskOrderService = INSTANCE;
                if (taskOrderService == null) {
                    INSTANCE = taskOrderService = new TaskOrderServiceImpl();
                }
            }
        }
        return taskOrderService;
    }

    @Override
    public TaskOrder save(TaskOrder order) throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            orderDao.save(order);
            endTransaction();
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while saving task order into database!", e);
            throw new ServiceSystemException("Exception while saving task order into database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return order;
    }

    @Override
    public TaskOrder get(long id) throws ServiceSystemException, ServiceBusinessException {
        TaskOrder order;
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            order = orderDao.get(id);
            endTransaction();
            if (order == null) {
                throw new ServiceBusinessException("No order was found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting task order from database!", e);
            throw new ServiceSystemException("Exception while getting task order from database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return order;
    }

    @Override
    public void update(TaskOrder order) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        int updAmount;
        threadCache.set(conn);
        try {
            startTransaction();
            updAmount = orderDao.update(order);
            endTransaction();
            if (updAmount == 0) {
                throw new ServiceBusinessException("No task order with such ID was found!");
            }
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while updating task order in database!", e);
            throw new ServiceSystemException("Exception while updating task order in database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
    }

    @Override
    public int delete(long id) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        int delAmount;
        threadCache.set(conn);
        try {
            startTransaction();
            delAmount = orderDao.delete(id);
            endTransaction();
            if (delAmount == 0) {
                throw new ServiceBusinessException("No task order with such ID was found! ID: " + id);
            }
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while deleting task order from database! ID: " + id, e);
            throw new ServiceSystemException("Exception while deleting task order from database! ID: " + id, e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return delAmount;
    }

    @Override
    public List<OrderDTO> getUserTaskOrders(long id) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        List<OrderDTO> complOrders;
        threadCache.set(conn);
        try {
            startTransaction();
            complOrders = orderDao.getUserTaskOrders(id);
            endTransaction();
            if (complOrders.size() == 0) {
                throw new ServiceBusinessException("No orders were found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting task orders from database! ID: " + id, e);
            throw new ServiceSystemException("Exception while getting task orders from database! ID: " + id, e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return complOrders;
    }

    @Override
    public void createNewOrder(User user, JavaTask task, long execTime) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        TaskOrder order;
        JavaTask newTask;
        try {
            startTransaction();
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
                endTransaction();
                throw new ServiceBusinessException("There is no more tasks for user");
            }
            //Creation of new "NOT SOLVED" task order with a new task
            order = new TaskOrder(user.getUserID(), newTask.getTaskID(), "NOT SOLVED");
            orderDao.save(order);
            endTransaction();
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Cannot update user in the database!", e);
            throw new ServiceSystemException("Cannot update user in the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
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
        newTask = taskDao.getNextTaskByDiffGroup(user.getLevel(), task.getTaskID());
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
            newTask = taskDao.getNextTaskByDiffGroup(user.getLevel(), task.getTaskID());
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

    @Override
    public void createTaskOrderTable() throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            orderDao.createTaskOrderTable();
        } catch (DAOSystemException e) {
            log.info("Exception while creating task orders table in database!", e);
            throw new ServiceSystemException("Exception while creating task orders table in database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
    }

    @Override
    public void deleteTaskOrderTable() throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            orderDao.deleteTaskOrderTable();
        } catch (DAOSystemException e) {
            log.info("Exception while deleting task orders table from database!", e);
            throw new ServiceSystemException("Exception while deleting task orders table from database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
    }

    public void tryRollBackTransaction(Exception e) {
        try {
            backTransaction();
        } catch (DAOSystemException e1) {
            log.info("Exception while rolling back the transaction! This exception was added to suppressed.", e);
            e.addSuppressed(e1);
        }
    }
}
