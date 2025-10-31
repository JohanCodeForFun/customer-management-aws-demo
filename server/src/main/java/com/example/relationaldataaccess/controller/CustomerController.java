package com.example.relationaldataaccess.controller;

import com.example.relationaldataaccess.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}) // Support both Vite dev server ports
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
        jdbcTemplate.update(
                "INSERT INTO customers(first_name, last_name) VALUES (?, ?)",
                customer.getFirstName(),
                customer.getLastName()
        );
        
        // Get the newly created customer
        List<Customer> customers = jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ? AND last_name = ? ORDER BY id DESC LIMIT 1",
                (rs, rowNum) -> new Customer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ),
                customer.getFirstName(),
                customer.getLastName()
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
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name ILIKE ? OR last_name ILIKE ? ORDER BY id",
                (rs, rowNum) -> new Customer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ),
                "%" + name + "%",
                "%" + name + "%"
        );
    }
}