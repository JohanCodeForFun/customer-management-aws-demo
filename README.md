# Customer Management AWS Demo

A Spring Boot application demonstrating how to connect to PostgreSQL locally and deploy to AWS RDS with a React frontend.

## Project Structure

```
customer-management-aws/
├── server/                     # Spring Boot API
│   ├── src/main/java/...      # Java source code
│   ├── src/main/resources/    # Configuration files
│   ├── pom.xml               # Maven dependencies
│   └── README.md             # Server-specific documentation
├── .github/workflows/         # CI/CD pipelines
├── LICENSE.txt               # Apache 2.0 License
└── README.md                 # This file
```

## Features

- ✅ Spring Boot 3.3.0 with Java 17+
- ✅ PostgreSQL integration (local and AWS RDS)
- ✅ JDBC Template for database operations
- 🚧 REST API endpoints (planned)
- 🚧 React frontend (planned)
- 🚧 AWS deployment with Elastic Beanstalk (planned)

## Quick Start

### Prerequisites

- Java 17 or higher
- PostgreSQL (local installation)
- Maven (included via wrapper)

### Run Locally

1. **Clone the repository**:
   ```bash
   git clone https://github.com/JohanCodeForFun/customer-management-aws-demo.git
   cd customer-management-aws-demo
   ```

2. **Start PostgreSQL** and create database:
   ```bash
   cd server
   ./setup-postgres.sh
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **View the data**:
   ```bash
   psql -h localhost -U $USER -d customerdb
   SELECT * FROM customers;
   ```

## Development Roadmap

### Phase 1: Local Development ✅
- [x] Spring Boot application with PostgreSQL
- [x] Basic customer data model
- [x] Database setup scripts

### Phase 2: REST API 🚧
- [ ] Convert to REST API with Spring Web
- [ ] Add Customer controller with CRUD operations
- [ ] Add data validation and error handling
- [ ] Add Swagger/OpenAPI documentation

### Phase 3: Frontend 🚧
- [ ] Create React application
- [ ] Customer list and management UI
- [ ] API integration with Axios
- [ ] Responsive design

### Phase 4: AWS Deployment 🚧
- [ ] Configure for AWS RDS
- [ ] Deploy backend to Elastic Beanstalk
- [ ] Deploy frontend to AWS Amplify
- [ ] Set up CI/CD with GitHub Actions

## Technology Stack

**Backend:**
- Spring Boot 3.3.0
- Spring JDBC
- PostgreSQL
- Maven

**Frontend (Planned):**
- React 18
- Axios for API calls
- Material-UI or Tailwind CSS

**Infrastructure (Planned):**
- AWS RDS (PostgreSQL)
- AWS Elastic Beanstalk
- AWS Amplify
- GitHub Actions

## Contributing

This is a learning project demonstrating AWS integration patterns. Feel free to:
- Open issues for questions or suggestions
- Submit pull requests for improvements
- Fork the project for your own experiments

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.txt](LICENSE.txt) file for details.

## Acknowledgments

- Based on the Spring Guides [Accessing Relational Data](https://github.com/spring-guides/gs-relational-data-access) tutorial
- Spring Boot and Spring Framework teams
- AWS documentation and examples