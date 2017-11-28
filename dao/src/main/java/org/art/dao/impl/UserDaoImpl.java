package org.art.dao.impl;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.UserDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.repository.UserRepository;
import org.art.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User DAO implementation
 */
@Data
@Repository
public class UserDaoImpl implements UserDao {

    private UserRepository userRepository;

    public static final Logger log = LogManager.getLogger(UserDaoImpl.class);

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) throws DAOSystemException {
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            log.error("Cannot save user to the database!", e);
            throw new DAOSystemException("Cannot save user to the database!", e);
        }
        return savedUser;
    }

    @Override
    public User get(Long id) throws DAOSystemException {
        User user;
        try {
            user = userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("Cannot get user from the database!", e);
            throw new DAOSystemException("Cannot get user from the database!", e);
        }
        return user;
    }

    @Override
    public User update(User user) throws DAOSystemException {
        User updUser;
        try {
            updUser = userRepository.save(user);
        } catch (Exception e) {
            log.error("Cannot update user in the database!", e);
            throw new DAOSystemException("Cannot update user in the database!", e);
        }
        return updUser;
    }

    @Override
    public void delete(Long id) throws DAOSystemException {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Cannot delete user from the database!", e);
            throw new DAOSystemException("Cannot delete user from the database!", e);
        }
    }

    @Override
    public List<User> getUsersByClanName(String clanName) throws DAOSystemException {
        List<User> users;
        try {
            users = userRepository.findUsersByClanName(clanName);
        } catch (Exception e) {
            log.info("Cannot get users from the database!", e);
            throw new DAOSystemException("Cannot get users from the database!", e);
        }
        return users;
    }

    @Override
    public User getUserByLogin(String login) throws DAOSystemException {
        User user;
        try {
            user = userRepository.findUserByLogin(login);
        } catch (Exception e) {
            log.info("Cannot get user from the database!", e);
            throw new DAOSystemException("Cannot get user from the database!", e);
        }
        return user;
    }

    @Override
    public List<User> getTopUsers(int usersAmount) throws DAOSystemException {
        List<User> users;
        try {
            users = userRepository.findAll(PageRequest.of(0, usersAmount, Sort.Direction.DESC,
                    "rating")).getContent();
        } catch (Exception e) {
            log.info("Cannot get users from the database!", e);
            throw new DAOSystemException("Cannot get users from the database!", e);
        }
        return users;
    }

    @Override
    public List<User> getAllUsers() throws DAOSystemException {
        List<User> users;
        try {
            users = userRepository.findAll();
        } catch (Exception e) {
            log.info("Cannot get users from the database!", e);
            throw new DAOSystemException("Cannot get users from the database!", e);
        }
        return users;
    }
}
