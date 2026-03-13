package com.example.splunk.service;

import com.example.splunk.model.Employee;
import com.example.splunk.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.apache. logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import java.util.List;

@Service
public class EmployeeService {

	private static final Logger logger = LogManager.getLogger(EmployeeService.class);

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public Employee create(Employee employee) {
		logger.info("Creating employee: {}", employee);
		employee.setId(null);
		Employee created = employeeRepository.save(employee);
		logger.info("Employee created with id: {}", created.getId());
		return created;
	}

	public List<Employee> getAll() {
		logger.info("Retrieving all employees");
		return employeeRepository.findAll();
	}

	public Employee getById(long id) {
		logger.info("Retrieving employee with id: {}", id);
		return employeeRepository
				.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + id));
	}

	public Employee update(long id, Employee updated) {
		logger.info("Updating employee with id: {}", id);
		Employee existing = getById(id);
		existing.setFirstName(updated.getFirstName());
		existing.setLastName(updated.getLastName());
		existing.setEmail(updated.getEmail());
		Employee saved = employeeRepository.save(existing);
		logger.info("Employee updated with id: {}", id);
		return saved;
	}

	public void delete(long id) {
		logger.info("Deleting employee with id: {}", id);
		if (!employeeRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + id);
		}
		employeeRepository.deleteById(id);
		logger.info("Employee deleted with id: {}", id);
	}
}
