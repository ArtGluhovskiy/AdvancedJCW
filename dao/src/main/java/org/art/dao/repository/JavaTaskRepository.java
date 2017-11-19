package org.art.dao.repository;

import org.art.entities.JavaTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface JavaTaskRepository extends CrudRepository<JavaTask, Long>, PagingAndSortingRepository<JavaTask, Long> {

    @Query("select t from JavaTask t where t.difficultyGroup = ?1 and t.taskId > ?2 order by t.taskId")
    List<JavaTask> findNextTaskByDiffGroup(String difGroup, Long taskID, Pageable pageable);

    JavaTask findFirstByDifficultyGroupAndTaskIdGreaterThan(String difGroup, Long taskId);
}
