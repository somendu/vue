package com.example.springposgres.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "employee")
@Data
public class Employee implements Serializable {

	private static final long serialVersionUID = -23432432431313341L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long emp_id;

	@Column(name = "name")
	private String name;

	@Column(name = "dept")
	private String dept;

	protected Employee() {
	}

	public Employee(String name, String dept) {
		this.name = name;
		this.dept = dept;
	}

	@Override
	public String toString() {
		return String.format("Customer[id=%d, firstName='%s', lastName='%s']", emp_id, name, dept);
	}

}