package org.art.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.art.dao.utils.DbcpConnectionPool.close;

/**
 * User DAO implementation
 */
public class UserDaoImpl implements UserDao {

    private static volatile UserDao instance = null;
    private ThreadLocal<Connection> threadCache;
    public static final Logger log = LogManager.getLogger(UserDaoImpl.class);

    private static final String SAVE_USER_QUERY = "INSERT INTO users (rating, clan_name, login, password, first_name, " +
                                                  "last_name, email, reg_date, role, status, birth_date, level) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET rating = ?, clan_name = ?, login = ?, password = ?, first_name = ?, " +
                                                    "last_name = ?, email = ?, role = ?, status = ?, level = ? WHERE user_id = ?";
    private static final String GET_USER_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String GET_USER_BY_CLAN_NAME_QUERY = "SELECT * FROM users WHERE clan_name = ?";
    private static final String GET_USER_BY_LOGIN_QUERY = "SELECT * FROM users WHERE login = ?";
    private static final String GET_TOP_USERS_QUERY = "SELECT * FROM users ORDER BY rating DESC LIMIT ?";
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";

    /**
     * This is an implementation of "Thread Scope" approach.
     * It means that every thread while transaction execution has its own connection
     * which contains in thread's cache (@code ThreadLocal).
     *
     * @param threadCache thread's cache which contains its own connection for transaction
     */
    public void setThreadCache(ThreadLocal<Connection> threadCache) {
        this.threadCache = threadCache;
    }

    private UserDaoImpl() {
    }

    public static UserDao getInstance() {
        UserDao userDao = instance;
        if (userDao == null) {
            synchronized (UserDaoImpl.class) {
                userDao = instance;
                if (userDao == null) {
                    instance = userDao = new UserDaoImpl();
                }
            }
        }
        return userDao;
    }

    @Override
    public User save(User user) throws DAOSystemException {
        PreparedStatement psSave = null;
        Connection conn = threadCache.get();
        ResultSet rs = null;
        try {
            psSave = conn.prepareStatement(SAVE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            psSave.setInt(1, user.getRating());
            psSave.setString(2, user.getClanName());
            psSave.setString(3, user.getLogin());
            psSave.setString(4, user.getPassword());
            psSave.setString(5, user.getFName());
            psSave.setString(6, user.getLName());
            psSave.setString(7, user.getEmail());
            psSave.setDate(8, user.getRegDate());
            psSave.setString(9, user.getRole());
            psSave.setString(10, user.getStatus());
            psSave.setDate(11, user.getBirthDate());
            psSave.setString(12, user.getLevel());
            psSave.executeUpdate();
            rs = psSave.getGeneratedKeys();
            if (rs.next()) {
                user.setUserID(rs.getLong(1));
            }
        } catch (SQLException e) {
            log.info("Cannot save user in the database!", e);
            throw new DAOSystemException("Cannot save user in the database!", e);
        } finally {
            close(rs);
            close(psSave);
        }
        return user;
    }

    @Override
    public User get(long id) throws DAOSystemException {
        PreparedStatement psGet = null;
        User user = null;
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try {
            psGet = conn.prepareStatement(GET_USER_QUERY);
            psGet.setLong(1, id);
            rs = psGet.executeQuery();
            System.out.println("Result set is: " + rs);
            if (rs.next()) {
                user = new User(rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getDate(9),
                        rs.getString(10), rs.getString(11), rs.getDate(12), rs.getString(13));
                user.setUserID(rs.getLong(1));
                user.setRating(rs.getInt(2));
            }
        } catch (SQLException e) {
            log.info("Cannot get user from the database!", e);
            throw new DAOSystemException("Cannot get user from the database!", e);
        } finally {
            close(rs);
            close(psGet);
        }
        return user;
    }

    @Override
    public int update(User user) throws DAOSystemException {
        PreparedStatement psUpdate = null;
        Connection conn = threadCache.get();
        int amount;
        try {
            psUpdate = conn.prepareStatement(UPDATE_USER_QUERY);
            psUpdate.setInt(1, user.getRating());
            psUpdate.setString(2, user.getClanName());
            psUpdate.setString(3, user.getLogin());
            psUpdate.setString(4, user.getPassword());
            psUpdate.setString(5, user.getFName());
            psUpdate.setString(6, user.getLName());
            psUpdate.setString(7, user.getEmail());
            psUpdate.setString(8, user.getRole());
            psUpdate.setString(9, user.getStatus());
            psUpdate.setString(10, user.getLevel());
            psUpdate.setLong(11, user.getUserID());
            amount = psUpdate.executeUpdate();
        } catch (SQLException e) {
            log.info("Cannot update user in the database!", e);
            throw new DAOSystemException("Cannot update user in the database!", e);
        } finally {
            close(psUpdate);
        }
        return amount;
    }

    @Override
    public int delete(long id) throws DAOSystemException {
        PreparedStatement psDelete = null;
        Connection conn = threadCache.get();
        int num;
        try {
            psDelete = conn.prepareStatement(DELETE_USER_QUERY);
            psDelete.setLong(1, id);
            num = psDelete.executeUpdate();
        } catch (SQLException e) {
            log.info("Cannot delete user from database!", e);
            throw new DAOSystemException("Cannot delete user from database!", e);
        } finally {
            close(psDelete);
        }
        return num;
    }

    @Override
    public List<User> getUsersByClanName(String clanName) throws DAOSystemException {
        PreparedStatement psGetByClan = null;
        User user;
        List<User> users = new ArrayList<>();
        Connection conn = threadCache.get();
        ResultSet rs = null;
        try {
            psGetByClan = conn.prepareStatement(GET_USER_BY_CLAN_NAME_QUERY);
            psGetByClan.setString(1, clanName);
            rs = psGetByClan.executeQuery();
            while (rs.next()) {
                user = readUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            log.info("Cannot get users from the database!", e);
            throw new DAOSystemException("Cannot get users from the database!", e);
        } finally {
            close(rs);
            close(psGetByClan);
        }
        return users;
    }

    @Override
    public User getUserByLogin(String login) throws DAOSystemException {
        PreparedStatement psGetByLoginAndPassword = null;
        User user = null;
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try {
            psGetByLoginAndPassword = conn.prepareStatement(GET_USER_BY_LOGIN_QUERY);
            psGetByLoginAndPassword.setString(1, login);
            rs = psGetByLoginAndPassword.executeQuery();
            while (rs.next()) {
                user = readUser(rs);
            }
        } catch (SQLException e) {
            log.info("Cannot get user from the database!", e);
            throw new DAOSystemException("Cannot get user from the database!", e);
        } finally {
            close(rs);
            close(psGetByLoginAndPassword);
        }
        return user;
    }

    @Override
    public List<User> getTopUsers(int usersAmount) throws DAOSystemException {
        PreparedStatement psGetTop = null;
        User user;
        List<User> users = new ArrayList<>(usersAmount);
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try {
            psGetTop = conn.prepareStatement(GET_TOP_USERS_QUERY);
            psGetTop.setInt(1, usersAmount);
            rs = psGetTop.executeQuery();
            while (rs.next()) {
                user = readUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            log.info("Cannot get users from the database!", e);
            throw new DAOSystemException("Cannot get users from the database!", e);
        } finally {
            close(rs);
            close(psGetTop);
        }
        return users;
    }

    @Override
    public List<User> getAllUsers() throws DAOSystemException {
        PreparedStatement psGetAll = null;
        User user;
        List<User> users = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try {
            psGetAll = conn.prepareStatement(GET_ALL_USERS_QUERY);
            rs = psGetAll.executeQuery();
            while (rs.next()) {
                user = readUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            log.info("Cannot get users from database!", e);
            throw new DAOSystemException("Cannot get users from database!", e);
        } finally {
            close(rs);
            close(psGetAll);
        }
        return users;
    }

    @Override
    public void createUsersTable() throws DAOSystemException {
        Connection conn = threadCache.get();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            StringBuilder sb = new StringBuilder("CREATE TABLE users (")
                    .append("user_id INT(11) AUTO_INCREMENT PRIMARY KEY,")
                    .append("rating INT,")
                    .append("clan_name VARCHAR(15),")
                    .append("login VARCHAR(15),")
                    .append("password VARCHAR(15),")
                    .append("first_name VARCHAR(15),")
                    .append("last_name VARCHAR(15),")
                    .append("email VARCHAR(25),")
                    .append("reg_date DATE,")
                    .append("role VARCHAR(10) DEFAULT 'user',")
                    .append("status VARCHAR(10) DEFAULT 'ACTIVE',")
                    .append("birth_date DATE,")
                    .append("level ENUM('BEGINNER', 'EXPERIENCED', 'EXPERT') DEFAULT 'BEGINNER',")
                    .append("UNIQUE KEY user_login_unique (login));");
            stmt.execute(sb.toString());
        } catch (SQLException e) {
            log.info("Cannot create users table", e);
            throw new DAOSystemException("Cannot create users table", e);
        } finally {
            close(stmt);
        }
    }

    public void deleteUsersTable() throws DAOSystemException {
        Connection conn = threadCache.get();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE users;");
        } catch (SQLException e) {
            log.info("Cannot delete users table!", e);
            throw new DAOSystemException("Cannot delete users table!", e);
        } finally {
            close(stmt);
        }
    }

    /**
     * Method reads user data from the {@code ResultSet} and creates user entity
     *
     * @param rs {@code ResultSet} with user date
     * @return user entity with data from the database
     * @throws SQLException in case of system problems while user reading
     */
    private User readUser(ResultSet rs) throws SQLException {
        User user;
        user = new User(rs.getString(3), rs.getString(4), rs.getString(5),
                rs.getString(6), rs.getString(7), rs.getString(8), rs.getDate(9),
                rs.getString(10), rs.getString(11), rs.getDate(12), rs.getString(13));
        user.setUserID(rs.getLong(1));
        user.setRating(rs.getInt(2));
        return user;
    }
}
