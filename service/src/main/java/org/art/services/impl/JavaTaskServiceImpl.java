package org.art.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.JavaTaskDao;
import org.art.dao.TaskOrderDao;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(/*propagation = Propagation.REQUIRED*/)
public class JavaTaskServiceImpl implements JavaTaskService {

    private static final Logger LOG = LogManager.getLogger(JavaTaskServiceImpl.class);

    private final JavaTaskDao javaTaskDao;

    private final TaskOrderDao orderDao;

    private final UserDao userDao;

    @Autowired
    public JavaTaskServiceImpl(JavaTaskDao javaTaskDao, TaskOrderDao orderDao, UserDao userDao) {
        this.javaTaskDao = javaTaskDao;
        this.orderDao = orderDao;
        this.userDao = userDao;
    }

    @Override
    public JavaTask save(JavaTask javaTask) throws ServiceSystemException {
        JavaTask savedTask;
        try {
            savedTask = javaTaskDao.save(javaTask);
        } catch (DAOSystemException e) {
            LOG.info("Exception while saving task into the database!", e);
            throw new ServiceSystemException("Exception while saving task into the database!", e);
        }
        return savedTask;
    }

    @Override
    public JavaTask get(Long id) throws ServiceSystemException, ServiceBusinessException {
        JavaTask javaTask;
        try {
            javaTask = javaTaskDao.get(id);
            if (javaTask == null) {
                throw new ServiceBusinessException("No task with such ID was found!");
            }
        } catch (DAOSystemException e) {
            LOG.info("Exception while getting task from the database! Task ID: " + id, e);
            throw new ServiceSystemException("Exception while getting task from the database! Task ID: " + id, e);
        }
        return javaTask;
    }

    @Override
    public JavaTask update(JavaTask javaTask) throws ServiceSystemException, ServiceBusinessException {
        JavaTask updTask;
        try {
            updTask = javaTaskDao.update(javaTask);
            if (updTask == null) {
                throw new ServiceBusinessException("Java task updating failed!");
            }
        } catch (DAOSystemException e) {
            LOG.info("Exception while updating task in the database!", e);
            throw new ServiceSystemException("Exception while updating task in the database!", e);
        }
        return updTask;
    }

    @Override
    public void delete(Long id) throws ServiceSystemException, ServiceBusinessException {
        try {
            javaTaskDao.delete(id);
        } catch (DAOSystemException e) {
            LOG.info("Exception while deleting task from database!", e);
            throw new ServiceSystemException("Exception while deleting task from database!", e);
        }
    }

    @Override
    public JavaTask getNextTaskByDiffGroup(User user, long solvedTaskID) throws ServiceBusinessException, ServiceSystemException {
        JavaTask newTask;
        TaskOrder order;
        try {
            //Get the next task with appropriate difficulty group from the database
            newTask = javaTaskDao.getNextTaskByDiffGroup(user.getLevel(), solvedTaskID);
            if (newTask == null) {
                throw new ServiceBusinessException("No more tasks with such difficulty group were found!");
            }
            //Creation of new "NOT SOLVED" task order with new task
            order = new TaskOrder("NOT SOLVED", user, newTask);
            orderDao.save(order);
        } catch (DAOSystemException e) {
            LOG.info("Exception while getting task from database!", e);
            throw new ServiceSystemException("Exception while getting task from database!", e);
        }
        return newTask;
    }

    @Override
    public JavaTask getNotSolvedTask(User user) throws ServiceSystemException, ServiceBusinessException {
        JavaTask javaTask;
        TaskOrder order;
        try {
            order = orderDao.getNotSolvedOrder(user);
            if (order == null) {
                throw new ServiceBusinessException("No order with not solved task was found!");
            }
            javaTask = javaTaskDao.get(order.getJavaTask().getTaskId());
            if (javaTask == null) {
                throw new ServiceBusinessException("No task with such ID was found (getNotSolvedTask method)!");
            }
        } catch (DAOSystemException e) {
            LOG.info("Exception while getting not solved task from the database!", e);
            throw new ServiceSystemException("Exception while getting not solved task from the database!", e);
        }
        return javaTask;
    }

    @Override
    public List<JavaTask> getPopularJavaTasks(int taskAmount) throws ServiceBusinessException, ServiceSystemException {
        List<JavaTask> taskList;
        try {
            taskList = javaTaskDao.getPopularJavaTasks(taskAmount);
            if (taskList.size() == 0) {
                throw new ServiceBusinessException("No tasks were found!");
            }
        } catch (DAOSystemException e) {
            LOG.info("Exception while getting popular tasks from the database!", e);
            throw new ServiceSystemException("Exception while getting popular tasks from the database!", e);
        }
        return taskList;
    }

    @Override
    public void increaseTaskPopularity(JavaTask task) throws ServiceBusinessException {
        try {
            javaTaskDao.increaseTaskPopularity(task);
        } catch (DAOSystemException e) {
            LOG.info("Cannot update task popularity in the database!", e);
            throw new ServiceBusinessException("Cannot update task popularity in the database!", e);
        }
    }

    @Override
    public List<JavaTask> getAll() throws ServiceSystemException, ServiceBusinessException {
        List<JavaTask> taskList;
        try {
            taskList = javaTaskDao.getAll();
            if (taskList.size() == 0) {
                throw new ServiceBusinessException("No tasks were found!");
            }
        } catch (DAOSystemException e) {
            LOG.info("Exception while getting all tasks from the database!", e);
            throw new ServiceSystemException("Exception while getting all tasks from the database!", e);
        }
        return taskList;
    }

//    @Override
//    public void createJavaTasksTable() throws ServiceSystemException {
//        Connection conn = connPool.getConnection();
//        threadCache.set(conn);
//        try {
//            javaTaskDao.createJavaTasksTable();
//        } catch (DAOSystemException e) {
//            LOG.info("Exception while creating tasks table in database!", e);
//            throw new ServiceSystemException("Exception while creating tasks table in database!", e);
//        } finally {
//            ConnectionPoolManager.close(conn);
//        }
//    }
//
//    @Override
//    public void deleteJavaTasksTable() throws ServiceSystemException {
//        Connection conn = connPool.getConnection();
//        threadCache.set(conn);
//        try {
//            javaTaskDao.deleteJavaTasksTable();
//        } catch (DAOSystemException e) {
//            LOG.info("Exception while deleting tasks table in database!", e);
//            throw new ServiceSystemException("Exception while deleting tasks table in database!", e);
//        } finally {
//            ConnectionPoolManager.close(conn);
//        }
//    }

}
