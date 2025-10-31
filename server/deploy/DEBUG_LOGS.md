# 🐛 Debug Logs Guide - Elastic Beanstalk

## 🚀 **Quick Debug Steps**

### **Step 1: Check EB Console Logs**

1. Go to **AWS Elastic Beanstalk Console**
2. Select your environment
3. Click **"Logs"** in left sidebar
4. Click **"Request Logs"** → **"Full Logs"**

### **Step 2: SSH to EC2 Instance**

```bash
# Get your instance IP from EB Console
ssh -i your-key.pem ec2-user@your-instance-ip

# Once connected, check these logs:
```

### **Step 3: Key Log Files to Check**

```bash
# Application logs
sudo tail -f /var/log/eb-engine.log
sudo tail -f /var/log/eb-hooks.log

# Application specific logs
sudo tail -f /var/log/web.stdout.log
sudo tail -f /var/log/web.stderr.log

# System logs
sudo tail -f /var/log/messages

# Check if Java process is running
ps aux | grep java

# Check port usage
sudo netstat -tlnp | grep :5000
```

## 🔍 **Common Error Patterns**

### **Database Connection Issues:**

```
ERROR: could not connect to server: Connection refused
ERROR: FATAL: password authentication failed
ERROR: database "customerdb" does not exist
```

### **Port Binding Issues:**

```
ERROR: Port 5000 already in use
ERROR: Address already in use
```

### **Spring Boot Startup Issues:**

```
ERROR: Application failed to start
ERROR: Unable to start web server
ERROR: Bean creation exception
```

### **Environment Variable Issues:**

```
ERROR: Required environment variable not found
ERROR: Could not resolve placeholder
```

## 🛠 **Quick Fixes**

### **1. Check Environment Variables:**

```bash
# SSH to instance and check env vars
sudo su
printenv | grep RDS
printenv | grep SPRING
```

### **2. Check Database Connectivity:**

```bash
# Test database connection
telnet your-rds-endpoint.amazonaws.com 5432

# Or use psql if available
psql -h your-rds-endpoint.amazonaws.com -U customeruser -d customerdb
```

### **3. Check Application Status:**

```bash
# Check if app is running
sudo systemctl status web
sudo journalctl -u web -f

# Check process
ps aux | grep java
```

### **4. Manual Application Test:**

```bash
# Try running the JAR manually
cd /var/app/current
sudo java -Dspring.profiles.active=production \
  -DRDS_HOSTNAME=your-hostname \
  -DRDS_PORT=5432 \
  -DRDS_DB_NAME=customerdb \
  -DRDS_USERNAME=customeruser \
  -DRDS_PASSWORD=your-password \
  -jar relational-data-access-complete-0.0.1-SNAPSHOT.jar
```

## 🎯 **Most Likely Issues**

### **Issue 1: Database Table Missing**

```sql
-- Connect to RDS and create table
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);
```

### **Issue 2: Security Group**

- EB instances can't reach RDS
- RDS security group doesn't allow EB security group

### **Issue 3: Environment Variables**

- Wrong RDS endpoint
- Incorrect credentials
- Missing environment variables

## 📱 **Easy Debug Commands**

Copy and paste these commands when SSH'd to the instance:

```bash
# 1. Check if app is running
echo "=== Java Processes ==="
ps aux | grep java

# 2. Check logs
echo "=== Recent Application Logs ==="
sudo tail -20 /var/log/web.stdout.log
echo "=== Recent Error Logs ==="
sudo tail -20 /var/log/web.stderr.log

# 3. Check environment
echo "=== Environment Variables ==="
sudo printenv | grep -E "(RDS|SPRING|CORS)" | sort

# 4. Check network
echo "=== Network Status ==="
sudo netstat -tlnp | grep -E "(5000|8080)"

# 5. Check disk space
echo "=== Disk Space ==="
df -h

# 6. Check system load
echo "=== System Status ==="
top -n 1 | head -5
```

## 🆘 **Emergency Reset**

If nothing works, try this sequence:

1. **Rebuild Environment:**

   - Go to EB Console → Actions → Rebuild Environment

2. **Or Redeploy:**

   - Upload the `deploy-fixed-*.zip` again
   - Set environment variables again
   - Create database table again

3. **Check RDS:**
   - Ensure RDS is running
   - Check security groups
   - Test connectivity from EB
