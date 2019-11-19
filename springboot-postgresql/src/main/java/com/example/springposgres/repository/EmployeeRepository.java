package com.example.springposgres.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.springposgres.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	List<Employee> findByName(String name);

	List<Employee> findAll();
}