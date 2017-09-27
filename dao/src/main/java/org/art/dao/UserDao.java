package org.art.dao;

import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.User;

import java.util.List;

public interface UserDao extends DAO<User> {

    /**
     * Getting all users from specified clan
     *
     * @param clanName the name of the user clan
     * @return the list of all users from specified clan
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the users reading from the database
     */
    List<User> getUsersByClanName(String clanName) throws DAOSystemException;

    /**
     * Getting user by login
     *
     * @param login user's login
     * @return user with specified login and password
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the user reading from the database
     */
    User getUserByLogin(String login) throws DAOSystemException;

    /**
     * Getting top users with the highest rating from the database
     *
     * @param usersAmount the amount of users from the top list
     * @return the list of the most experienced users
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the users reading from the database
     */
    List<User> getTopUsers(int usersAmount) throws DAOSystemException;

    /**
     * Getting all users from the database
     *
     * @return the list of all users
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the users reading from the database
     */
    List<User> getAllUsers() throws DAOSystemException;

    /**
     * Creating table "users" in the database
     *
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the table creation in the database
     */
    void createUsersTable() throws DAOSystemException;

    /**
     * Deleting table "users" from the database
     *
     * @throws DAOSystemException if {@link java.sql.SQLException}
     *                            was thrown during the table deleting from the database
     */
    void deleteUsersTable() throws DAOSystemException;
}
