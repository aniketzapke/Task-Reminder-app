package com.tracker.app.controller;
import com.tracker.app.entity.Task;
import com.tracker.app.service.Taskservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskRestController {

    @Autowired
    private Taskservice taskService;
    @Autowired
    public TaskRestController(Taskservice taskService){
        this.taskService=taskService;
    }
    @GetMapping
    public ResponseEntity<Page<Task>> getAll(Pageable pageable) {
        Page<Task> page = taskService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<Task> t = taskService.findById(id);

        if (t.isPresent()) return ResponseEntity.ok(t.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Task not present with id");
    }

    @PostMapping()
    public ResponseEntity<Task> create(@RequestBody Task task) {
        task.setCreatedAt(LocalDateTime.now());
        Task saved = taskService.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Integer id, @RequestBody Task task) {
        Optional<Task> existing = taskService.findById(id);

        if (existing.isEmpty()) return ResponseEntity.notFound().build();

        task.setId(id);
        task.setCreatedAt(existing.get().getCreatedAt());

        Task updated = taskService.updateTask(task);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (taskService.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(taskService.findByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getByPriority(@PathVariable String priority) {
        return ResponseEntity.ok(taskService.findByPriority(priority));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchByTitle(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(taskService.searchByTitle(keyword));
    }

    @GetMapping("/due")
    public ResponseEntity<List<Task>> getByDueDate(@RequestParam("date") String date) {
        return ResponseEntity.ok(taskService.findByDueDate(date));
    }
}

