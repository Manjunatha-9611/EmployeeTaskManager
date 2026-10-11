package com.employee.employeetaskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class EmployeeDTO {
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message = "Email is required")
	@Email(message ="Enter a valid email")
	private String email;
	
	@NotBlank(message = "Department is required")
	private String department;
	
	@Positive(message = "Salary must be greater than 0")
	private double salary;
	
	public EmployeeDTO() {
	}

	public EmployeeDTO(String name, String email, String department, double salary) {
		this.name = name;
		this.email = email;
		this.department = department;
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	

}
