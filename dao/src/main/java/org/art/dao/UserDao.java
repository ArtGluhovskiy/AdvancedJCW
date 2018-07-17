package org.art.dao;

import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.User;

import java.util.List;

/**
 * UserDao interface with special methods.
 */
public interface UserDao extends DAO<User> {

    /**
     * Retrieves all users from specified clan.
     *
     * @param clanName the name of the user clan
     * @return the list of all users from specified clan
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the users reading from the database
     */
    List<User> getUsersByClanName(String clanName) throws DAOSystemException;

    /**
     * Retrieves a user by login
     *
     * @param login user's login
     * @return user with specified login and password
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the user reading from the database
     */
    User getUserByLogin(String login) throws DAOSystemException;

    /**
     * Retrieves top users with the highest rating from the database
     *
     * @param usersAmount the amount of users from the top list
     * @return the list of the most experienced users
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the users reading from the database
     */
    List<User> getTopUsers(int usersAmount) throws DAOSystemException;

    /**
     * Retrieves all users from the database
     *
     * @return the list of all users
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the users reading from the database
     */
    List<User> getAllUsers() throws DAOSystemException;
}
