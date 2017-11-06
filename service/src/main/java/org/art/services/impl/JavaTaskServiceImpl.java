package org.art.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.JavaTaskDao;
import org.art.dao.TaskOrderDao;
import org.art.dao.UserDao;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;

import java.util.List;

public class JavaTaskServiceImpl implements JavaTaskService {

    private static final Logger log = LogManager.getLogger(JavaTaskServiceImpl.class);

    private JavaTaskDao javaTaskDao;
    private TaskOrderDao orderDao;
    private UserDao userDao;

    @Override
    public JavaTask save(JavaTask javaTask) throws ServiceSystemException {
//        try {
//
//        } catch (DAOSystemException e) {
//            log.info("Exception while saving task into database!", e);
//            throw new ServiceSystemException("Exception while saving task into database!", e);
//        }
        return javaTask;
    }

    @Override
    public JavaTask get(long id) throws ServiceSystemException, ServiceBusinessException {
        JavaTask javaTask = null;
//        try {
//            if (javaTask == null) {
//                throw new ServiceBusinessException("No task with such ID was found!");
//            }
//        } catch (DAOSystemException e) {
//            log.info("Exception while getting task from database! Task ID: " + id, e);
//            throw new ServiceSystemException("Exception while getting task from database! Task ID: " + id, e);
//        }
        return javaTask;
    }

    @Override
    public void update(JavaTask javaTask) throws ServiceSystemException, ServiceBusinessException {
//        try {
//            if (updRows == 0) {
//                throw new ServiceBusinessException("No task with such ID was found!");
//            }
//        } catch (DAOSystemException e) {
//            tryRollBackTransaction(e);
//            log.info("Exception while updating task in database!", e);
//            throw new ServiceSystemException("Exception while updating task in database!", e);
//        }
    }

    @Override
    public void delete(long id) throws ServiceSystemException, ServiceBusinessException {
//        int delRows;
//        try {
//            if (delRows == 0) {
//                throw new ServiceBusinessException("No task with such ID was found!");
//            }
//        } catch (DAOSystemException e) {
//            log.info("Exception while deleting task from database!", e);
//            throw new ServiceSystemException("Exception while deleting task from database!", e);
//        }
//        return delRows;
    }

    @Override
    public JavaTask getNextTaskByDiffGroup(User user, long solvedTaskID) throws ServiceBusinessException, ServiceSystemException {
        JavaTask newTask = null;
        TaskOrder order;
//        try {
//            startTransaction();
//            //Getting next task in database
//            newTask = javaTaskDao.getNextTaskByDiffGroup(user.getLevel(), solvedTaskID);
//            //After registration user rating increases by 1
//            if (solvedTaskID == 0) {
//
//            }
//            //Creation of new "NOT SOLVED" task order with new task
//            order = new TaskOrder(user.getUserID(), newTask.getTaskID(), "NOT SOLVED");
//            TaskOrder order1 = orderDao.save(order);
//            endTransaction();
//            if (newTask == null) {
//                throw new ServiceBusinessException("No task with such ID was found!");
//            }
//        } catch (DAOSystemException e) {
//            tryRollBackTransaction(e);
//            log.info("Exception while getting task from database!", e);
//            throw new ServiceSystemException("Exception while getting task from database!", e);
//        } finally {
//            ConnectionPoolManager.close(conn);
//        }
        return newTask;
    }

    @Override
    public JavaTask getNotSolvedTask(User user) throws ServiceSystemException, ServiceBusinessException {
//        Connection conn = connPool.getConnection();
//        threadCache.set(conn);
        JavaTask javaTask = null;
//        TaskOrder order;
//        try {
//            startTransaction();
//            order = orderDao.getNotSolvedOrder(user);
//            if (order == null) {
//                throw new ServiceBusinessException("No order with not solved task was found!");
//            }
//            javaTask = javaTaskDao.get(order.getTaskID());
//            endTransaction();
//            if (javaTask == null) {
//                throw new ServiceBusinessException("No task with such ID was found (getNotSolvedTask method)!");
//            }
//        } catch (DAOSystemException e) {
//            log.info("Exception while getting not solved task from database!", e);
//            throw new ServiceSystemException("Exception while getting not solved task from database!", e);
//        } finally {
//            ConnectionPoolManager.close(conn);
//        }
        return javaTask;
    }

    @Override
    public List<JavaTask> getPopularJavaTasks(int taskAmount) throws ServiceBusinessException, ServiceSystemException {
//        Connection conn = connPool.getConnection();
//        threadCache.set(conn);
        List<JavaTask> taskList = null;
//        try {
//            startTransaction();
//            taskList = javaTaskDao.getPopularJavaTasks(taskAmount);
//            endTransaction();
//            if (taskList.size() == 0) {
//                throw new ServiceBusinessException("No tasks were found!");
//            }
//        } catch (DAOSystemException e) {
//            log.info("Exception while getting popular tasks from database!", e);
//            throw new ServiceSystemException("Exception while getting popular tasks from database!", e);
//        } finally {
//            ConnectionPoolManager.close(conn);
//        }
        return taskList;
    }

    @Override
    public List<JavaTask> getAll() throws ServiceSystemException, ServiceBusinessException {
//        Connection conn = connPool.getConnection();
//        threadCache.set(conn);
        List<JavaTask> taskList = null;
//        try {
//            startTransaction();
//            taskList = javaTaskDao.getAll();
//            endTransaction();
//            if (taskList.size() == 0) {
//                throw new ServiceBusinessException("No tasks were found!");
//            }
//        } catch (DAOSystemException e) {
//            log.info("Exception while getting all tasks from database!", e);
//            throw new ServiceSystemException("Exception while getting all tasks from database!", e);
//        } finally {
//            ConnectionPoolManager.close(conn);
//        }
        return taskList;
    }

//    @Override
//    public void createJavaTasksTable() throws ServiceSystemException {
//        Connection conn = connPool.getConnection();
//        threadCache.set(conn);
//        try {
//            javaTaskDao.createJavaTasksTable();
//        } catch (DAOSystemException e) {
//            log.info("Exception while creating tasks table in database!", e);
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
//            log.info("Exception while deleting tasks table in database!", e);
//            throw new ServiceSystemException("Exception while deleting tasks table in database!", e);
//        } finally {
//            ConnectionPoolManager.close(conn);
//        }
//    }
//
//    public void tryRollBackTransaction(Exception e) {
//        try {
//            backTransaction();
//        } catch (DAOSystemException e1) {
//            log.info("Exception while rolling back the transaction! This exception was added to suppressed.", e);
//            e.addSuppressed(e1);
//        }
//    }
}
