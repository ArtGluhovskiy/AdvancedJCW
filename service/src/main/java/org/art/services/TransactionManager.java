package org.art.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.exceptions.DAOSystemException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class TransactionManager {

    public static final Logger log = LogManager.getLogger(TransactionManager.class);
    protected ThreadLocal<Connection> threadCache;

    public void startTransaction() throws DAOSystemException {
        try {
            Connection conn = threadCache.get();
            conn.setAutoCommit(false);
            System.out.println(Thread.currentThread().getName() + " starts transaction! With connection: " + conn);
        } catch (SQLException e) {
            log.info("Cannot start transaction!");
            throw new DAOSystemException("Cannot start transaction!", e);
        }

    }

    public void endTransaction() throws DAOSystemException {
        try {
            Connection conn = threadCache.get();
            conn.commit();
            System.out.println(Thread.currentThread().getName() + " ends transaction! With connection: " + conn);
        } catch (SQLException e) {
            log.info("Cannot end transaction!");
            throw new DAOSystemException("Cannot end transaction!", e);
        }

    }

    public void backTransaction() throws DAOSystemException {
        try {
            threadCache.get().rollback();
            System.out.println(Thread.currentThread().getName() + " rolls back transaction!");
        } catch (SQLException e) {
            log.info("Cannot roll back transaction");
            throw new DAOSystemException("Cannot roll back transaction", e);
        }
    }

    public void setThreadCache(ThreadLocal<Connection> threadCache) {
        this.threadCache = threadCache;
    }
}
