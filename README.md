# Customer Management System - AWS Cloud Deployment

A complete full-stack application demonstrating modern cloud-native deployment patterns using Spring Boot, React, and AWS services with enterprise-grade security.

## 🏗️ Architecture

- **Frontend**: React 18 + TypeScript + Vite (deployed on AWS Amplify)
- **Backend**: Spring Boot 3.3.0 + Java 17 (deployed on AWS Elastic Beanstalk)
- **Database**: PostgreSQL (Local development + AWS RDS)
- **CI/CD**: GitHub Actions with automated testing and deployment
- **Security**: SQL injection protection, input validation, CORS configuration

## 📁 Project Structure

```
customer-management-aws-demo/
├── client/                     # React TypeScript Frontend
│   ├── src/
│   │   ├── components/        # React components (CustomerList, CustomerForm)
│   │   ├── services/          # API service layer
│   │   ├── types/             # TypeScript interfaces
│   │   └── styles/            # CSS styling
│   ├── .env.development       # Local environment variables
│   ├── .env.production        # Production environment variables
│   └── package.json           # Node.js dependencies
├── server/                     # Spring Boot API
│   ├── .ebextensions/         # AWS Elastic Beanstalk configuration
│   ├── src/main/java/         # Java source code
│   │   └── com/example/relationaldataaccess/
│   │       ├── Customer.java                    # Data model
│   │       ├── RelationalDataAccessApplication.java  # Main application
│   │       └── controller/
│   │           └── CustomerController.java      # REST API endpoints
│   ├── src/test/java/         # Security and unit tests
│   ├── application.properties          # Local configuration
│   ├── application-production.properties  # Production configuration
│   ├── Dockerfile             # Container configuration
│   └── pom.xml               # Maven dependencies
├── .github/workflows/         # CI/CD pipelines
│   └── deploy.yml            # Automated deployment workflow
├── amplify.yml               # AWS Amplify build configuration
├── DEPLOYMENT.md             # Comprehensive deployment guide
├── SECURITY.md               # Security documentation
├── setup.sh                  # Local development setup script
└── README.md                 # This file
```

## ✨ Features

### Backend (Spring Boot)

- ✅ **REST API**: Complete CRUD operations for customer management
- ✅ **Database Integration**: PostgreSQL with JDBC Template
- ✅ **Security**: SQL injection protection with parameterized queries
- ✅ **Input Validation**: Comprehensive input sanitization and validation
- ✅ **CORS**: Configured for local development and production
- ✅ **Health Checks**: API monitoring endpoint
- ✅ **Environment Configuration**: Development and production profiles

### Frontend (React + TypeScript)

- ✅ **Modern UI**: Responsive customer management interface
- ✅ **TypeScript**: Type-safe development with interfaces
- ✅ **Component Architecture**: Reusable React components
- ✅ **API Integration**: Axios-based service layer
- ✅ **Real-time Updates**: Live data synchronization
- ✅ **Error Handling**: User-friendly error messages

### DevOps & Deployment

- ✅ **Containerization**: Docker support with security best practices
- ✅ **CI/CD Pipeline**: GitHub Actions for automated deployment
- ✅ **AWS Ready**: Configured for Amplify, Elastic Beanstalk, and RDS
- ✅ **Environment Management**: Separate configs for dev/prod
- ✅ **Security Testing**: Automated security validation

## 🚀 Quick Start

### Prerequisites

- **Java 17+** (for backend development)
- **Node.js 18+** (for frontend development)
- **PostgreSQL 14+** (for local database)
- **Git** (for version control)

### Automated Setup

Run the setup script to configure everything automatically:

```bash
# Clone and setup
git clone https://github.com/JohanCodeForFun/customer-management-aws-demo.git
cd customer-management-aws-demo
chmod +x setup.sh
./setup.sh
```

### Manual Setup

1. **Clone the repository**:

   ```bash
   git clone https://github.com/JohanCodeForFun/customer-management-aws-demo.git
   cd customer-management-aws-demo
   ```

2. **Setup PostgreSQL database**:

   ```bash
   # macOS with Homebrew
   brew install postgresql
   brew services start postgresql

   # Create database and user
   psql postgres -c "CREATE DATABASE customerdb;"
   psql postgres -c "CREATE USER customeruser WITH PASSWORD 'customerpass';"
   psql postgres -c "GRANT ALL PRIVILEGES ON DATABASE customerdb TO customeruser;"
   ```

3. **Start the backend**:

   ```bash
   cd server
   ./mvnw spring-boot:run
   ```

4. **Start the frontend** (in a new terminal):
   ```bash
   cd client
   npm install
   npm run dev
   ```

### Access the Application

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api/customers
- **Health Check**: http://localhost:8080/api/health

## 🔧 API Endpoints

### Customer Management

- `GET /api/customers` - List all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `DELETE /api/customers/{id}` - Delete customer
- `GET /api/customers/search?name={name}` - Search customers by name

### System

- `GET /api/health` - API health check

### Example API Usage

```bash
# Get all customers
curl http://localhost:8080/api/customers

# Create a new customer
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"firstName": "John", "lastName": "Doe"}'

# Search customers
curl "http://localhost:8080/api/customers/search?name=John"

# Delete customer
curl -X DELETE http://localhost:8080/api/customers/1
```

## 🛡️ Security Features

- **SQL Injection Protection**: Parameterized queries with input validation
- **Input Sanitization**: Character filtering and length limits
- **CORS Configuration**: Secure cross-origin resource sharing
- **Environment-based Security**: Production-ready configurations
- **Docker Security**: Non-root user containers

For detailed security information, see [SECURITY.md](SECURITY.md).

## ☁️ AWS Cloud Deployment

This application is configured for deployment to AWS using:

- **AWS Amplify**: Frontend hosting with CDN
- **AWS Elastic Beanstalk**: Backend auto-scaling and load balancing
- **AWS RDS**: Managed PostgreSQL database
- **GitHub Actions**: Automated CI/CD pipeline

For step-by-step deployment instructions, see [DEPLOYMENT.md](DEPLOYMENT.md).

### Quick Deploy to AWS

1. Set up AWS infrastructure (RDS, Elastic Beanstalk, Amplify)
2. Configure GitHub repository secrets
3. Push to main branch to trigger deployment

```bash
git push origin main  # Triggers automatic deployment
```

## 🛠️ Technology Stack

### Backend

- **Spring Boot 3.3.0** - Application framework
- **Spring Web** - REST API development
- **Spring JDBC** - Database access layer
- **PostgreSQL** - Primary database
- **JUnit 5** - Testing framework
- **Maven** - Build and dependency management

### Frontend

- **React 18** - UI library
- **TypeScript** - Type-safe JavaScript
- **Vite** - Build tool and development server
- **Axios** - HTTP client for API calls
- **CSS3** - Styling and responsive design

### DevOps & Infrastructure

- **Docker** - Containerization
- **AWS Amplify** - Frontend hosting and CI/CD
- **AWS Elastic Beanstalk** - Backend deployment and scaling
- **AWS RDS** - Managed PostgreSQL database
- **GitHub Actions** - Continuous integration and deployment

## 🧪 Development & Testing

### Running Tests

```bash
# Backend tests (includes security tests)
cd server
./mvnw test

# Frontend tests
cd client
npm test
```

### Development Workflow

1. **Feature Development**: Create feature branch from `main`
2. **Local Testing**: Test both frontend and backend locally
3. **Pull Request**: Submit PR for code review
4. **Automated Testing**: GitHub Actions runs tests
5. **Deployment**: Merge to `main` triggers deployment

### Debugging

```bash
# View backend logs
cd server
./mvnw spring-boot:run --debug

# View frontend development server
cd client
npm run dev -- --debug
```

## 🤝 Contributing

This project demonstrates modern full-stack development with AWS cloud deployment. Contributions are welcome!

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines

- Follow existing code style and patterns
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

### Issues and Feedback

- 🐛 **Bug Reports**: Use GitHub Issues with bug template
- 💡 **Feature Requests**: Describe use case and expected behavior
- ❓ **Questions**: Use Discussions for general questions
- 🔒 **Security Issues**: Report privately via email

## 📚 Additional Resources

- **[DEPLOYMENT.md](DEPLOYMENT.md)** - Complete AWS deployment guide
- **[SECURITY.md](SECURITY.md)** - Security features and best practices
- **[Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)** - Framework documentation
- **[React Docs](https://react.dev/)** - Frontend library documentation
- **[AWS Documentation](https://docs.aws.amazon.com/)** - Cloud platform guides

## 📄 License

This project is licensed under the **Apache License 2.0** - see the [LICENSE.txt](LICENSE.txt) file for details.

## 🙏 Acknowledgments

- **Spring Guides**: Based on [Accessing Relational Data](https://github.com/spring-guides/gs-relational-data-access) tutorial
- **Spring Boot Team**: For the excellent framework and documentation
- **React Team**: For the powerful UI library
- **AWS**: For comprehensive cloud platform and documentation
- **Open Source Community**: For the tools and libraries that make this possible

---

**Built with ❤️ by Johan** | [GitHub](https://github.com/JohanCodeForFun) | [LinkedIn](https://linkedin.com/in/johan-hellberg)
