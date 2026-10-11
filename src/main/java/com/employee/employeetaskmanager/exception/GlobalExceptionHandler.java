package com.employee.employeetaskmanager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleEmployeeNotFound(EmployeeNotFoundException exception) {
		Map<String, Object> response = new HashMap<String, Object>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", 404);
		response.put("error", "Employee Not Found");
		response.put("message", exception.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException exception) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<String> messages = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getDefaultMessage()).toList();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", 400);
		response.put("error", "Validation Failed");
		response.put("messages", messages);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleTaskNotFound(TaskNotFoundException exception){
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		response.put("timestamp", LocalDateTime.now());
		response.put("status",404);
		response.put("error","Task Not found");
		response.put("message", exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		response.put("timestamp", LocalDateTime.now());
		response.put("status", 409);
		response.put("error", "Email Already Exists");
		response.put("message", exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	@ExceptionHandler(EmployeeHasTasksException.class)
	public ResponseEntity<Map<String, Object>> handleEmployeeHasTasks(EmployeeHasTasksException exception) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		response.put("timestamp", LocalDateTime.now());
		response.put("status", 409);
		response.put("error", "Employee Cannot Be Deleted");
		response.put("message", exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	
}
