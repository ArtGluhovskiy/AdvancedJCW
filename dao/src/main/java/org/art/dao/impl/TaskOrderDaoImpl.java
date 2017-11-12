package org.art.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.TaskOrderDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.repository.TaskOrderRepository;
import org.art.dao.utils.EMUtil;
import org.art.dto.OrderDTO;
import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.art.dao.utils.DbcpConnectionPool.close;

@Repository
public class TaskOrderDaoImpl implements TaskOrderDao {

    private TaskOrderRepository orderRepository;

    public static final Logger log = LogManager.getLogger(TaskOrderDaoImpl.class);

    @Autowired
    public void setTaskOrderRepository(TaskOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public TaskOrder save(TaskOrder taskOrder) throws DAOSystemException {
        TaskOrder savedOrder;
        try {
            savedOrder = orderRepository.save(taskOrder);
        } catch (Exception e) {
            log.info("Cannot save task order to the database", e);
            throw new DAOSystemException("Cannot save task order to the database", e);
        }
        return savedOrder;
    }

    @Override
    public TaskOrder get(Long id) throws DAOSystemException {
        TaskOrder order;
        try {
            order = orderRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.info("Cannot get task order from the database! ID: " + id, e);
            throw new DAOSystemException("Cannot get task order from the database! ID: " + id, e);
        }
        return order;
    }

    @Override
    public TaskOrder update(TaskOrder order) throws DAOSystemException {
        TaskOrder updOrder;
        try {
           updOrder = orderRepository.save(order);
        } catch (Exception e) {
            log.info("Cannot update task order in the database!", e);
            throw new DAOSystemException("Cannot update task order in the database!", e);
        }
        return updOrder;
    }

    @Override
    public void delete(Long id) throws DAOSystemException {
        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {
            log.info("Cannot delete task order from the database! ID: " + id, e);
            throw new DAOSystemException("Cannot delete task order from the database! ID: " + id, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderDTO> getUserSolvedTaskOrders(Long id) throws DAOSystemException {
        List<OrderDTO> orders;
        EntityManager em = EMUtil.getThreadCachedEM();
        try {
            em.getTransaction().begin();
            orders = em.createQuery("select new org.art.dto.OrderDTO(u.userID, u.login," +
                    " t.difficultyGroup, t.shortDescr, o.regDate, o.status, t.regDate, t.popularity, t.elapsedTime," +
                    " o.execTime, o.orderID) from USERS u inner join u.orders o inner join o.javaTask t " +
                    "where o.status = 'SOLVED' and u.userID = :id").setParameter("id", id).getResultList();

            em.getTransaction().commit();
        } catch (Exception e) {
            log.info("Cannot get task orders from the database! User ID: " + id, e);
            throw new DAOSystemException("Cannot get task order from the database! User ID: " + id, e);
        }
        return orders;
    }

    @Override
    public TaskOrder getNotSolvedOrder(User user) throws DAOSystemException {
        TaskOrder order;
        try {
            order = orderRepository.getNotSolvedOrder(user);
        } catch (Exception e) {
            log.info("Cannot get task order from the database!", e);
            throw new DAOSystemException("Cannot get task order from the database!", e);
        }
        return order;
    }

//    @Override
//    public void createTaskOrderTable() throws DAOSystemException {
//        Connection conn = threadCache.get();
//        Statement stmt = null;
//        try {
//            stmt = conn.createStatement();
//            StringBuilder sb = new StringBuilder("CREATE TABLE task_orders (")
//                    .append("order_id INT(11) PRIMARY KEY AUTO_INCREMENT,")
//                    .append("user_id INT(11),")
//                    .append("task_id INT(11),")
//                    .append("reg_date DATE,")
//                    .append("exec_time BIGINT DEFAULT 0,")
//                    .append("status ENUM('SOLVED', 'NOT SOLVED') DEFAULT 'NOT SOLVED',")
//                    .append("CONSTRAINT fk_user_id FOREIGN KEY (user_id)")
//                    .append("REFERENCES users (user_id),")
//                    .append("CONSTRAINT fk_task_id FOREIGN KEY (task_id)")
//                    .append("REFERENCES java_tasks (task_id));");
//            stmt.execute(sb.toString());
//        } catch (SQLException e) {
//            log.info("Cannot create task orders table in database!", e);
//            throw new DAOSystemException("Cannot create task orders table in database!", e);
//        } finally {
//            close(stmt);
//        }
//    }
//
//    @Override
//    public void deleteTaskOrderTable() throws DAOSystemException {
//        Connection conn = threadCache.get();
//        Statement stmt = null;
//        try {
//            stmt = conn.createStatement();
//            stmt.execute("DROP TABLE task_orders;");
//        } catch (SQLException e) {
//            log.info("Cannot delete task orders table from database!", e);
//            throw new DAOSystemException("Cannot delete task orders table from database!", e);
//        } finally {
//            close(stmt);
//        }
//    }
}
