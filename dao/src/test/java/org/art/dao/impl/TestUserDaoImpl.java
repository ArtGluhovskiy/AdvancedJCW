package org.art.dao.impl;

import org.art.dao.UserDao;
import org.art.dao.util.DbcpConnectionPool;
import org.art.entities.DifficultyGroup;
import org.art.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import static org.art.dao.util.DateTimeUtil.toSQLDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUserDaoImpl {

    static ThreadLocal<Connection> threadCache;
    static DbcpConnectionPool connPool;
    static UserDao userDao;

    @BeforeAll
    static void initAll() throws SQLException {
        threadCache = new ThreadLocal<>();
        connPool = DbcpConnectionPool.getInstance();
        Connection conn = connPool.getConnection();
        threadCache.set(conn);
        userDao = UserDaoImpl.getInstance();
        ((UserDaoImpl) userDao).setThreadCache(threadCache);

        Statement st = conn.createStatement();

        String createSchema = "CREATE SCHEMA TEST;";

        st.execute(createSchema);

        String createTable = "CREATE TABLE users (\n" +
                "  user_id    INT(11)     AUTO_INCREMENT PRIMARY KEY,\n" +
                "  rating     INT,\n" +
                "  clan_name  VARCHAR(15),\n" +
                "  login      VARCHAR(15),\n" +
                "  password   VARCHAR(15),\n" +
                "  first_name VARCHAR(15),\n" +
                "  last_name  VARCHAR(15),\n" +
                "  email      VARCHAR(25),\n" +
                "  reg_date DATE," +
                "  role       VARCHAR(10) DEFAULT 'user',\n" +
                "  status     VARCHAR(10) DEFAULT 'active',\n" +
                "  birth_date DATE,\n" +
                "  level ENUM('BEGINNER', 'EXPERIENCED', 'EXPERT') DEFAULT 'BEGINNER',\n" +
                "  CONSTRAINT clan_name_unique UNIQUE(clan_name)\n" +
                ");";

        st.execute(createTable);

    }

    @Test
    @DisplayName("User saving test")
    void saveUserTest() throws Exception {

        //Start transaction
        threadCache.get().setAutoCommit(false);

        //Amount of users in the database before saving
        int userAmount = userDao.getAllUsers().size();

        User user = new User("Sharks", "gooder", "8273gds", "Allen",
                "Swift", "swift@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());

        userDao.save(user);
        assertEquals(userAmount + 1, userDao.getAllUsers().size());

        threadCache.get().rollback();
    }

    @Test
    @DisplayName("User getting test")
    void getUserTest() throws Exception {

        //Start transaction
        threadCache.get().setAutoCommit(false);

        User user = new User("Sharks", "gooder", "8273gds", "Allen",
                "Swift", "swift@gmail.com", new Date(System.currentTimeMillis()), "user",
                "ACTIVE", toSQLDate("24-02-1993"), DifficultyGroup.BEGINNER.toString());

        User user1 = userDao.save(user);
        User user2 = userDao.get(user1.getUserID());
        assertEquals(user1, user2);

        threadCache.get().rollback();
    }

    @AfterAll
    static void tearDown() throws SQLException {

        //Closing connection
        threadCache.get().close();
    }
}
