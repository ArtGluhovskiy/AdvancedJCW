package org.art.dao.repository;

import org.art.entities.TaskOrder;
import org.art.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskOrderRepository extends CrudRepository<TaskOrder, Long> {

    @Query("select t from TaskOrder t where t.user = ?1 and t.status = 'NOT SOLVED'")
    TaskOrder getNotSolvedOrder(User user);

    List<TaskOrder> getTaskOrderByUser(User user);
}
