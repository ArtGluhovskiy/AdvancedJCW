package org.art.dao.repository;

import org.art.entities.TaskOrder;
import org.springframework.data.repository.CrudRepository;

public interface TaskOrderRepository extends CrudRepository<TaskOrder, Long> {
}
