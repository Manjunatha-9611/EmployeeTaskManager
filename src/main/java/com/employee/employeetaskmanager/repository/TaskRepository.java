package com.employee.employeetaskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.employeetaskmanager.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer>{
	
	
	boolean existsByEmployeeId(Integer employeeId);
		
	List<Task> findByEmployeeId(Integer id);
	
	List<Task> findByStatus(String status);
}
