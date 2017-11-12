package org.art.dao.repository;

import org.art.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByClanName(String clanName);

    User findUserByLogin(String login);
}
