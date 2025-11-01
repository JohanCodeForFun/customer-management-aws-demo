package com.example.relationaldataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
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

	@Autowired
	private Environment environment;

	@Value("${cors.allowed-origins:http://localhost:5173,http://localhost:5174}")
	private String corsAllowedOrigins;

	public static void main(String args[]) {
		SpringApplication.run(RelationalDataAccessApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/api/health")
	@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:5173,http://localhost:5174}")
	public String health() {
		log.info("Health check called - Application is running on profile: {}", 
			String.join(",", environment.getActiveProfiles()));
		return "Customer Management API is running! Profile: " + String.join(",", environment.getActiveProfiles());
	}

	@GetMapping("/api/ping")
	@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:5173,http://localhost:5174}")
	public String ping() {
		return "pong";
	}

	@Override
	public void run(String... strings) throws Exception {
		String[] activeProfiles = environment.getActiveProfiles();
		boolean isProduction = Arrays.asList(activeProfiles).contains("production");
		
		log.info("Starting application with profiles: {}", String.join(",", activeProfiles));
		log.info("CORS allowed origins: {}", corsAllowedOrigins);
		
		if (isProduction) {
			log.info("Production mode: Skipping database initialization");
			log.info("Ensure your RDS database has the 'customers' table created");
			
			// Check if table exists in production
			try {
				Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Long.class);
				log.info("Connected to database successfully. Current customer count: {}", count);
			} catch (Exception e) {
				log.error("Database connection failed: {}", e.getMessage());
				log.error("Please ensure RDS database is accessible and 'customers' table exists");
				// Don't fail startup - let health checks handle it
			}
		} else {
			// Development mode: Initialize database
			log.info("Development mode: Initializing database tables and sample data...");
			
			try {
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
			} catch (Exception e) {
				log.error("Database initialization failed: {}", e.getMessage());
			}
		}
		
		log.info("Application started successfully");
		log.info("API available at /api/customers");
		log.info("Health check at /api/health");
	}
}
