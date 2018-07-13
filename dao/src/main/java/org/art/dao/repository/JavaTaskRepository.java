package org.art.dao.repository;

import org.art.entities.JavaTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.List;

public interface JavaTaskRepository extends CrudRepository<JavaTask, Long>, PagingAndSortingRepository<JavaTask, Long> {

    @Query("select t from JavaTask t where t.difficultyGroup = ?1 and t.taskId > ?2 order by t.taskId")
    List<JavaTask> findNextTaskByDiffGroup(String difGroup, Long taskID, Pageable pageable);

    JavaTask findFirstByDifficultyGroupAndTaskIdGreaterThan(String difGroup, Long taskId);

    @Modifying(clearAutomatically = true)
    @QueryHints(value = {@QueryHint(name = org.hibernate.annotations.QueryHints.FLUSH_MODE, value = "AUTO")})
    @Query("update JavaTask t set t.popularity = ?1 where t.taskId = ?2")
    void updateTaskPopularity(int newPopularity, Long taskId);
}
