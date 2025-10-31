#!/bin/bash

# Local Development Setup Script
set -e

echo "🚀 Setting up Customer Management System for local development..."

# Check prerequisites
echo "📋 Checking prerequisites..."

if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 17+"
    exit 1
fi

if ! command -v node &> /dev/null; then
    echo "❌ Node.js not found. Please install Node.js 18+"
    exit 1
fi

if ! command -v psql &> /dev/null; then
    echo "❌ PostgreSQL not found. Please install PostgreSQL"
    echo "   macOS: brew install postgresql"
    echo "   Ubuntu: sudo apt-get install postgresql"
    exit 1
fi

echo "✅ Prerequisites check passed"

# Setup database
echo "🗄️ Setting up database..."
echo "Creating database and user (you may be prompted for PostgreSQL password)..."

psql postgres -c "CREATE DATABASE IF NOT EXISTS customerdb;" 2>/dev/null || true
psql postgres -c "CREATE USER customeruser WITH PASSWORD 'customerpass';" 2>/dev/null || true
psql postgres -c "GRANT ALL PRIVILEGES ON DATABASE customerdb TO customeruser;" 2>/dev/null || true

echo "✅ Database setup complete"

# Install frontend dependencies
echo "📦 Installing frontend dependencies..."
cd client
npm install
cd ..

echo "✅ Frontend dependencies installed"

# Build backend
echo "🔨 Building backend..."
cd server
./mvnw clean compile
cd ..

echo "✅ Backend build complete"

echo "🎉 Setup complete!"
echo ""
echo "To start the application:"
echo "1. Start backend:  cd server && ./mvnw spring-boot:run"
echo "2. Start frontend: cd client && npm run dev"
echo ""
echo "URLs:"
echo "- Frontend: http://localhost:5173"
echo "- Backend:  http://localhost:8080"
echo "- API:      http://localhost:8080/api/customers"
echo "- Health:   http://localhost:8080/api/health"