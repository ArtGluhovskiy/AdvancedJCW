package org.art.services.impl;

import org.art.dao.JavaTaskDao;
import org.art.dao.TaskOrderDao;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.impl.JavaTaskDaoImpl;
import org.art.dao.impl.TaskOrderDaoImpl;
import org.art.dao.impl.UserDaoImpl;
import org.art.db.ConnectionPoolManager;
import org.art.entities.JavaTask;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.TransactionManager;
import org.art.services.exceptions.ServiceSystemException;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JavaTaskServiceImpl extends TransactionManager implements JavaTaskService {

    private static JavaTaskService INSTANCE;

    private JavaTaskDao javaTaskDao;
    private TaskOrderDao orderDao;
    private UserDao userDao;
    private ConnectionPoolManager connPool;

    private JavaTaskServiceImpl() {
        connPool = ConnectionPoolManager.getInstance();
        threadCache = connPool.getThreadCache();
        javaTaskDao = JavaTaskDaoImpl.getInstance();
        ((JavaTaskDaoImpl) javaTaskDao).setThreadCache(threadCache);
        orderDao = TaskOrderDaoImpl.getInstance();
        ((TaskOrderDaoImpl) orderDao).setThreadCache(threadCache);
        userDao = UserDaoImpl.getInstance();
        ((UserDaoImpl) userDao).setThreadCache(threadCache);
    }

    public static JavaTaskService getInstance() {
        JavaTaskService javaTaskService = INSTANCE;
        if (javaTaskService == null) {
            synchronized (JavaTaskServiceImpl.class) {
                javaTaskService = INSTANCE;
                if (javaTaskService == null) {
                    INSTANCE = javaTaskService = new JavaTaskServiceImpl();
                }
            }
        }
        return javaTaskService;
    }

    @Override
    public JavaTask save(JavaTask javaTask) throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            javaTaskDao.save(javaTask);
            endTransaction();
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while saving task into database!", e);
            throw new ServiceSystemException("Exception while saving task into database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return javaTask;
    }

    @Override
    public JavaTask get(long id) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        JavaTask javaTask;
        try {
            startTransaction();
            javaTask = javaTaskDao.get(id);
            endTransaction();
            if (javaTask == null) {
                throw new ServiceBusinessException("No task with such ID was found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting task from database! Task ID: " + id, e);
            throw new ServiceSystemException("Exception while getting task from database! Task ID: " + id, e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return javaTask;
    }

    @Override
    public void update(JavaTask javaTask) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            int updRows = javaTaskDao.update(javaTask);
            endTransaction();
            if (updRows == 0) {
                throw new ServiceBusinessException("No task with such ID was found!");
            }
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while updating task in database!", e);
            throw new ServiceSystemException("Exception while updating task in database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
    }

    @Override
    public int delete(long id) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        int delRows;
        try {
            startTransaction();
            delRows = javaTaskDao.delete(id);
            endTransaction();
            if (delRows == 0) {
                throw new ServiceBusinessException("No task with such ID was found!");
            }
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while deleting task from database!", e);
            throw new ServiceSystemException("Exception while deleting task from database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return delRows;
    }

    @Override
    public JavaTask getNextTaskByDiffGroup(User user, long solvedTaskID) throws ServiceBusinessException, ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        JavaTask newTask;
        TaskOrder order;
        try {
            startTransaction();
            //Getting next task in database
            newTask = javaTaskDao.getNextTaskByDiffGroup(user.getLevel(), solvedTaskID);
            //After registration user rating increases by 1
            if (solvedTaskID == 0) {

            }
            //Creation of new "NOT SOLVED" task order with new task
            order = new TaskOrder(user.getUserID(), newTask.getTaskID(), "NOT SOLVED");
            TaskOrder order1 = orderDao.save(order);
            endTransaction();
            if (newTask == null) {
                throw new ServiceBusinessException("No task with such ID was found!");
            }
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while getting task from database!", e);
            throw new ServiceSystemException("Exception while getting task from database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return newTask;
    }

    @Override
    public JavaTask getNotSolvedTask(User user) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        JavaTask javaTask;
        TaskOrder order;
        try {
            startTransaction();
            order = orderDao.getNotSolvedOrder(user);
            if (order == null) {
                throw new ServiceBusinessException("No order with not solved task was found!");
            }
            javaTask = javaTaskDao.get(order.getTaskID());
            endTransaction();
            if (javaTask == null) {
                throw new ServiceBusinessException("No task with such ID was found (getNotSolvedTask method)!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting not solved task from database!", e);
            throw new ServiceSystemException("Exception while getting not solved task from database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return javaTask;
    }

    @Override
    public List<JavaTask> getPopularJavaTasks(int taskAmount) throws ServiceBusinessException, ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        List<JavaTask> taskList;
        try {
            startTransaction();
            taskList = javaTaskDao.getPopularJavaTasks(taskAmount);
            endTransaction();
            if (taskList.size() == 0) {
                throw new ServiceBusinessException("No tasks were found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting popular tasks from database!", e);
            throw new ServiceSystemException("Exception while getting popular tasks from database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return taskList;
    }

    @Override
    public List<JavaTask> getAll() throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        List<JavaTask> taskList;
        try {
            startTransaction();
            taskList = javaTaskDao.getAll();
            endTransaction();
            if (taskList.size() == 0) {
                throw new ServiceBusinessException("No tasks were found!");
            }
        } catch (DAOSystemException e) {
            log.info("Exception while getting all tasks from database!", e);
            throw new ServiceSystemException("Exception while getting all tasks from database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return taskList;
    }

    @Override
    public void createJavaTasksTable() throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            javaTaskDao.createJavaTasksTable();
        } catch (DAOSystemException e) {
            log.info("Exception while creating tasks table in database!", e);
            throw new ServiceSystemException("Exception while creating tasks table in database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
    }

    @Override
    public void deleteJavaTasksTable() throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            javaTaskDao.deleteJavaTasksTable();
        } catch (DAOSystemException e) {
            log.info("Exception while deleting tasks table in database!", e);
            throw new ServiceSystemException("Exception while deleting tasks table in database!", e);
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
