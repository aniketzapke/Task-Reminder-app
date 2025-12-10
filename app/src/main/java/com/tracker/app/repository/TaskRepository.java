package com.tracker.app.repository;
import com.tracker.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{
    List<Task> findByStatus(String status);
    List<Task> findByPriority(String priority);

//    @Query("select t from Task t where t.title like %:keyword%")
    List<Task> searchByTitle(@Param("keyword") String keyword);

    List<Task> findByDueDate(String date);
}
