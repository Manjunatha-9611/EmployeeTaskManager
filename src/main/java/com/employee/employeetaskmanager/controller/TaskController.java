package com.employee.employeetaskmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.employeetaskmanager.dto.TaskDTO;
import com.employee.employeetaskmanager.dto.TaskStatusDTO;
import com.employee.employeetaskmanager.entity.Task;
import com.employee.employeetaskmanager.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	
    private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
    
    @PostMapping
    public Task addTask(@Valid @RequestBody TaskDTO taskDTO) {
    	return taskService.addTask(taskDTO);
    }
    
    @GetMapping
    public List<Task> getAllTasks(){
    	return taskService.getAllTasks();
    }
    
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable int id) {
    	return taskService.getTaskById(id);
    }
    
    @GetMapping("/status/{status}")
    public List<Task> getTaskByStatus(@PathVariable String status){
    	return taskService.getTaskByStatus(status);
    }
    
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id, @Valid @RequestBody TaskDTO taskDTO) {
    	return taskService.updateTask(id, taskDTO);
    }
    
    
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable int id) {
    	taskService.deleteTask(id);
    	return "Task deleted successfully";
    }
    
    @PatchMapping("/{id}/status")
    public Task updateStatus(@PathVariable int id, @Valid @RequestBody TaskStatusDTO statusDTO) {
    	return taskService.updateTaskStatus(id, statusDTO);
    }
}
