# 🚀 Build and Deploy Guide - Customer Management Application

This guide provides step-by-step instructions for building and deploying the enhanced Spring Boot application with automatic table creation to AWS Elastic Beanstalk.

## 📋 **Prerequisites**

- ✅ Java 17+ installed
- ✅ Maven installed
- ✅ AWS Account with Elastic Beanstalk environment configured
- ✅ RDS PostgreSQL database running
- ✅ Environment variables set in Elastic Beanstalk Console

## 🔧 **Build Process**

### **Step 1: Navigate to Server Directory**

```bash
cd /path/to/project/gs-relational-data-access/server
```

### **Step 2: Clean and Build Application**

```bash
mvn clean package -DskipTests
```

**Expected Output:**

- ✅ Compilation successful
- ✅ JAR file created: `target/relational-data-access-complete-0.0.1-SNAPSHOT.jar`
- ✅ Build time: ~2-3 seconds

## 📦 **Deployment Package Creation**

### **Step 3: Copy Updated JAR to Deploy Folder**

```bash
cp target/relational-data-access-complete-0.0.1-SNAPSHOT.jar deploy/
```

### **Step 4: Create Deployment ZIP**

```bash
cd deploy && zip -r ../deploy.zip .
```

**Package Contents:**

- ✅ `relational-data-access-complete-0.0.1-SNAPSHOT.jar` - Updated application with auto table creation
- ✅ `Procfile` - Startup configuration for Elastic Beanstalk
- ✅ `.ebextensions/app-config.config` - AWS configuration settings
- ✅ `application-production.properties` - Production database configuration

## 🌐 **AWS Deployment**

### **Step 5: Deploy to Elastic Beanstalk**

1. **Open AWS Elastic Beanstalk Console**

   - Navigate to your environment: `customer-management-api-env`

2. **Upload and Deploy**

   - Click **"Upload and Deploy"**
   - Select file: `deploy.zip`
   - Version label: `auto-table-creation-v1.0`
   - Click **"Deploy"**

3. **Wait for Deployment**
   - ⏱️ Deployment time: 2-5 minutes
   - ✅ Status should show: "Environment update completed successfully"

### **Step 6: Verify Environment Variables**

Go to **Configuration** → **Software** → **Environment Variables** and ensure these are set:

```bash
RDS_HOSTNAME=database-1.cluster-cva0y8g26zp5.eu-north-1.rds.amazonaws.com
RDS_PORT=5432
RDS_DB_NAME=postgres
RDS_USERNAME=postgres
RDS_PASSWORD=[your-master-password]
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:5174,https://main.djuc4qsf6ddxg.amplifyapp.com
```

**⚠️ Important:** Update `CORS_ALLOWED_ORIGINS` to include local development URLs for frontend testing.

## 🧪 **Post-Deployment Testing**

### **Step 7: Test Health Endpoint**

```bash
curl http://customer-management-api-env.eba-qt9mipk3.eu-north-1.elasticbeanstalk.com/api/health
```

**Expected Response:**

```
Customer Management API is running! Profile: production
```

### **Step 8: Test Automatic Table Creation**

```bash
curl http://customer-management-api-env.eba-qt9mipk3.eu-north-1.elasticbeanstalk.com/api/customers
```

**Expected Response:**

```json
[
  { "id": 1, "firstName": "John", "lastName": "Woo" },
  { "id": 2, "firstName": "Jeff", "lastName": "Dean" },
  { "id": 3, "firstName": "Josh", "lastName": "Bloch" },
  { "id": 4, "firstName": "Josh", "lastName": "Long" },
  { "id": 5, "firstName": "Jane", "lastName": "Doe" },
  { "id": 6, "firstName": "Alice", "lastName": "Smith" }
]
```

### **Step 9: Check Application Logs**

In Elastic Beanstalk Console → **Logs** → **Request Logs** → **Full Logs**

Look for these success indicators:

```
Production mode: Checking and initializing database if needed...
Table created but empty. Adding sample data...
Sample data added: 6 customers
Application started successfully
```

## 📁 **File Structure After Deployment**

```
deploy/
├── relational-data-access-complete-0.0.1-SNAPSHOT.jar  # Main application
├── Procfile                                            # EB startup configuration
├── .ebextensions/
│   └── app-config.config                              # AWS-specific settings
└── application-production.properties                  # Database configuration
```

## 🎯 **Key Features Deployed**

### **✨ Automatic Database Setup**

- ✅ Creates `customers` table if it doesn't exist
- ✅ Adds sample data (6 customers) if table is empty
- ✅ Safe for existing data (won't overwrite)
- ✅ Comprehensive error handling and logging

### **🔧 Production-Ready Configuration**

- ✅ Environment variable-driven database connection
- ✅ CORS configuration for frontend connectivity
- ✅ Health checks and monitoring endpoints
- ✅ Proper connection pooling and timeouts

### **🌐 Multi-Environment Support**

- ✅ Local development (recreates tables)
- ✅ Production deployment (preserves existing data)
- ✅ Frontend CORS support for multiple domains

## 🚨 **Troubleshooting**

### **Deployment Fails:**

1. Check EB Console logs for errors
2. Verify JAR file is not corrupted
3. Ensure environment variables are set correctly

### **Database Connection Issues:**

1. Verify RDS cluster is running
2. Check security group allows connections
3. Confirm environment variables match RDS settings

### **Table Creation Fails:**

1. Ensure database user has CREATE TABLE permissions
2. Check if database exists (use `postgres` if `customerdb` doesn't exist)
3. Review application logs for specific error messages

### **CORS Errors from Frontend:**

1. Verify `CORS_ALLOWED_ORIGINS` includes your frontend URL
2. Restart EB environment after changing environment variables
3. Check frontend `.env` files use correct API URLs

## ✅ **Success Checklist**

- [ ] Build completes without errors
- [ ] Deployment package created successfully
- [ ] EB deployment shows "Environment update completed successfully"
- [ ] Health endpoint returns success message
- [ ] Customers endpoint returns JSON array with 6 customers
- [ ] Application logs show successful table creation
- [ ] Frontend can connect without CORS errors

## 🎉 **Next Steps**

### **For Development:**

- Update frontend environment variables
- Test full-stack functionality locally
- Deploy frontend to AWS Amplify

### **For Production:**

- Configure SSL certificate for HTTPS
- Set up CloudWatch monitoring
- Configure backup strategies for RDS

### **For Students:**

- This deployment is ready for educational use
- No manual database setup required
- Focus on learning AWS services and deployment concepts

## 📝 **Deployment Log Template**

```
Deployment: deploy.zip
Date: [Current Date]
Version: auto-table-creation-v1.0
Environment: customer-management-api-env
Status: ✅ SUCCESS

Features Deployed:
- Automatic table creation
- Sample data insertion
- CORS configuration
- Production database connection
- Health monitoring

Test Results:
- Health Check: ✅ PASS
- Database Connection: ✅ PASS
- Table Creation: ✅ PASS
- API Endpoints: ✅ PASS
- Frontend Connectivity: ✅ PASS
```

🚀 **Your enhanced Customer Management Application with automatic database setup is now successfully deployed and ready for use!**
