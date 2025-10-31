# 🚀 AWS Deployment Tutorial: Full-Stack Customer Management Application

> **Complete guide for deploying a Spring Boot + React application to AWS**

## 📚 What You'll Learn

By following this tutorial, you will:

- Deploy a React frontend to AWS Amplify
- Deploy a Spring Boot backend to AWS Elastic Beanstalk
- Set up a PostgreSQL database on AWS RDS
- Configure CI/CD with GitHub Actions
- Implement security best practices
- Monitor and troubleshoot your deployed application

## 🎯 Prerequisites

### Required Knowledge

- Basic understanding of Spring Boot and React
- Familiarity with Git and GitHub
- Basic command line usage
- Understanding of REST APIs

### Required Accounts

- [ ] **AWS Account** (with billing enabled)
- [ ] **GitHub Account**
- [ ] **Domain** (optional, for custom URLs)

### Required Software

- [ ] **Java 17+** - `java -version`
- [ ] **Node.js 18+** - `node -version`
- [ ] **Git** - `git --version`
- [ ] **AWS CLI** - `aws --version`
- [ ] **PostgreSQL** (for local development) - `psql --version`

## 📁 Project Overview

Our application consists of:

```
customer-management-aws-demo/
├── client/          # React TypeScript frontend
├── server/          # Spring Boot backend API
├── .github/         # CI/CD workflows
└── docs/           # Documentation
```

**Architecture:**

- **Frontend**: React → AWS Amplify (Global CDN)
- **Backend**: Spring Boot → AWS Elastic Beanstalk (Auto-scaling)
- **Database**: PostgreSQL → AWS RDS (Managed database)
- **CI/CD**: GitHub Actions → Automated deployment

---

## 🏁 Phase 1: Local Development Setup

### Step 1.1: Clone and Setup Project

```bash
# Clone the repository
git clone https://github.com/JohanCodeForFun/customer-management-aws-demo.git
cd customer-management-aws-demo

# Make setup script executable and run it
chmod +x setup.sh
./setup.sh
```

### Step 1.2: Verify Local Setup

**Start Backend:**

```bash
cd server
./mvnw spring-boot:run
```

**Start Frontend (new terminal):**

```bash
cd client
npm run dev
```

**Test Application:**

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api/customers
- Health Check: http://localhost:8080/api/health

✅ **Checkpoint:** You should see the customer management interface and be able to add/delete customers.

---

## ☁️ Phase 2: AWS Infrastructure Setup

### Step 2.1: Install and Configure AWS CLI

```bash
# Install AWS CLI (macOS)
brew install awscli

# Configure with your credentials
aws configure
```

**Enter when prompted:**

- AWS Access Key ID: `[Your access key]`
- AWS Secret Access Key: `[Your secret key]`
- Default region: `us-east-1` (or your preferred region)
- Default output format: `json`

### Step 2.2: Create AWS RDS Database

**Navigate to AWS Console → RDS:**

1. **Click "Create database"**
2. **Engine options:**

   - Engine type: `PostgreSQL`
   - Version: `PostgreSQL 14.x` (latest)

3. **Templates:**

   - Select `Free tier` (for learning)

4. **Settings:**

   - DB instance identifier: `customer-db`
   - Master username: `customeruser`
   - Master password: `YourSecurePassword123!`

5. **Instance configuration:**

   - DB instance class: `db.t3.micro`

6. **Storage:**

   - Allocated storage: `20 GB`
   - Storage type: `General Purpose SSD`

7. **Connectivity:**

   - VPC: `Default VPC`
   - Subnet group: `default`
   - Public access: `Yes` (for learning - use No in production)
   - VPC security group: `Create new`
   - Security group name: `customer-db-sg`

8. **Database authentication:**

   - Database authentication: `Password authentication`

9. **Additional configuration:**

   - Initial database name: `customerdb`

10. **Click "Create database"**

⏱️ **Wait 5-10 minutes** for the database to be created.

**Note the RDS endpoint** (e.g., `customer-db.xxxxxxxxxx.us-east-1.rds.amazonaws.com`)

### Step 2.3: Create Elastic Beanstalk Application

**Navigate to AWS Console → Elastic Beanstalk:**

1. **Click "Create application"**
2. **Application information:**

   - Application name: `customer-management-api`
   - Description: `Spring Boot Customer Management API`

3. **Platform:**

   - Platform: `Java`
   - Platform branch: `Corretto 17`
   - Platform version: `(latest)`

4. **Application code:**

   - Sample application: `Sample application`

5. **Click "Create application"**

⏱️ **Wait 5-10 minutes** for the environment to be created.

### Step 2.4: Create AWS Amplify App

**Navigate to AWS Console → AWS Amplify:**

1. **Click "New app" → "Host web app"**
2. **Select "GitHub"**
3. **Authorize AWS Amplify** to access your GitHub account
4. **Select repository:** `customer-management-aws-demo`
5. **Select branch:** `main`
6. **App name:** `customer-management-frontend`
7. **Build settings:** AWS will detect the React app automatically
8. **Advanced settings:**
   - Build specification: Use the existing `amplify.yml`
9. **Click "Save and deploy"**

⏱️ **Wait 3-5 minutes** for the initial build.

---

## 🔧 Phase 3: Environment Configuration

### Step 3.1: Configure Environment Variables

**Elastic Beanstalk Environment Variables:**

1. Go to **Elastic Beanstalk Console** → `customer-management-api-env`
2. **Configuration** → **Software** → **Edit**
3. **Add Environment properties:**

```
RDS_HOSTNAME=customer-db.xxxxxxxxxx.us-east-1.rds.amazonaws.com
RDS_PORT=5432
RDS_DB_NAME=customerdb
RDS_USERNAME=customeruser
RDS_PASSWORD=YourSecurePassword123!
CORS_ALLOWED_ORIGINS=https://main.d1234567890123.amplifyapp.com
```

4. **Click "Apply"**

**Amplify Environment Variables:**

1. Go to **Amplify Console** → `customer-management-frontend`
2. **Environment variables** → **Manage variables**
3. **Add variable:**

```
VITE_API_BASE_URL=http://customer-management-api-env.xxxxxxxxxx.us-east-1.elasticbeanstalk.com/api
```

4. **Save**

### Step 3.2: Update Security Groups

**RDS Security Group:**

1. **EC2 Console** → **Security Groups**
2. **Find:** `customer-db-sg`
3. **Edit inbound rules** → **Add rule:**
   - Type: `PostgreSQL`
   - Port: `5432`
   - Source: `Custom` → Select Elastic Beanstalk security group
4. **Save rules**

---

## 🚀 Phase 4: Application Deployment

### Step 4.1: Prepare Backend for Deployment

**Update application-production.properties:**

```bash
cd server/src/main/resources
```

Verify the file contains:

```properties
spring.datasource.url=jdbc:postgresql://${RDS_HOSTNAME:localhost}:${RDS_PORT:5432}/${RDS_DB_NAME:customerdb}
spring.datasource.username=${RDS_USERNAME:customeruser}
spring.datasource.password=${RDS_PASSWORD:customerpass}
cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:5173}
```

### Step 4.2: Build and Deploy Backend

**Create deployment package:**

```bash
cd server
./mvnw clean package -DskipTests
```

**Deploy to Elastic Beanstalk:**

1. **Elastic Beanstalk Console** → `customer-management-api-env`
2. **Upload and deploy**
3. **Choose file:** `server/target/relational-data-access-0.0.1-SNAPSHOT.jar`
4. **Version label:** `v1.0.0`
5. **Deploy**

⏱️ **Wait 3-5 minutes** for deployment.

### Step 4.3: Test Backend Deployment

```bash
# Replace with your actual Elastic Beanstalk URL
curl http://customer-management-api-env.xxxxxxxxxx.us-east-1.elasticbeanstalk.com/api/health

# Should return: "Customer Management API is running!"
```

### Step 4.4: Deploy Frontend

**Trigger Amplify build:**

1. **Amplify Console** → `customer-management-frontend`
2. **Main branch** → **Redeploy this version**

Or push a change to trigger automatic deployment:

```bash
git add .
git commit -m "Deploy to AWS"
git push origin main
```

---

## 🔒 Phase 5: Setup CI/CD Pipeline

### Step 5.1: Configure GitHub Secrets

**GitHub Repository → Settings → Secrets and variables → Actions:**

Add these secrets:

```
AWS_ACCESS_KEY_ID=your_access_key_here
AWS_SECRET_ACCESS_KEY=your_secret_key_here
VITE_API_BASE_URL=http://your-eb-url.elasticbeanstalk.com/api
AMPLIFY_APP_ID=your_amplify_app_id_here
```

### Step 5.2: Update CI/CD Workflow

The repository already includes:

- `.github/workflows/continuous-integration-build.yml` - For testing
- `.github/workflows/deploy.yml` - For deployment

**Update deploy.yml with your values:**

```yaml
env:
  AWS_REGION: us-east-1 # Your region
  EB_APPLICATION_NAME: customer-management-api
  EB_ENVIRONMENT_NAME: customer-management-api-env
```

### Step 5.3: Test CI/CD Pipeline

```bash
# Make a small change
echo "# Test deployment" >> README.md

# Commit and push
git add .
git commit -m "Test CI/CD pipeline"
git push origin main
```

**Monitor in GitHub:**

- **Actions tab** → Watch the build and deployment process

---

## 🧪 Phase 6: Testing and Verification

### Step 6.1: End-to-End Testing

**Test the complete flow:**

1. **Open your Amplify URL** (e.g., `https://main.d1234567890123.amplifyapp.com`)
2. **Verify frontend loads**
3. **Test customer operations:**
   - Add a new customer
   - View customer list
   - Delete a customer
   - Search for customers

### Step 6.2: API Testing

```bash
# Health check
curl https://your-amplify-url.amplifyapp.com

# API health check
curl http://your-eb-url.elasticbeanstalk.com/api/health

# Get customers
curl http://your-eb-url.elasticbeanstalk.com/api/customers

# Add customer
curl -X POST http://your-eb-url.elasticbeanstalk.com/api/customers \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe"}'
```

### Step 6.3: Monitor Applications

**CloudWatch Logs:**

1. **CloudWatch Console** → **Log groups**
2. **Find:** `/aws/elasticbeanstalk/customer-management-api-env/var/log/eb-docker/containers/eb-current-app`
3. **View logs** for errors and monitoring

**Amplify Build Logs:**

1. **Amplify Console** → **Build history**
2. **View build details** for any issues

---

## 🔧 Phase 7: Troubleshooting Guide

### Common Issues and Solutions

#### **Issue 1: Database Connection Failed**

```
Error: Connection to localhost:5432 refused
```

**Solution:**

- Verify RDS endpoint in environment variables
- Check security group allows connections from EB
- Verify database credentials

#### **Issue 2: CORS Errors in Browser**

```
Access to fetch at 'http://api..' blocked by CORS policy
```

**Solution:**

- Update `CORS_ALLOWED_ORIGINS` in EB environment variables
- Include your Amplify domain
- Redeploy backend

#### **Issue 3: Frontend Build Fails**

```
Error: VITE_API_BASE_URL is not defined
```

**Solution:**

- Check Amplify environment variables
- Verify the API URL is correct
- Retrigger build

#### **Issue 4: GitHub Actions Fails**

```
Error: AWS credentials not found
```

**Solution:**

- Verify GitHub secrets are set correctly
- Check IAM permissions for the access keys
- Ensure secret names match workflow file

### Health Check Commands

```bash
# Check if services are running
curl -I http://your-eb-url.elasticbeanstalk.com/api/health
curl -I https://your-amplify-url.amplifyapp.com

# Check database connectivity
psql -h your-rds-endpoint.rds.amazonaws.com -U customeruser -d customerdb

# Check logs
aws logs describe-log-groups --region us-east-1
```

---

## 💰 Phase 8: Cost Management

### Expected Monthly Costs (Free Tier)

- **RDS (db.t3.micro)**: $0 (12 months free)
- **Elastic Beanstalk**: $0 (platform is free, pay for EC2)
- **EC2 (t3.micro)**: $0 (12 months free, 750 hours/month)
- **Amplify**: $0 (1GB storage + 15GB bandwidth free monthly)
- **Total**: ~$0-5/month (depending on usage)

### Cost Optimization Tips

1. **Stop environments when not in use:**

   ```bash
   # Terminate EB environment
   aws elasticbeanstalk terminate-environment --environment-name customer-management-api-env
   ```

2. **Use RDS snapshots** instead of running instances
3. **Monitor usage** in AWS Billing Dashboard
4. **Set up billing alerts** for unexpected charges

---

## 🎓 Phase 9: Next Steps and Learning

### What You've Accomplished

✅ **Deployed a full-stack application to AWS**  
✅ **Set up a production database**  
✅ **Implemented CI/CD pipeline**  
✅ **Configured monitoring and logging**  
✅ **Applied security best practices**

### Further Learning

1. **Security Enhancements:**

   - Set up SSL certificates
   - Implement JWT authentication
   - Add API rate limiting

2. **Performance Optimization:**

   - Add CloudFront CDN
   - Implement caching strategies
   - Database optimization

3. **Monitoring and Alerting:**

   - Set up CloudWatch alarms
   - Implement application monitoring
   - Add logging dashboards

4. **Infrastructure as Code:**
   - Learn AWS CloudFormation
   - Try AWS CDK
   - Explore Terraform

### Additional Resources

- **AWS Documentation**: [aws.amazon.com/documentation/](https://aws.amazon.com/documentation/)
- **Spring Boot on AWS**: [spring.io/guides/gs/spring-boot-docker/](https://spring.io/guides/gs/spring-boot-docker/)
- **React Deployment**: [create-react-app.dev/docs/deployment/](https://create-react-app.dev/docs/deployment/)

---

## 🚨 Cleanup Instructions

**When you're done learning, clean up to avoid charges:**

```bash
# Delete Amplify app
aws amplify delete-app --app-id your-app-id

# Terminate Elastic Beanstalk
aws elasticbeanstalk terminate-environment --environment-name customer-management-api-env

# Delete RDS instance
aws rds delete-db-instance --db-instance-identifier customer-db --skip-final-snapshot

# Delete security groups and other resources through AWS Console
```

---

## 📞 Getting Help

- **Troubleshooting Guide**: See `TROUBLESHOOTING.md`
- **Security Information**: See `SECURITY.md`
- **GitHub Issues**: Report problems in the repository
- **AWS Support**: Use AWS Support Center for AWS-specific issues

---

**🎉 Congratulations! You've successfully deployed a production-ready full-stack application on AWS!**

_This tutorial demonstrates real-world deployment patterns used by software development teams. The skills you've learned are directly applicable to professional environments._
