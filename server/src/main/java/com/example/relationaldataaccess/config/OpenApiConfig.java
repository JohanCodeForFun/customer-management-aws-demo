package com.example.relationaldataaccess.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    @Bean
    public OpenAPI customerManagementOpenAPI() {
        // Parse the first allowed origin for the server URL
        String serverUrl = allowedOrigins.split(",")[0].trim();
        
        // For local development, use backend URL
        if (serverUrl.contains("localhost:5173") || serverUrl.contains("localhost:5174")) {
            serverUrl = "http://localhost:8080";
        }

        Server server = new Server()
                .url(serverUrl)
                .description("Customer Management API Server");

        Contact contact = new Contact()
                .name("Customer Management Team")
                .email("support@customerapp.com")
                .url("https://github.com/JohanCodeForFun/customer-management-aws-demo");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Customer Management API")
                .description("""
                    A comprehensive REST API for managing customer data with full CRUD operations.
                    
                    ## Features
                    - **Customer CRUD Operations**: Create, Read, Update, Delete customers
                    - **Search Functionality**: Search customers by name (case-insensitive)
                    - **Health Monitoring**: System health and connectivity checks
                    - **Input Validation**: Automatic sanitization and validation
                    - **CORS Support**: Cross-origin resource sharing enabled
                    
                    ## Authentication
                    Currently no authentication required. In production, implement proper authentication.
                    
                    ## Error Handling
                    All endpoints return appropriate HTTP status codes and error messages.
                    """)
                .version("1.0.0")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}