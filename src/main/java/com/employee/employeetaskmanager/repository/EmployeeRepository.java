package com.employee.employeetaskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.employeetaskmanager.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
		//JpaRepositoryy<Table,Type of the primary key of the table>
	
	boolean existsByEmail(String email);
	
	Employee findByEmail(String email);
	
	List<Employee> findByDepartment(String department);
	
	List<Employee> findBySalaryGreaterThan(double salary);
	
	List<Employee> findAllByOrderBySalaryDesc();
	
	List<Employee> findByDepartmentAndSalaryGreaterThan(String department,double salary);
}
