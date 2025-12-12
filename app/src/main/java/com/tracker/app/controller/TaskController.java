package com.tracker.app.controller;

import com.tracker.app.entity.Task;
import com.tracker.app.service.Taskservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private Taskservice taskservice;
    @GetMapping("/task")
    public String listTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            Model model) {

        List<Task> tasks;
        if (keyword != null && !keyword.trim().isEmpty()) {
            tasks = taskservice.searchByTitle(keyword);
        }
        else if (status != null && !status.trim().isEmpty()) {
            tasks = taskservice.findByStatus(status);
        }
        else if (priority != null && !priority.trim().isEmpty()) {
            tasks = taskservice.findByPriority(priority);
        }
        else if (sort != null && !sort.trim().isEmpty()) {
            tasks = taskservice.sortByField(sort);
        }
        else {
            tasks = taskservice.getAllTasks();
        }
        model.addAttribute("tasks", tasks);
        return "tasks";
    }




    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("task", new Task());
        return "add-task";
    }

    @PostMapping("/add")
    public String saveTask(@ModelAttribute Task task, Model model){
        if(task.getTitle()==null || task.getTitle().trim().isEmpty()){
            model.addAttribute("errorMessage", "Title is required");
            model.addAttribute("task", task);
            return "add-task";
        }
        if(task.getDueDate()==null || task.getDueDate().trim().isEmpty()){
            model.addAttribute("errorMessage", "Due Date is required");
            model.addAttribute("task", task);
            return "add-task";
        }


        task.setCreatedAt(LocalDateTime.now());
        //task.setId(Taskservice.nextId());
        taskservice.addTask(task);
        return "redirect:/api/tasks/task";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model){
        Task task = taskservice.findById(id).orElseThrow(() ->new RuntimeException("Task not found"));


        model.addAttribute("task", task);
        return "edit-task";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Integer id, @ModelAttribute Task task,Model model){
        if(task.getTitle()==null || task.getTitle().trim().isEmpty()){
            model.addAttribute("errorMessage", "Title is required");
            model.addAttribute("task", task);
            return "add-task";
        }
        if(task.getDueDate()==null || task.getDueDate().trim().isEmpty()){
            model.addAttribute("errorMessage", "Due Date is required");
            model.addAttribute("task", task);
            return "add-task";
        }
        Task existing = taskservice.findById(id).orElse(null);
        task.setId(id);
        task.setCreatedAt(existing.getCreatedAt());
        taskservice.updateTask(task);
        return "redirect:/api/tasks/task";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Integer id, Model model){
        if(!taskservice.findById(id).isPresent()){
            model.addAttribute("errorMessage","Task not found");
            return "tasks";
        }
        taskservice.deleteTask(id);
        return "redirect:/api/tasks/task";
    }
    @GetMapping("/done/{id}")
    public String markTaskDone(@PathVariable Integer id){
        Task task = taskservice.findById(id).orElse(null);
        if(task != null){
            task.setStatus("Done");
            taskservice.updateTask(task);
        }
        return "redirect:/api/tasks/task";
    }

}
