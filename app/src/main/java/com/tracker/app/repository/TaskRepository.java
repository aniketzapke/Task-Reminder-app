package com.tracker.app.repository;

import com.tracker.app.entity.Task;
import com.tracker.app.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(String priority);

    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchByTitle(@Param("keyword") String keyword);

    List<Task> findByDueDate(String date);
}

