package com.tracker.app.service;

import com.tracker.app.entity.Task;
import com.tracker.app.enums.TaskPriority;
import com.tracker.app.enums.TaskStatus;
import com.tracker.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Taskservice {

    @Autowired
    private TaskRepository taskRepository;

    // ✅ Pagination (mentor requirement)
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Task updated) {
        return taskRepository.save(updated);
    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> findByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> searchByTitle(String keyword) {
        return taskRepository.searchByTitle(keyword);
    }

    public List<Task> findByDueDate(String date) {
        return taskRepository.findByDueDate(date);
    }

    public List<Task> sortByField(String field) {
        return taskRepository.findAll(); // simple fallback
    }

    // ✅ PAGINATION + FILTER LOGIC (mentor asked)
    public Page<Task> getPagedTasks(
            Pageable pageable,
            TaskStatus status,
            TaskPriority priority,
            String keyword) {

        if (status != null) {
            return taskRepository.findByStatus(status, pageable);
        }

        if (priority != null) {
            return taskRepository.findByPriority(priority, pageable);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            return taskRepository.searchByTitle(keyword, pageable);
        }

        return taskRepository.findAll(pageable);
    }
}
