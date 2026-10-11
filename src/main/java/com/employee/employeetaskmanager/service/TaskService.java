package com.employee.employeetaskmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.employee.employeetaskmanager.dto.TaskDTO;
import com.employee.employeetaskmanager.dto.TaskStatusDTO;
import com.employee.employeetaskmanager.entity.Employee;
import com.employee.employeetaskmanager.entity.Task;
import com.employee.employeetaskmanager.exception.EmployeeNotFoundException;
import com.employee.employeetaskmanager.exception.TaskNotFoundException;
import com.employee.employeetaskmanager.repository.EmployeeRepository;
import com.employee.employeetaskmanager.repository.TaskRepository;

@Service
public class TaskService {
	
	private final TaskRepository taskRepository;
	private final EmployeeRepository employeeRepository;

	public TaskService(TaskRepository taskRepository, EmployeeRepository employeeRepository) {
		this.taskRepository = taskRepository;
		this.employeeRepository = employeeRepository;
	}

	public Task addTask(TaskDTO taskDTO) {
		
		Employee employee = employeeRepository.findById(taskDTO.getEmployeeId()).orElseThrow(()-> new EmployeeNotFoundException("Employee with id "+taskDTO.getEmployeeId()+" not found"));
		Task task = new Task();
		task.setTitle(taskDTO.getTitle());
		task.setDescription(taskDTO.getDescription());
		task.setStatus(taskDTO.getStatus());
		task.setEmployee(employee);
		
		return taskRepository.save(task);
	}
	
	public Task updateTask(int id, TaskDTO taskDTO) {
		Task existingTask = taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task with id "+id+" not found"));
		
		Employee employee = employeeRepository.findById(taskDTO.getEmployeeId()).orElseThrow(()->new EmployeeNotFoundException("Employee with id "+taskDTO.getEmployeeId()+" not found"));
		existingTask.setDescription(taskDTO.getDescription());
		existingTask.setStatus(taskDTO.getStatus());
		existingTask.setTitle(taskDTO.getTitle());
		existingTask.setEmployee(employee);
		
		return taskRepository.save(existingTask);
	}
	
	public boolean deleteTask(int id) {
		if(!taskRepository.existsById(id))
			throw new TaskNotFoundException("Task with id "+id+" not found");
		
		
		taskRepository.deleteById(id);
		return true;
	}
	
	public List<Task> getAllTasks(){
		return taskRepository.findAll();
	}

	public Task getTaskById(int id) {
		return taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task with id "+id+" not found"));
	}
	
	public List<Task> getTasksByEmployeeId(Integer employeeId){
		
		employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException("Employee with id "+employeeId+" not found"));
		return taskRepository.findByEmployeeId(employeeId);
	}
	
	public List<Task> getTaskByStatus(String status){
		return taskRepository.findByStatus(status);
				
	}
	
	public Task updateTaskStatus(int id,TaskStatusDTO statusDTO) {
		Task task = taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task with id "+id+" not found"));
		
		task.setStatus(statusDTO.getStatus());
		
		return taskRepository.save(task);
	}
	
}
