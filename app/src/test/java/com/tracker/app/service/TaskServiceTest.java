package com.tracker.app.service;

import com.tracker.app.entity.Task;
import com.tracker.app.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskserviceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private Taskservice taskservice;

    @Test
    void testGetAllTasks() {

        List<Task> mockTasks = Arrays.asList(new Task(), new Task());

        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<Task> result = taskservice.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testAddTask() {

        Task task = new Task();
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskservice.addTask(task);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testFindById() {

        Task task = new Task();
        task.setId(5);

        when(taskRepository.findById(5)).thenReturn(Optional.of(task));

        Optional<Task> result = taskservice.findById(5);

        assertTrue(result.isPresent());
        verify(taskRepository, times(1)).findById(5);
    }

    @Test
    void testUpdateTask() {

        Task task = new Task();
        task.setId(3);

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskservice.updateTask(task);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteTask() {

        taskservice.deleteTask(2);

        verify(taskRepository, times(1)).deleteById(2);
    }
}

