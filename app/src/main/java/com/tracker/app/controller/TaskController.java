package com.tracker.app.controller;
import com.tracker.app.entity.Task;
import com.tracker.app.enums.TaskPriority;
import com.tracker.app.enums.TaskStatus;
import com.tracker.app.service.Taskservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private Taskservice taskservice;

    @GetMapping("/task")
    public String listTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String keyword,

            Model model) {



        Pageable pageable= PageRequest.of(page,size);
        Page<Task> taskPage=taskservice.getPagedTasks(pageable, status, priority, keyword);
        model.addAttribute("taskpage",taskPage);
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("size", size);
        int TotalPage=taskPage.getTotalPages();
        List<Integer> pageNumbers = IntStream.range(0, TotalPage).boxed().toList();
        model.addAttribute("pageNumbers", pageNumbers);
        return "tasks";

    }

    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("task", new Task());
        return "add-task";
    }

    @PostMapping("/add")
    public String saveTask(@ModelAttribute Task task, Model model){

        if(task.getTitle() == null || task.getTitle().trim().isEmpty()){
            model.addAttribute("errorMessage", "Title is required");
            model.addAttribute("task", task);
            return "add-task";
        }

        if(task.getDueDate() == null || task.getDueDate().trim().isEmpty()){
            model.addAttribute("errorMessage", "Due Date is required");
            model.addAttribute("task", task);
            return "add-task";
        }

        if(task.getStatus() == null){
            task.setStatus(TaskStatus.PENDING);
        }

        task.setCreatedAt(LocalDateTime.now());
        taskservice.addTask(task);

        return "redirect:/api/tasks/task";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model){
        Task task = taskservice.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        model.addAttribute("task", task);
        return "edit-task";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Integer id,
                             @ModelAttribute Task task,
                             Model model){

        if(task.getTitle() == null || task.getTitle().trim().isEmpty()){
            model.addAttribute("errorMessage", "Title is required");
            model.addAttribute("task", task);
            return "edit-task";
        }

        if(task.getDueDate() == null || task.getDueDate().trim().isEmpty()){
            model.addAttribute("errorMessage", "Due Date is required");
            model.addAttribute("task", task);
            return "edit-task";
        }

        Task existing = taskservice.findById(id).orElse(null);
        task.setId(id);
        task.setCreatedAt(existing.getCreatedAt());

        taskservice.updateTask(task);
        return "redirect:/api/tasks/task";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Integer id){
        taskservice.deleteTask(id);
        return "redirect:/api/tasks/task";
    }

    @GetMapping("/done/{id}")
    public String markTaskDone(@PathVariable Integer id){

        Task task = taskservice.findById(id).orElse(null);
        if (task != null && task.getStatus() == TaskStatus.IN_PROGRESS) {

            task.setStatus(TaskStatus.DONE);
            task.setCompletedAt(LocalDateTime.now());
            taskservice.updateTask(task);
        }

        return "redirect:/api/tasks/task";
    }


    @GetMapping("/view/{id}")
    public String viewTask(@PathVariable Integer id, Model model, RedirectAttributes ra){

        Task task=taskservice.findById(id).orElse(null);
        if(task==null){
            ra.addFlashAttribute("errorMessage", "Task not Found");
            return "redirect:/api/tasks/task";
        }
        model.addAttribute("task",task);
        return "view-task";
    }
}
