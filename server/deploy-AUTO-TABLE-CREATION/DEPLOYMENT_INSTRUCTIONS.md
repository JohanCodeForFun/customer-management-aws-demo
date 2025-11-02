# 🚀 Deployment Instructions - Fixed Configuration

## 📦 **Fixed Issues**

The following critical issues have been resolved:

### 1. **Environment Variable Syntax** ✅

- **Problem**: Elastic Beanstalk doesn't support `${VAR:default}` syntax directly
- **Fix**: Updated `.ebextensions/app-config.config` to use standard EB environment variable format

### 2. **Production Database Initialization** ✅

- **Problem**: Application tried to DROP and CREATE tables on every startup
- **Fix**: Added profile-aware database initialization that skips table creation in production

### 3. **Simplified Procfile** ✅

- **Problem**: Complex environment variable usage in Procfile
- **Fix**: Simplified to use standard EB environment variables

### 4. **Better Error Handling** ✅

- **Problem**: Database connection failures caused complete startup failure
- **Fix**: Added try-catch blocks and graceful error handling

---

## 📋 **Deployment Steps**

### **Step 1: Upload Package**

Upload `deploy-fixed-YYYYMMDD-HHMM.zip` to your Elastic Beanstalk environment

### **Step 2: Set Environment Variables in EB Console**

Go to Configuration > Software > Environment Variables and add:

```
RDS_HOSTNAME=your-rds-endpoint.amazonaws.com
RDS_PORT=5432
RDS_DB_NAME=customerdb
RDS_USERNAME=your-username
RDS_PASSWORD=your-password
CORS_ALLOWED_ORIGINS=*
```

### **Step 3: Create Database Table**

Connect to your RDS PostgreSQL database and run:

```sql
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

-- Optional: Add some test data
INSERT INTO customers(first_name, last_name) VALUES
('John', 'Woo'),
('Jeff', 'Dean'),
('Josh', 'Bloch'),
('Josh', 'Long');
```

### **Step 4: Verify Deployment**

Check these endpoints after deployment:

- Health check: `https://your-app.region.elasticbeanstalk.com/api/health`
- Customers API: `https://your-app.region.elasticbeanstalk.com/api/customers`

---

## 🔧 **Key Configuration Changes**

### **Procfile**

```
web: java -Dspring.profiles.active=production -Dserver.port=$PORT -jar relational-data-access-complete-0.0.1-SNAPSHOT.jar
```

### **Environment Variables Required**

- `RDS_HOSTNAME` - Your RDS endpoint
- `RDS_PORT` - Database port (5432)
- `RDS_DB_NAME` - Database name
- `RDS_USERNAME` - Database username
- `RDS_PASSWORD` - Database password
- `CORS_ALLOWED_ORIGINS` - Frontend URL or \* for development

### **Application Behavior**

- **Production Profile**: Skips database initialization, connects to existing RDS
- **Development Profile**: Creates and initializes database tables
- **Health Check**: Available at `/api/health` with detailed profile information
- **Customer API**: Available at `/api/customers` with error handling

---

## 🐛 **Troubleshooting**

### **If "Following services are not running: web" persists:**

1. Check CloudWatch logs for application startup errors
2. Verify RDS security group allows connections from EB
3. Confirm RDS database exists and is accessible
4. Ensure environment variables are set correctly in EB console
5. Verify the `customers` table exists in your RDS database

### **Check Application Logs:**

- Go to EB Console > Health > View recent logs
- Look for Spring Boot startup messages and database connection attempts

### **Database Connection Test:**

The health endpoint will show connection status and active profiles:
`GET /api/health` returns: `"Customer Management API is running! Profile: production"`
