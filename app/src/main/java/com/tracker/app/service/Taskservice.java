package com.tracker.app.service;

import com.tracker.app.entity.Task;
import com.tracker.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Taskservice {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks(){

        return taskRepository.findAll();
    }

    public void addTask(Task task){
        taskRepository.save(task);
    }

    public Optional<Task> findById(Integer id){
        return taskRepository.findById(id);
    }

    public void updateTask(Task updated){
        taskRepository.save(updated);
    }

    public void deleteTask(Integer id){
        taskRepository.deleteById(id);
    }
}

