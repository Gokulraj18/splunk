package com.example.splunk.controller;

import com.example.splunk.model.Employee;
import com.example.splunk.service.EmployeeService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private static final Logger logger = LogManager.getLogger(EmployeeController.class);

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
		logger.info("Creating employee: {}", employee);
		Employee created = employeeService.create(employee);
		logger.info("Employee created with id: {}", created.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public List<Employee> getAll() {
		logger.info("Retrieving all employees");
		return employeeService.getAll();
	}

	@GetMapping("/{id}")
	public Employee getById(@PathVariable long id) {
		logger.info("Retrieving employee with id: {}", id);
		return employeeService.getById(id);
	}

	@PutMapping("/{id}")
	public Employee update(@PathVariable long id, @Valid @RequestBody Employee employee) {
		logger.info("Updating employee with id: {}", id);
		return employeeService.update(id, employee);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		logger.info("Deleting employee with id: {}", id);
		employeeService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
