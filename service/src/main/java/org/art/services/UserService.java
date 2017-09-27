package org.art.services;

import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.User;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;

import java.util.List;

public interface UserService extends Service<User> {

    /**
     * Service method for getting all users from specified clan
     *
     * @param clanName clanName the name of the user clan
     * @return the list of all users from specified clan
     * @throws ServiceBusinessException if no users were found in the database
     * @throws ServiceSystemException   if {@link DAOSystemException}
     *                                  was thrown during the users reading from the database
     */
    List<User> getUsersByClanName(String clanName) throws ServiceBusinessException, ServiceSystemException;

    /**
     * Service method for getting user by login
     *
     * @param login user's login
     * @return user with specified login and password
     * @throws ServiceBusinessException if no user was found in the database
     * @throws ServiceSystemException   if {@link DAOSystemException}
     *                                  was thrown during the user reading from the database
     */
    User getUserByLogin(String login) throws ServiceBusinessException, ServiceSystemException;

    /**
     * Service method for getting top users with the highest rating from the database
     *
     * @param usersAmount the amount of users from the top list
     * @return the list of the most experienced users
     * @throws ServiceBusinessException if no users were found in the database
     * @throws ServiceSystemException   if {@link DAOSystemException}
     *                                  was thrown during the users reading from the database
     */
    List<User> getTopUsers(int usersAmount) throws ServiceBusinessException, ServiceSystemException;

    /**
     * Service method for getting all users from the database
     *
     * @return the list of all users
     * @throws ServiceBusinessException if no users were found in the database
     * @throws ServiceSystemException   if {@link DAOSystemException}
     *                                  was thrown during the users reading from the database
     */
    List<User> getAllUsers() throws ServiceBusinessException, ServiceSystemException;

    /**
     * Creating table "users" in the database
     *
     * @throws ServiceSystemException if {@link DAOSystemException}
     *                                was thrown during the table creation in the database
     */
    void createUsersTable() throws ServiceSystemException;

    /**
     * Deleting table "users" from the database
     *
     * @throws ServiceSystemException if {@link DAOSystemException}
     *                                was thrown during the table deleting from the database
     */
    void deleteUsersTable() throws ServiceSystemException;
}
