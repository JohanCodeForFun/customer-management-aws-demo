# Spring Boot Customer Management with PostgreSQL

This application demonstrates how to connect a Spring Boot application to a local PostgreSQL database.

## Prerequisites

1. **PostgreSQL**: Install and start PostgreSQL (via Postgres.app, Homebrew, or other method)
2. **Java 17+**: Required for Spring Boot 3.3.0

## Setup Instructions

### 1. Start PostgreSQL

Make sure PostgreSQL is running on `localhost:5432`. If using Postgres.app, start it from Applications.

### 2. Create Database

Run the setup script to create the database:

```bash
./setup-postgres.sh
```

Or manually create the database:

```bash
createdb -h localhost -U $USER customerdb
```

### 3. Configure Database Connection

The application is configured to connect to:
- **Host**: localhost:5432
- **Database**: customerdb  
- **Username**: Your system username (default)
- **Password**: Empty (default for local development)

You can override these with environment variables:

```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application will:
1. Create a `customers` table
2. Insert sample customer data
3. Query customers with first name "Josh"
4. Display results in the console

## Database Schema

```sql
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);
```

## Sample Data

The application inserts these customers:
- John Woo
- Jeff Dean  
- Josh Bloch
- Josh Long

## Viewing Data

Connect to PostgreSQL to view the data:

```bash
psql -h localhost -U $USER -d customerdb
SELECT * FROM customers;
```

## Configuration Files

- `src/main/resources/application.properties` - PostgreSQL configuration
- `src/test/resources/application-test.properties` - H2 test configuration

## Next Steps

To extend this application:
1. Add REST API endpoints
2. Create a React frontend
3. Deploy to AWS RDS
4. Add more advanced queries and business logic