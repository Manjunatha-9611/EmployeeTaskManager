package com.employee.employeetaskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TaskDTO {
	
	
	@NotBlank(message = "Title is required")
	private String title;
	
	@NotBlank(message = "Description is required")
	private String description;
	
	@NotBlank(message = "Status is required")
	private String status;
	
	@NotNull(message = "Employee id is required")
	@Positive(message = "Employee id must be greater than 0")
	private Integer employeeId;
	
	public TaskDTO(){
	}

	public TaskDTO(String title,String description, String status,Integer employeeId) {
		this.title = title;
		this.description = description;
		this.status = status;
		this.employeeId = employeeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	
	
}
