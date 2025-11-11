package com.example.relationaldataaccess.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.relationaldataaccess.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:5173,http://localhost:5174}")
@Tag(name = "Customer Management", description = "REST API for managing customer data with full CRUD operations and search functionality")
public class CustomerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Operation(
        summary = "Get all customers", 
        description = "Retrieve a complete list of all customers in the system, ordered by ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved customers",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = Customer.class),
                examples = @ExampleObject(
                    name = "Customer list example",
                    value = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"},{\"id\":2,\"firstName\":\"Jane\",\"lastName\":\"Smith\"}]"
                )
            )
        )
    })
    @GetMapping
    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers ORDER BY id",
                (rs, rowNum) -> new Customer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                )
        );
    }

    @Operation(
        summary = "Get customer by ID", 
        description = "Retrieve a specific customer by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Customer found",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = Customer.class),
                examples = @ExampleObject(
                    name = "Customer example",
                    value = "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Customer not found with the specified ID"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
        @Parameter(description = "Unique identifier of the customer", required = true, example = "1")
        @PathVariable Long id) {
        List<Customer> customers = jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE id = ?",
                (rs, rowNum) -> new Customer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ),
                id
        );
        
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(customers.get(0));
    }

    @Operation(
        summary = "Create a new customer", 
        description = "Create a new customer with the provided first name and last name. Names are automatically sanitized and validated for security."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Customer successfully created",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = Customer.class),
                examples = @ExampleObject(
                    name = "Created customer example",
                    value = "{\"id\":3,\"firstName\":\"John\",\"lastName\":\"Doe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid customer data provided (empty names, null data, etc.)"
        )
    })
    @PostMapping
    public Customer createCustomer(
        @Parameter(description = "Customer data with firstName and lastName", required = true)
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Customer object with first name and last name",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Customer.class),
                examples = @ExampleObject(
                    name = "New customer example",
                    value = "{\"firstName\":\"John\",\"lastName\":\"Doe\"}"
                )
            )
        )
        @RequestBody Customer customer) {
        // Input validation and sanitization
        if (customer == null) {
            throw new IllegalArgumentException("Customer data cannot be null");
        }
        
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        
        // Validate and sanitize first name
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        
        // Sanitize input: remove dangerous characters and limit length
        firstName = firstName.trim()
                .replaceAll("[<>\"'%;()&+]", "")
                .substring(0, Math.min(firstName.trim().length(), 50));
        lastName = lastName.trim()
                .replaceAll("[<>\"'%;()&+]", "")
                .substring(0, Math.min(lastName.trim().length(), 50));
        
        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("Names cannot be empty after sanitization");
        }
        
        jdbcTemplate.update(
                "INSERT INTO customers(first_name, last_name) VALUES (?, ?)",
                firstName,
                lastName
        );
        
        // Get the newly created customer
        List<Customer> customers = jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ? AND last_name = ? ORDER BY id DESC LIMIT 1",
                (rs, rowNum) -> new Customer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ),
                firstName,
                lastName
        );
        
        return customers.get(0);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a customer",
        description = "Deletes a customer by their unique ID. Returns 200 OK if the customer was successfully deleted, or 404 Not Found if no customer exists with the specified ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer successfully deleted"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found with the specified ID",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteCustomer(
        @Parameter(
            description = "The unique identifier of the customer to delete",
            required = true,
            example = "1"
        )
        @PathVariable Long id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM customers WHERE id = ?", id);
        
        if (rowsAffected > 0) {
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String name) {
        // Input validation and sanitization
        if (name == null || name.trim().isEmpty()) {
            return List.of(); // Return empty list for invalid input
        }
        
        // Sanitize input: remove potentially dangerous characters and limit length
        String sanitizedName = name.trim()
                .replaceAll("[<>\"'%;()&+]", "") // Remove potential SQL injection characters
                .substring(0, Math.min(name.trim().length(), 50)); // Limit to 50 characters
        
        if (sanitizedName.isEmpty()) {
            return List.of(); // Return empty list if nothing remains after sanitization
        }
        
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name ILIKE ? OR last_name ILIKE ? ORDER BY id",
                (rs, rowNum) -> new Customer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ),
                "%" + sanitizedName + "%",
                "%" + sanitizedName + "%"
        );
    }
}