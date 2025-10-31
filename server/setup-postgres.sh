#!/bin/bash

# PostgreSQL Setup Script for the Customer Application

echo "Setting up PostgreSQL for Customer Application..."

# Check if PostgreSQL is running
if ! pg_isready -h localhost -p 5432; then
    echo "PostgreSQL is not running. Please start Postgres.app from your Applications folder."
    echo "Then run this script again."
    exit 1
fi

# Create database (this assumes you have a default postgres user)
echo "Creating customerdb database..."
createdb -h localhost -U $USER customerdb 2>/dev/null || echo "Database may already exist"

# Test connection
echo "Testing database connection..."
psql -h localhost -U $USER -d customerdb -c "SELECT version();" || {
    echo "Failed to connect to database. Please check your PostgreSQL setup."
    exit 1
}

echo "PostgreSQL setup complete!"
echo "Database: customerdb"
echo "Host: localhost:5432"
echo "Username: $USER"
echo ""
echo "You can now run the Spring Boot application with: ./mvnw spring-boot:run"