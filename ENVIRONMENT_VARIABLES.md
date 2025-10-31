# Environment Variables Reference Guide

## 🌍 **Required Environment Variables for AWS Deployment**

### **AWS RDS Database Configuration**
```bash
# Required for RDS connection
RDS_HOSTNAME=your-rds-endpoint.amazonaws.com
RDS_PORT=5432
RDS_DB_NAME=customerdb
RDS_USERNAME=your-database-username
RDS_PASSWORD=your-database-password
```

### **Application Configuration**
```bash
# Server settings
SERVER_PORT=5000                    # Port for Elastic Beanstalk
PORT=5000                          # Alternative port variable (EB uses this)

# CORS settings
CORS_ALLOWED_ORIGINS=https://main.d1234567890123.amplifyapp.com,https://your-custom-domain.com
FRONTEND_URL=https://main.d1234567890123.amplifyapp.com

# Spring configuration
SPRING_PROFILES_ACTIVE=production
```

### **Database & Application Behavior**
```bash
# JPA/Hibernate settings
JPA_DDL_AUTO=validate              # validate | create | update | none
JPA_SHOW_SQL=false                 # true | false

# Logging levels
LOG_LEVEL=INFO                     # DEBUG | INFO | WARN | ERROR
SPRING_LOG_LEVEL=INFO              # DEBUG | INFO | WARN | ERROR

# Health checks
DB_HEALTH_CHECK=true               # true | false
HEALTH_DETAILS=always              # always | when-authorized | never
MANAGEMENT_ENDPOINTS=health,info   # Comma-separated list
```

## 🏠 **Local Development Environment Variables**

### **Local PostgreSQL Configuration**
```bash
# Local database settings
DB_HOST=localhost
DB_PORT=5432
DB_NAME=customerdb
DB_USERNAME=customeruser
DB_PASSWORD=customerpass

# Local development settings
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:5174
JPA_DDL_AUTO=create-drop
JPA_SHOW_SQL=true
LOG_LEVEL=DEBUG
```

## 🔧 **Setting Environment Variables**

### **AWS Elastic Beanstalk Console**
1. Go to your EB environment
2. Configuration → Software → Environment properties
3. Add the variables listed above

### **AWS CLI**
```bash
aws elasticbeanstalk update-environment \
  --environment-name customer-management-api-env \
  --option-settings \
  Namespace=aws:elasticbeanstalk:application:environment,OptionName=RDS_HOSTNAME,Value=your-rds-endpoint.amazonaws.com \
  Namespace=aws:elasticbeanstalk:application:environment,OptionName=RDS_USERNAME,Value=your-username \
  Namespace=aws:elasticbeanstalk:application:environment,OptionName=RDS_PASSWORD,Value=your-password \
  Namespace=aws:elasticbeanstalk:application:environment,OptionName=CORS_ALLOWED_ORIGINS,Value=https://your-amplify-url.amplifyapp.com
```

### **Local Development (.env file)**
Create a `.env` file in the server directory:
```bash
# .env file for local development
DB_HOST=localhost
DB_PORT=5432
DB_NAME=customerdb
DB_USERNAME=customeruser
DB_PASSWORD=customerpass
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:5174
JPA_DDL_AUTO=create-drop
JPA_SHOW_SQL=true
LOG_LEVEL=DEBUG
```

### **Docker Environment**
```dockerfile
# Dockerfile environment variables
ENV RDS_HOSTNAME=your-rds-endpoint.amazonaws.com
ENV RDS_PORT=5432
ENV RDS_DB_NAME=customerdb
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=5000
```

## 🔄 **Environment Variable Precedence**

Spring Boot resolves environment variables in this order:
1. **System environment variables** (highest priority)
2. **Java system properties** (-D flags)
3. **Application properties files**
4. **Default values** (lowest priority)

Example resolution:
```
spring.datasource.url=jdbc:postgresql://${RDS_HOSTNAME:${DB_HOST:localhost}}:${RDS_PORT:${DB_PORT:5432}}/${RDS_DB_NAME:${DB_NAME:customerdb}}
```

This means:
- Use `RDS_HOSTNAME` if available
- Fall back to `DB_HOST` if `RDS_HOSTNAME` is not set
- Fall back to `localhost` if neither is set

## 🧪 **Testing Environment Variables**

### **Verify Variables Are Set**
```bash
# Check environment variables
echo $RDS_HOSTNAME
echo $CORS_ALLOWED_ORIGINS

# Test application with variables
export RDS_HOSTNAME=your-endpoint.amazonaws.com
export RDS_USERNAME=your-username
export CORS_ALLOWED_ORIGINS=https://your-frontend.amplifyapp.com
java -jar target/relational-data-access-complete-0.0.1-SNAPSHOT.jar
```

### **Health Check Endpoint**
```bash
# Test if variables are properly loaded
curl http://localhost:8080/api/health

# Check application properties endpoint (if management endpoints are enabled)
curl http://localhost:8080/actuator/env
```

## 🔒 **Security Best Practices**

1. **Never commit credentials** to version control
2. **Use AWS Secrets Manager** for sensitive data in production
3. **Rotate credentials regularly**
4. **Use IAM roles** when possible instead of access keys
5. **Limit CORS origins** to specific domains in production

## 📝 **Environment Variable Validation**

Add this to your application startup to validate required variables:
```java
@Component
public class EnvironmentValidator {
    
    @Value("${RDS_HOSTNAME:}")
    private String rdsHostname;
    
    @PostConstruct
    public void validateEnvironment() {
        if ("production".equals(activeProfile) && rdsHostname.isEmpty()) {
            throw new IllegalStateException("RDS_HOSTNAME must be set in production");
        }
    }
}
```