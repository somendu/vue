package com.example.springposgres.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springposgres.model.Customer;
import com.example.springposgres.model.CustomerUI;
import com.example.springposgres.model.Employee;
import com.example.springposgres.repository.CustomerRepository;
import com.example.springposgres.repository.EmployeeRepository;

@RestController
public class CustomerController {

	@Autowired
	CustomerRepository repository;

	@Autowired
	EmployeeRepository employeeRepo;

	@GetMapping("/bulkcreate")
	public String bulkcreate() {
		// save a single Customer
		repository.save(new Customer("Rajesh", "Bhojwani"));

		// save a list of Customers
		repository.saveAll(Arrays.asList(new Customer("Salim", "Khan"), new Customer("Rajesh", "Parihar"),
				new Customer("Rahul", "Dravid"), new Customer("Dharmendra", "Bhojwani")));

		return "Customers are created";
	}

	@GetMapping("/bulkcreateemployee")
	public String bulkcreateemployee() {
		// save a single Employee
		employeeRepo.save(new Employee("Rahul", "HR"));

		// Save All Employees
		employeeRepo.saveAll(Arrays.asList(new Employee("Nilay", "Arch"), new Employee("Dilip", "support"),
				new Employee("Kapil", ".net"), new Employee("Krishna", "Java")));

		return "Employees are created";
	}

	@PostMapping("/create")
	public String create(@RequestBody CustomerUI customer) {
		// save a single Customer
		repository.save(new Customer(customer.getFirstName(), customer.getLastName()));

		return "Customer is created";
	}

	@GetMapping("/findall")
	public List<CustomerUI> findAll() {

		List<Customer> customers = repository.findAll();
		List<CustomerUI> customerUI = new ArrayList<>();

		for (Customer customer : customers) {
			customerUI.add(new CustomerUI(customer.getFirstName(), customer.getLastName()));
		}

		return customerUI;
	}

	@RequestMapping("/search/{id}")
	public String search(@PathVariable long id) {
		String customer = "";
		customer = repository.findById(id).toString();
		return customer;
	}

	@RequestMapping("/searchbyfirstname/{firstname}")
	public List<CustomerUI> fetchDataByLastName(@PathVariable String firstname) {

		List<Customer> customers = repository.findByFirstName(firstname);
		List<CustomerUI> customerUI = new ArrayList<>();

		for (Customer customer : customers) {
			customerUI.add(new CustomerUI(customer.getFirstName(), customer.getLastName()));
		}

		return customerUI;
	}
}