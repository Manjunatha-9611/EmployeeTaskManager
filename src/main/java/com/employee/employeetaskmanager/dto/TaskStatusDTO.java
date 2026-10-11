package com.employee.employeetaskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskStatusDTO {

	@NotBlank(message = "Status is required")
	private String status;
	
	public TaskStatusDTO() {
	}

	public TaskStatusDTO(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
