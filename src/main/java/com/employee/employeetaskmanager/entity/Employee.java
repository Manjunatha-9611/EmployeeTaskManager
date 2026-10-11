package com.employee.employeetaskmanager.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

//basically this class represents our mysql table in database
@Entity // ->hibernate knows that this should become table in database
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // autogeneration logic
	private Integer id;

	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String department;
	private double salary;

	@OneToMany(mappedBy = "employee")
	@JsonIgnore
	private List<Task> tasks;

	public Employee() {
	}

	public Employee(Integer id, String name, String email, String department, double salary) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.department = department;
		this.salary = salary;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
