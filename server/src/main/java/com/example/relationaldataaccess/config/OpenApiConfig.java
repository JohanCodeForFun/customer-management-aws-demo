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

    @Value("${server.url:}")
    private String serverUrl;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customerManagementOpenAPI() {
        // Determine the server URL
        String apiServerUrl;
        
        if (serverUrl != null && !serverUrl.isEmpty()) {
            // Use configured server URL (for production deployment)
            apiServerUrl = serverUrl;
        } else {
            // Use localhost with configured port (for local development)
            apiServerUrl = "http://localhost:" + serverPort;
        }

        Server server = new Server()
                .url(apiServerUrl)
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