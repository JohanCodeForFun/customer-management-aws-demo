# 🚀 Build and Deploy Guide - Customer Management Application

This guide provides step-by-step instructions for building and deploying the enhanced Spring Boot application with automatic table creation to AWS Elastic Beanstalk.

> **⚠️ CRITICAL WARNING:**
>
> **The deployment WILL FAIL if `Procfile` and `.ebextensions/` are missing!**
>
> Without these files:
>
> - ❌ Application runs in development mode (wrong database)
> - ❌ Wrong port configuration
> - ❌ Missing environment variables
> - ❌ Connection refused errors
>
> **Always verify these files exist in the deploy/ folder BEFORE creating deploy.zip**

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

### **Step 3: Verify Deploy Folder Structure** ⚠️

**CRITICAL:** Before creating the deployment package, ensure the `deploy/` folder contains ALL required files:

```bash
ls -la deploy/
```

**Required files checklist:**

- ✅ `Procfile` - **ESSENTIAL** - Sets production profile and port
- ✅ `.ebextensions/` directory with `app-config.config` - **ESSENTIAL** - Environment configuration
- ✅ Previous JAR file (will be replaced in next step)

**If missing, copy from working template:**

```bash
# If Procfile is missing
cp deploy-AUTO-TABLE-CREATION/Procfile deploy/

# If .ebextensions is missing
cp -r deploy-AUTO-TABLE-CREATION/.ebextensions deploy/
```

### **Step 4: Copy Updated JAR to Deploy Folder**

```bash
cp target/relational-data-access-complete-0.0.1-SNAPSHOT.jar deploy/
```

### **Step 5: Verify Complete Package**

```bash
ls -la deploy/
```

**Expected output:**

```
drwxr-xr-x   .ebextensions/
-rw-r--r--   Procfile
-rw-r--r--   relational-data-access-complete-0.0.1-SNAPSHOT.jar
```

### **Step 6: Create Deployment ZIP**

```bash
cd deploy && zip -r ../deploy.zip .
```

**Verify ZIP contents:**

```bash
unzip -l deploy.zip
```

**Package Contents (MUST contain ALL):**

- ✅ `relational-data-access-complete-0.0.1-SNAPSHOT.jar` - Updated application with auto table creation
- ✅ `Procfile` - **CRITICAL** - Startup configuration for Elastic Beanstalk with production profile
- ✅ `.ebextensions/app-config.config` - **CRITICAL** - AWS configuration settings and environment variables

## 🔍 **Quick Deployment Package Verification**

Before deploying, run this quick check to ensure you have the correct package:

```bash
# Verify deploy.zip contains ALL required files
unzip -l deploy.zip
```

**✅ CORRECT OUTPUT (deploy.zip should contain):**

```
Archive:  deploy.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
 22510080  11-02-2025 21:48   relational-data-access-complete-0.0.1-SNAPSHOT.jar
        0  11-02-2025 21:59   .ebextensions/
     2228  11-02-2025 21:59   .ebextensions/app-config.config
      242  11-02-2025 21:59   Procfile
---------                     -------
 22512550                     4 files
```

**❌ BROKEN OUTPUT (missing configuration files):**

```
Archive:  deploy.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
 22510080  11-02-2025 21:48   relational-data-access-complete-0.0.1-SNAPSHOT.jar
---------                     -------
 22510080                     1 file
```

**If you see the broken output above, DO NOT DEPLOY! Fix the package first using the steps in the troubleshooting section.**

## 🌐 **AWS Deployment**

### **Step 7: Deploy to Elastic Beanstalk**

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

### **Step 8: Verify Environment Variables**

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

### **Step 9: Test Health Endpoint**

```bash
curl http://customer-management-api-env.eba-qt9mipk3.eu-north-1.elasticbeanstalk.com/api/health
```

**Expected Response:**

```
Customer Management API is running! Profile: production
```

### **Step 10: Test Automatic Table Creation**

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

### **Step 11: Check Application Logs**

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
├── Procfile                                            # EB startup configuration (ESSENTIAL)
└── .ebextensions/
    └── app-config.config                              # AWS-specific settings (ESSENTIAL)
```

**Note:** The `application-production.properties` file is embedded in the JAR file, not needed as a separate file.

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

### **❌ Application Runs in Development Mode (CRITICAL):**

**Symptoms:**

- Logs show "No active profile set, falling back to 1 default profile: 'default'"
- Database connection errors: "Failed to obtain JDBC Connection"
- Application tries to connect to localhost instead of RDS
- 502 Bad Gateway errors from nginx
- Connection refused errors to port 5000

**Root Cause:** Missing `Procfile` or `.ebextensions/app-config.config` in deployment package

**Fix:**

1. Check what's in your deploy.zip:

   ```bash
   unzip -l deploy.zip
   ```

2. If missing files, recreate deploy folder from template:

   ```bash
   rm -rf deploy/*
   cp -r deploy-AUTO-TABLE-CREATION/* deploy/
   cp target/relational-data-access-complete-0.0.1-SNAPSHOT.jar deploy/
   cd deploy && zip -r ../deploy.zip .
   ```

3. Verify fixed package:

   ```bash
   unzip -l deploy.zip
   # Must show: Procfile, .ebextensions/, and .jar file
   ```

4. **Re-deploy immediately** - Upload the fixed deploy.zip to Elastic Beanstalk

### **❌ 502 Bad Gateway with Connection Refused:**

**Symptoms:**

- nginx error: "connect() failed (111: Connection refused) while connecting to upstream"
- Application logs show port 8080 instead of 5000
- Web requests return 502 Bad Gateway

**Root Cause:** Application running on wrong port due to missing production profile

**Immediate Fix:**

1. This is the SAME issue as above - deploy the corrected package
2. After deployment, verify in logs:

   ```
   ✅ Should see: "Tomcat initialized with port 5000 (http)"
   ❌ Currently shows: "Tomcat initialized with port 8080 (http)"
   ```

3. Verify production profile is active:
   ```
   ✅ Should see: "The following 1 profile is active: 'production'"
   ❌ Currently shows: "No active profile set, falling back to 1 default profile: 'default'"
   ```

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
