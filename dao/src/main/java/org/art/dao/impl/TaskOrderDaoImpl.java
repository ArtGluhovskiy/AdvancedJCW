package org.art.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.TaskOrderDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dto.OrderDTO;
import org.art.entities.TaskOrder;
import org.art.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.art.dao.util.DbcpConnectionPool.close;

public class TaskOrderDaoImpl implements TaskOrderDao {

    private static volatile TaskOrderDao instance = null;
    private ThreadLocal<Connection> threadCache;
    public static final Logger log = LogManager.getLogger(TaskOrderDaoImpl.class);

    private static final String SAVE_ORDER_QUERY = "INSERT INTO task_orders (user_id, task_id, reg_date, status) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ORDER_QUERY = "UPDATE task_orders SET user_id = ?, task_id = ?, status = ?, exec_time = ? " +
            "WHERE order_id = ?";
    private static final String GET_ORDER_QUERY = "SELECT * FROM task_orders WHERE order_id = ?";
    private static final String GET_ALL_ORDERS_QUERY = "SELECT u.user_id, u.login, jt.difficulty_group, jt.short_desc, ord.reg_date," +
                                                       " ord.status, jt.reg_date, jt.popularity, jt.elapsed_time, ord.exec_time, ord.order_id " +
                                                       "FROM users u INNER JOIN java_tasks jt INNER JOIN task_orders ord " +
                                                       "ON u.user_id = ord.user_id AND jt.task_id = ord.task_id " +
                                                       "HAVING u.user_id = ?;";
    private static final String GET_NOT_SOLVED_QUERY = "SELECT * FROM task_orders WHERE user_id = ? AND" +
                                                       " task_orders.status = 'NOT SOLVED' LIMIT 1;";
    private static final String DELETE_ORDER_QUERY = "DELETE FROM task_orders WHERE order_id = ?";

    /**
     * This is an implementation of "Thread Scope" approach.
     * It means that every thread while transaction execution has its own connection
     * which contains in thread's cache (@code ThreadLocal).
     * @param threadCache thread's cache which contains its own connection for transaction
     */
    public void setThreadCache(ThreadLocal<Connection> threadCache) {
        this.threadCache = threadCache;
    }

    private TaskOrderDaoImpl() {
    }

    public static TaskOrderDao getInstance() {
        TaskOrderDao orderDao = instance;
        if (orderDao == null) {
            synchronized (TaskOrderDaoImpl.class) {
                orderDao = instance;
                if (orderDao == null) {
                    instance = orderDao = new TaskOrderDaoImpl();
                }
            }
        }
        return orderDao;
    }

    @Override
    public TaskOrder save(TaskOrder taskOrder) throws DAOSystemException {
        PreparedStatement psSave = null;
        Connection conn = threadCache.get();
        System.out.println(Thread.currentThread().getName() + " in get save task order in DAO. His connection: " + conn);
        ResultSet rs = null;
        try {
            psSave = conn.prepareStatement(SAVE_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS);
            psSave.setLong(1, taskOrder.getUserID());
            psSave.setLong(2, taskOrder.getTaskID());
            psSave.setDate(3, taskOrder.getRegDate());
            psSave.setString(4, taskOrder.getStatus());
            psSave.executeUpdate();
            rs = psSave.getGeneratedKeys();
            if (rs.next()) {
                taskOrder.setOrderID(rs.getLong(1));
            }
        } catch (SQLException e) {
            log.info("Cannot save task order in the database", e);
            throw new DAOSystemException("Cannot save task order in the database", e);
        } finally {
            close(rs);
            close(psSave);
        }
        return taskOrder;
    }

    @Override
    public TaskOrder get(long id) throws DAOSystemException {
        PreparedStatement psGet = null;
        TaskOrder order = null;
        Connection conn = threadCache.get();
        ResultSet rs = null;
        try {
            psGet = conn.prepareStatement(GET_ORDER_QUERY);
            psGet.setLong(1, id);
            rs = psGet.executeQuery();
            if (rs.next()) {
                order = new TaskOrder(rs.getLong(2), rs.getLong(3), rs.getString(5));
                order.setOrderID(rs.getLong(1));
                order.setRegDate(rs.getDate(4));
            }
        } catch (SQLException e) {
            log.info("Cannot get task order from the database! ID: " + id, e);
            throw new DAOSystemException("Cannot get task order from the database! ID: " + id, e);
        } finally {
            close(rs);
            close(psGet);
        }
        return order;
    }

    @Override
    public int update(TaskOrder order) throws DAOSystemException {
        PreparedStatement psUpdate = null;
        Connection conn = threadCache.get();
        int updTaskOrders;
        try {
            psUpdate = conn.prepareStatement(UPDATE_ORDER_QUERY);
            psUpdate.setLong(1, order.getUserID());
            psUpdate.setLong(2, order.getTaskID());
            psUpdate.setString(3, order.getStatus());
            psUpdate.setLong(4, order.getExecTime());
            psUpdate.setLong(5, order.getOrderID());
            updTaskOrders = psUpdate.executeUpdate();
        } catch (SQLException e) {
            log.info("Cannot update task order in database!", e);
            throw new DAOSystemException("Cannot update task order in database!", e);
        } finally {
            close(psUpdate);
        }
        return updTaskOrders;
    }

    @Override
    public int delete(long id) throws DAOSystemException {
        PreparedStatement psDelete = null;
        Connection conn = threadCache.get();
        int delAmount;
        try {
            psDelete = conn.prepareStatement(DELETE_ORDER_QUERY);
            psDelete.setLong(1, id);
            delAmount = psDelete.executeUpdate();
        } catch (SQLException e) {
            log.info("Cannot delete task order from database! ID: " + id, e);
            throw new DAOSystemException("Cannot delete task order from database! ID: " + id, e);
        } finally {
            close(psDelete);
        }
        return delAmount;
    }

    @Override
    public List<OrderDTO> getUserTaskOrders(long id) throws DAOSystemException {
        PreparedStatement psGetAllOrders = null;
        OrderDTO compOrder;
        List<OrderDTO> compOrders = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try {
            psGetAllOrders = conn.prepareStatement(GET_ALL_ORDERS_QUERY);
            psGetAllOrders.setLong(1, id);
            rs = psGetAllOrders.executeQuery();
            while (rs.next()) {
                compOrder = readOrder(rs);
                compOrders.add(compOrder);
            }
        } catch (SQLException e) {
            log.info("Cannot get task orders from database! User ID: " + id, e);
            throw new DAOSystemException("Cannot get task order from database! User ID: " + id, e);
        } finally {
            close(rs);
            close(psGetAllOrders);
        }
        return compOrders;
    }

    @Override
    public TaskOrder getNotSolvedOrder(User user) throws DAOSystemException {
        PreparedStatement psGetNotSolved = null;
        TaskOrder order = null;
        Connection conn = threadCache.get();
        ResultSet rs = null;
        try {
            psGetNotSolved = conn.prepareStatement(GET_NOT_SOLVED_QUERY);
            psGetNotSolved.setLong(1, user.getUserID());
            rs = psGetNotSolved.executeQuery();
            if (rs.next()) {
                order = new TaskOrder(rs.getLong(2), rs.getLong(3), rs.getString(5));
                order.setOrderID(rs.getLong(1));
                order.setRegDate(rs.getDate(4));
            }
        } catch (SQLException e) {
            log.info("Cannot get task order from the database!", e);
            throw new DAOSystemException("Cannot get task order from the database!", e);
        } finally {
            close(rs);
            close(psGetNotSolved);
        }
        return order;
    }

    /**
     * Method reads order data from the {@code ResultSet} and creates OrderDTO
     *
     * @param rs {@code ResultSet} with order date
     * @return OrderDTO with data from the database
     * @throws SQLException in case of system problems while order data reading
     */
    private OrderDTO readOrder(ResultSet rs) throws SQLException {
        OrderDTO compOrder;
        compOrder = new OrderDTO(rs.getLong(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getDate(5), rs.getString(6), rs.getDate(7),
                rs.getInt(8), rs.getLong(9), rs.getLong(10), rs.getLong(11));
        return compOrder;
    }

    @Override
    public void createTaskOrderTable() throws DAOSystemException {
        Connection conn = threadCache.get();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            StringBuilder sb = new StringBuilder("CREATE TABLE task_orders (")
                    .append("order_id INT(11) PRIMARY KEY AUTO_INCREMENT,")
                    .append("user_id INT(11),")
                    .append("task_id INT(11),")
                    .append("reg_date DATE,")
                    .append("exec_time BIGINT DEFAULT 0,")
                    .append("status ENUM('SOLVED', 'NOT SOLVED') DEFAULT 'NOT SOLVED',")
                    .append("CONSTRAINT fk_user_id FOREIGN KEY (user_id)")
                    .append("REFERENCES users (user_id),")
                    .append("CONSTRAINT fk_task_id FOREIGN KEY (task_id)")
                    .append("REFERENCES java_tasks (task_id));");
            stmt.execute(sb.toString());
        } catch (SQLException e) {
            log.info("Cannot create task orders table in database!", e);
            throw new DAOSystemException("Cannot create task orders table in database!", e);
        } finally {
            close(stmt);
        }
    }

    @Override
    public void deleteTaskOrderTable() throws DAOSystemException {
        Connection conn = threadCache.get();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE task_orders;");
        } catch (SQLException e) {
            log.info("Cannot delete task orders table from database!", e);
            throw new DAOSystemException("Cannot delete task orders table from database!", e);
        } finally {
            close(stmt);
        }
    }
}
