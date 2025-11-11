package com.example.relationaldataaccess;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer entity representing a customer in the system")
public class Customer {
	@Schema(description = "Unique identifier for the customer", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	private long id;
	
	@Schema(description = "Customer's first name", example = "John", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
	private String firstName;
	
	@Schema(description = "Customer's last name", example = "Doe", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
	private String lastName;

	public Customer(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// Default constructor
	public Customer() {}

	@Override
	public String toString() {
		return String.format(
				"Customer[id=%d, firstName='%s', lastName='%s']",
				id, firstName, lastName);
	}

	// Getters and setters
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
}
