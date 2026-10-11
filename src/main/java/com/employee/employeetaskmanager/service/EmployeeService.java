package com.employee.employeetaskmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.employee.employeetaskmanager.dto.EmployeeDTO;
import com.employee.employeetaskmanager.entity.Employee;
import com.employee.employeetaskmanager.exception.EmailAlreadyExistsException;
import com.employee.employeetaskmanager.exception.EmployeeHasTasksException;
import com.employee.employeetaskmanager.exception.EmployeeNotFoundException;
import com.employee.employeetaskmanager.repository.EmployeeRepository;
import com.employee.employeetaskmanager.repository.TaskRepository;

@Service
public class EmployeeService {
	
	private final TaskRepository taskRepository;
	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository, TaskRepository taskRepository) {
		this.employeeRepository = employeeRepository;
		this.taskRepository = taskRepository;
	}
	
	public Employee addEmployee(EmployeeDTO employeeDTO) {
		
		if(employeeRepository.existsByEmail(employeeDTO.getEmail())) {
			throw new EmailAlreadyExistsException("Employee with email "+employeeDTO.getEmail()+" already exists");
		}
		Employee employee = new Employee();
		employee.setName(employeeDTO.getName());
		employee.setSalary(employeeDTO.getSalary());
		employee.setDepartment(employeeDTO.getDepartment());
		employee.setEmail(employeeDTO.getEmail());
		
		return employeeRepository.save(employee);
	}
	
	public List<Employee> getAllEmployees(){
		return employeeRepository.findAll();
	}
	
	public Employee getEmployeeById(int id) {
		return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("Employee with id "+id+" not found"));
	}
	
	public Employee updateEmployee(int id, EmployeeDTO employeeDTO) {
		Employee existingEmployee = employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("Employee with id "+id+" not found"));
		
		Employee employeeWithEmail = employeeRepository.findByEmail(employeeDTO.getEmail());
		
		if(employeeWithEmail != null && !employeeWithEmail.getId().equals(id)) {
			throw new EmailAlreadyExistsException("Employee with email "+employeeDTO.getEmail()+" already exists");
		}
		
		existingEmployee.setName(employeeDTO.getName());
		existingEmployee.setEmail(employeeDTO.getEmail());
		existingEmployee.setDepartment(employeeDTO.getDepartment());
		existingEmployee.setSalary(employeeDTO.getSalary());
		
		return employeeRepository.save(existingEmployee);
	}
	
	public boolean deleteEmployee(int id) {
		if(!employeeRepository.existsById(id)) 
			throw new EmployeeNotFoundException("Employee with id "+id+" not found");
		
		if(taskRepository.existsByEmployeeId(id)) {
			throw new EmployeeHasTasksException("Employee cannot be deleted because tasks are assigned to this employee");
		}
		employeeRepository.deleteById(id);
		return true;
	}
	
	public List<Employee> getEmployeesByDepartment(String department){
		return employeeRepository.findByDepartment(department);
	}
	
	public List<Employee> getEmployeeAboveSalary(double salary){
		return employeeRepository.findBySalaryGreaterThan(salary);
	}
	
	public List<Employee> getEmployeesBySalaryDescending(){
		return employeeRepository.findAllByOrderBySalaryDesc();
	}
	
	public Page<Employee> getEmployees(Pageable pageable){
		return employeeRepository.findAll(pageable);
	}
	
	public List<Employee> getEmployeesByDepartmentAndSalary(String department,double salary){
		return employeeRepository.findByDepartmentAndSalaryGreaterThan(department, salary);
	}
}
