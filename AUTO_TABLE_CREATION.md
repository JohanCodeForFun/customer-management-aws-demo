# 🚀 Automatic Database Table Creation

This enhanced version of the Spring Boot application automatically creates the `customers` table and sample data when deployed to AWS, eliminating the need for manual database setup.

## ✨ **What Changed:**

### **Enhanced `RelationalDataAccessApplication.java`:**

The application now automatically:

1. **Checks if the `customers` table exists** on startup
2. **Creates the table** if it doesn't exist (using `CREATE TABLE IF NOT EXISTS`)
3. **Adds sample data** if the table is empty
4. **Logs all operations** for debugging

## 🔧 **How It Works:**

### **Production Mode Logic:**

```java
if (isProduction) {
    // Try to query existing table
    try {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Long.class);
        log.info("Database connected. Current customer count: {}", count);
    } catch (Exception e) {
        // Table doesn't exist, create it
        log.info("Creating customers table and adding sample data...");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS customers(" +
            "id SERIAL PRIMARY KEY, " +
            "first_name VARCHAR(255) NOT NULL, " +
            "last_name VARCHAR(255) NOT NULL, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        // Add sample data if table is empty
        if (existingCount == 0) {
            // Insert John Woo, Jeff Dean, Josh Bloch, Josh Long, Jane Doe, Alice Smith
        }
    }
}
```

## 📁 **Deployment Files:**

### **Updated Package:** `deploy-AUTO-TABLE-CREATION.zip`

Contains:

- ✅ **Updated JAR** with automatic table creation
- ✅ **Environment variables** configuration
- ✅ **CORS settings** for frontend connectivity
- ✅ **Port configuration** (5000)
- ✅ **All previous fixes** included

## 🎯 **Benefits:**

### **1. Zero Manual Setup**

- No need to manually create database tables
- No need to run SQL scripts
- Works out-of-the-box on any PostgreSQL database

### **2. Educational Friendly**

- Perfect for teaching AWS deployment
- Students don't need database administration knowledge
- Focuses on application deployment, not database setup

### **3. Production Safe**

- Uses `CREATE TABLE IF NOT EXISTS` (won't overwrite existing data)
- Only adds sample data if table is completely empty
- Comprehensive logging for troubleshooting

### **4. Environment Aware**

- **Development:** Recreates table from scratch (for testing)
- **Production:** Creates table only if missing (preserves data)

## 🚀 **Deployment Instructions:**

### **Step 1: Deploy to Elastic Beanstalk**

1. Go to **Elastic Beanstalk Console**
2. Select your environment: `customer-management-api-env`
3. Click **Upload and Deploy**
4. Upload: `deploy-AUTO-TABLE-CREATION.zip`
5. Click **Deploy**

### **Step 2: Verify Environment Variables** (Only if not set)

Go to **Configuration** → **Software** → **Environment Variables**:

```
RDS_HOSTNAME=database-1.cluster-cva0y8g26zp5.eu-north-1.rds.amazonaws.com
RDS_PORT=5432
RDS_DB_NAME=postgres
RDS_USERNAME=postgres
RDS_PASSWORD=[your-master-password]
CORS_ALLOWED_ORIGINS=https://main.djuc4qsf6ddxg.amplifyapp.com
```

### **Step 3: Test the Application**

```bash
# Health check (should work)
curl http://customer-management-api-env.eba-qt9mipk3.eu-north-1.elasticbeanstalk.com/api/health

# Customers API (should now work automatically!)
curl http://customer-management-api-env.eba-qt9mipk3.eu-north-1.elasticbeanstalk.com/api/customers
```

## 📊 **Expected Response:**

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

## 🔍 **Troubleshooting:**

### **Check Application Logs:**

In Elastic Beanstalk Console → **Logs** → **Request Logs** → **Full Logs**

Look for:

- ✅ `"Production mode: Checking and initializing database if needed..."`
- ✅ `"Table created but empty. Adding sample data..."`
- ✅ `"Sample data added: 6 customers"`

### **Common Issues:**

1. **Environment variables not set** → Check EB Console Configuration
2. **Database connection fails** → Check Security Group and RDS status
3. **Permission denied** → Ensure database user has CREATE TABLE permissions

## 🎉 **Success Indicators:**

- ✅ Health check returns: `"Customer Management API is running! Profile: production"`
- ✅ `/api/customers` returns JSON array with 6 customers
- ✅ Frontend can connect and display customer list
- ✅ No manual database setup required!

## 🔄 **For Future Deployments:**

This enhanced application will work with any PostgreSQL database connection. Just ensure:

1. Database is accessible
2. User has CREATE TABLE permissions
3. Environment variables are set correctly

The application handles the rest automatically! 🚀
