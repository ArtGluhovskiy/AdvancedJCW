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

import javax.persistence.PersistenceContext;
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
    public User get(long id) throws DAOSystemException {
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
    public void delete(long id) throws DAOSystemException {

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

//    @Override
//    public void createUsersTable() throws DAOSystemException {
//        Connection conn = threadCache.get();
//        Statement stmt = null;
//        try {
//            stmt = conn.createStatement();
//            StringBuilder sb = new StringBuilder("CREATE TABLE users (")
//                    .append("user_id INT(11) AUTO_INCREMENT PRIMARY KEY,")
//                    .append("rating INT,")
//                    .append("clan_name VARCHAR(15),")
//                    .append("login VARCHAR(15),")
//                    .append("password VARCHAR(15),")
//                    .append("first_name VARCHAR(15),")
//                    .append("last_name VARCHAR(15),")
//                    .append("email VARCHAR(25),")
//                    .append("reg_date DATE,")
//                    .append("role VARCHAR(10) DEFAULT 'user',")
//                    .append("status VARCHAR(10) DEFAULT 'ACTIVE',")
//                    .append("birth_date DATE,")
//                    .append("level ENUM('BEGINNER', 'EXPERIENCED', 'EXPERT') DEFAULT 'BEGINNER',")
//                    .append("UNIQUE KEY user_login_unique (login));");
//            stmt.execute(sb.toString());
//        } catch (SQLException e) {
//            log.info("Cannot create users table", e);
//            throw new DAOSystemException("Cannot create users table", e);
//        } finally {
//            close(stmt);
//        }
//    }
//
//    public void deleteUsersTable() throws DAOSystemException {
//        Connection conn = threadCache.get();
//        Statement stmt = null;
//        try {
//            stmt = conn.createStatement();
//            stmt.execute("DROP TABLE users;");
//        } catch (SQLException e) {
//            log.info("Cannot delete users table!", e);
//            throw new DAOSystemException("Cannot delete users table!", e);
//        } finally {
//            close(stmt);
//        }
//    }
}
