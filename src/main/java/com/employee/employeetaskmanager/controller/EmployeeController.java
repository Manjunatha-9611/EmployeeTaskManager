package com.employee.employeetaskmanager.controller;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employee.employeetaskmanager.dto.EmployeeDTO;
import com.employee.employeetaskmanager.entity.Employee;
import com.employee.employeetaskmanager.entity.Task;
import com.employee.employeetaskmanager.service.EmployeeService;
import com.employee.employeetaskmanager.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	private final TaskService taskService;

	public EmployeeController(EmployeeService employeeService,TaskService taskService) {
		this.employeeService = employeeService;
		this.taskService = taskService;
	}
	
	@PostMapping
	public Employee addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
		return employeeService.addEmployee(employeeDTO);
	}
	
	@GetMapping
	public List<Employee> getAllEmployee(){
		return employeeService.getAllEmployees();
	}
	
	
	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable int id) {
		return employeeService.getEmployeeById(id);
	}
	
	@GetMapping("/{id}/tasks")
	public List<Task> getEmployeeTasks(@PathVariable("id") int id){		
		return taskService.getTasksByEmployeeId(id);
	}
	
	@GetMapping("/department/{department}")
	public List<Employee> getEmployeesByDepartment(@PathVariable String department){
		return employeeService.getEmployeesByDepartment(department);
	}
	
	@GetMapping("/salary/above/{salary}")
	public List<Employee> getEmployeesAboveSalary(@PathVariable double salary){
		return employeeService.getEmployeeAboveSalary(salary);
	}
	
	@GetMapping("/page")
	public Page<Employee> getEmployees(Pageable pageable){
		return employeeService.getEmployees(pageable);
	}
	
	@GetMapping("/salary/descending")
	public List<Employee> getEmployeesBySalaryDescending(){
		return employeeService.getEmployeesBySalaryDescending();
	}
	
	
	@GetMapping("/filter")
	public List<Employee> getEmployeesByDepartmentAndSalary(@RequestParam String department, @RequestParam double salary){
		return employeeService.getEmployeesByDepartmentAndSalary(department, salary);
	}
	
	@PutMapping("/{id}")
	public Employee updateEmployee(@PathVariable int id, @Valid @RequestBody EmployeeDTO employeeDTO) {
		return employeeService.updateEmployee(id, employeeDTO);
	}
	
	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable int id) {
		employeeService.deleteEmployee(id);
		return "Employee deleted successfully";
	}
	
	
	
}
