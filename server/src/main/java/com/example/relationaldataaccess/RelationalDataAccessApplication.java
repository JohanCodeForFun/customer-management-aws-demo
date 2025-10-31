package com.example.relationaldataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class RelationalDataAccessApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RelationalDataAccessApplication.class);

	public static void main(String args[]) {
		SpringApplication.run(RelationalDataAccessApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/api/health")
	@CrossOrigin(origins = {
		"http://localhost:5173", 
		"http://localhost:5174",
		"${cors.allowed-origins:*}"
	})
	public String health() {
		return "Customer Management API is running!";
	}

	@Override
	public void run(String... strings) throws Exception {

		log.info("Initializing database tables and sample data...");

		jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
		jdbcTemplate.execute("CREATE TABLE customers(" +
				"id SERIAL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255))");

		// Split up the array of whole names into an array of first/last names
		List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
				.map(name -> name.split(" "))
				.collect(Collectors.toList());

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

		log.info("Database initialized with {} customers", splitUpNames.size());
		log.info("API available at http://localhost:8080/api/customers");
		log.info("Health check at http://localhost:8080/api/health");
	}
}
