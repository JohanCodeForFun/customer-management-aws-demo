package com.example.relationaldataaccess.controller;

import com.example.relationaldataaccess.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {
    "http://localhost:5173", 
    "http://localhost:5174",
    "${cors.allowed-origins:*}"
}) // Support local development and production
public class CustomerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
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

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
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
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
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