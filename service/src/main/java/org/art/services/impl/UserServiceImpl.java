package org.art.services.impl;

import org.art.dao.*;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.impl.*;
import org.art.entities.*;
import org.art.db.ConnectionPoolManager;
import org.art.services.*;
import org.art.services.exceptions.*;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class UserServiceImpl extends TransactionManager implements UserService {

    private static UserService INSTANCE;

    private UserDao userDao;
    private TaskOrderDao orderDao;
    private JavaTaskDao taskDao;
    private ConnectionPoolManager connPool;

    private UserServiceImpl() {
        connPool = ConnectionPoolManager.getInstance();
        threadCache = connPool.getThreadCache();
        userDao = UserDaoImpl.getInstance();
        ((UserDaoImpl) userDao).setThreadCache(threadCache);
        taskDao = JavaTaskDaoImpl.getInstance();
        ((JavaTaskDaoImpl) taskDao).setThreadCache(threadCache);
        orderDao = TaskOrderDaoImpl.getInstance();
        ((TaskOrderDaoImpl) orderDao).setThreadCache(threadCache);
    }

    public static UserService getInstance() {
        UserService userService = INSTANCE;
        if (userService == null) {
            synchronized (UserServiceImpl.class) {
                userService = INSTANCE;
                if (userService == null) {
                    INSTANCE = userService = new UserServiceImpl();
                }
            }
        }
        return userService;
    }

    @Override
    public User save(User user) throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            userDao.save(user);
            endTransaction();
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Exception while saving user into database!", e);
            throw new ServiceSystemException("Exception while saving user into database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return user;
    }

    @Override
    public User get(long id) throws ServiceSystemException, ServiceBusinessException {
        User user;
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            user = userDao.get(id);
            endTransaction();
            if (user == null) {
                throw new ServiceBusinessException("No such user in the database!");
            }
        } catch (DAOSystemException e) {
            log.info("Cannot get user from the database! ID: " + id, e);
            throw new ServiceSystemException("Cannot get user from the database! ID: " + id, e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return user;
    }

    @Override
    public void update(User user) throws ServiceSystemException, ServiceBusinessException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            int amount = userDao.update(user);
            endTransaction();
            if (amount == 0) {
                throw new ServiceBusinessException("Cannot find user with such ID");
            }
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Cannot update user in the database!", e);
            throw new ServiceSystemException("Cannot update user in the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
    }

    @Override
    public int delete(long id) throws ServiceBusinessException, ServiceSystemException {
        Connection conn = connPool.getConnection();
        int num;
        threadCache.set(conn);
        try {
            startTransaction();
            num = userDao.delete(id);
            endTransaction();
            if (num == 0) {
                throw new ServiceBusinessException("No such user in the database!");
            }
        } catch (DAOSystemException e) {
            tryRollBackTransaction(e);
            log.info("Can't delete user from the database!", e);
            throw new ServiceSystemException("Can't delete user from the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return num;
    }

    @Override
    public List<User> getUsersByClanName(String clanName) throws ServiceBusinessException, ServiceSystemException {
        Connection conn = connPool.getConnection();
        List<User> usersList;
        threadCache.set(conn);
        try {
            startTransaction();
            usersList = userDao.getUsersByClanName(clanName);
            endTransaction();
            if (usersList.size() == 0) {
                throw new ServiceBusinessException("No user was found!");
            }
        } catch (DAOSystemException e) {
            log.info("Cannot get users from the database!", e);
            throw new ServiceSystemException("Cannot get users from the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return usersList;
    }

    @Override
    public User getUserByLogin(String login) throws ServiceBusinessException, ServiceSystemException {
        User user;
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            startTransaction();
            user = userDao.getUserByLogin(login);
            endTransaction();
            if (user == null) {
                throw new ServiceBusinessException("No user was found!");
            }
        } catch (DAOSystemException e) {
            log.info("Cannot get user from the database!", e);
            throw new ServiceSystemException("Cannot get user from the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return user;
    }

    @Override
    public List<User> getTopUsers(int usersAmount) throws ServiceBusinessException, ServiceSystemException {
        Connection conn = connPool.getConnection();
        List<User> usersList;
        threadCache.set(conn);
        try {
            startTransaction();
            usersList = userDao.getTopUsers(usersAmount);
            endTransaction();
            if (usersList.size() == 0) {
                throw new ServiceBusinessException("No users were found!");
            }
        } catch (DAOSystemException e) {
            log.info("Cannot get users from the database!", e);
            throw new ServiceSystemException("Cannot get users from the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return usersList;
    }

    @Override
    public List<User> getAllUsers() throws ServiceBusinessException, ServiceSystemException {
        Connection conn = connPool.getConnection();
        List<User> usersList;
        threadCache.set(conn);
        try {
            startTransaction();
            usersList = userDao.getAllUsers();
            endTransaction();
            if (usersList.size() == 0) {
                throw new ServiceBusinessException("No users were found!");
            }
        } catch (DAOSystemException e) {
            log.info("Cannot get users from the database!", e);
            throw new ServiceSystemException("Cannot get users from the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
        return usersList;
    }

    @Override
    public void createUsersTable() throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            userDao.createUsersTable();
        } catch (DAOSystemException e) {
            log.info("Cannot create users table in the database!", e);
            throw new ServiceSystemException("Cannot create users table in the database!", e);
        } finally {
            ConnectionPoolManager.close(conn);
        }
    }

    @Override
    public void deleteUsersTable() throws ServiceSystemException {
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        try {
            userDao.deleteUsersTable();
        } catch (DAOSystemException e) {
            log.info("Cannot delete users table from the database!", e);
            throw new ServiceSystemException("Cannot delete users table from the database!", e);
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
