# 🚀 Deployment Guide - Customer Management API

This guide covers multiple deployment methods for the Spring Boot backend to AWS Elastic Beanstalk.

## 📋 Prerequisites

### Required Tools
- **Java 17+**: `java -version`
- **Maven**: Already included via `./mvnw`
- **AWS CLI** (for CLI deployment): [Install Guide](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
- **AWS Account** with Elastic Beanstalk access

### AWS Setup
1. **Create Elastic Beanstalk Application**:
   ```bash
   # Via AWS Console or CLI
   aws elasticbeanstalk create-application \
     --application-name customer-management-api \
     --description "Customer Management REST API"
   ```

2. **Create Environment**:
   ```bash
   aws elasticbeanstalk create-environment \
     --application-name customer-management-api \
     --environment-name customer-management-api-env \
     --platform-arn "arn:aws:elasticbeanstalk:eu-north-1::platform/Java 17 running on 64bit Amazon Linux 2/3.x.x"
   ```

## 🎯 Deployment Methods

### Method 1: Manual Build & Deploy (Beginner Friendly)

**Step 1**: Build the deployment package
```bash
cd server
./build-and-deploy.sh
```

**Step 2**: Upload via AWS Console
1. Go to [AWS Elastic Beanstalk Console](https://console.aws.amazon.com/elasticbeanstalk/)
2. Select your application → Environment
3. Click "Upload and Deploy"
4. Upload the generated zip file from `deploy/deploy-YYYYMMDD-HHMMSS.zip`
5. Click "Deploy"

### Method 2: AWS CLI Deployment (Recommended for Development)

**Prerequisites**: 
```bash
# Install AWS CLI
aws --version

# Configure credentials
aws configure
```

**Deploy**:
```bash
cd server
./deploy-aws-cli.sh [environment-name]

# Example
./deploy-aws-cli.sh customer-management-api-env
```

### Method 3: GitHub Actions CI/CD (Recommended for Production)

**Step 1**: Set up GitHub Secrets
Go to your repository → Settings → Secrets and variables → Actions

Add these secrets:
- `AWS_ACCESS_KEY_ID`: Your AWS access key
- `AWS_SECRET_ACCESS_KEY`: Your AWS secret key

**Step 2**: Update workflow configuration
Edit `.github/workflows/deploy-backend.yml`:
```yaml
application_name: your-eb-application-name
environment_name: your-eb-environment-name  
region: your-aws-region
```

**Step 3**: Deploy
Push changes to `main` branch:
```bash
git add .
git commit -m "Deploy: Update backend"
git push origin main
```

## 🔧 Configuration

### Environment Variables (Set in AWS EB Console)

**Database Configuration**:
```
DB_HOST=your-rds-endpoint.amazonaws.com
DB_PORT=5432
DB_NAME=customerdb
DB_USERNAME=your-username
DB_PASSWORD=your-secure-password
```

**CORS Configuration**:
```
CORS_ALLOWED_ORIGINS=https://main.d123456789.amplifyapp.com,https://your-frontend-domain.com
```

**Application Settings**:
```
SPRING_PROFILES_ACTIVE=production
JPA_DDL_AUTO=validate
LOG_LEVEL=INFO
```

## 🏥 Health Checks

Elastic Beanstalk will use these endpoints:
- **Health Check**: `/api/health`
- **Ping Test**: `/api/ping`

## 📊 Monitoring

### CloudWatch Logs
Logs are automatically streamed to CloudWatch:
```bash
# View logs via CLI
aws logs describe-log-groups --log-group-name-prefix "/aws/elasticbeanstalk"
```

### Application Metrics
- **Health Dashboard**: AWS EB Console → Environment Health
- **Application Logs**: AWS EB Console → Logs
- **Monitoring**: CloudWatch metrics for requests, latency, errors

## 🚨 Troubleshooting

### Common Issues

**1. Health Check Failures**
```bash
# Check if health endpoint responds
curl https://your-app-url.elasticbeanstalk.com/api/health
```

**2. Database Connection Issues**
- Verify RDS security groups allow EB connections
- Check environment variables are set correctly
- Ensure database exists and credentials are valid

**3. CORS Issues**
- Update `CORS_ALLOWED_ORIGINS` with your frontend URL
- Verify frontend is using correct backend URL

### Debugging Commands
```bash
# Check environment status
aws elasticbeanstalk describe-environments \
  --application-name customer-management-api

# Download logs
aws elasticbeanstalk request-environment-info \
  --environment-name customer-management-api-env \
  --info-type tail

# View recent events
aws elasticbeanstalk describe-events \
  --environment-name customer-management-api-env
```

## 🔄 Rollback

If deployment fails:
```bash
# List previous versions
aws elasticbeanstalk describe-application-versions \
  --application-name customer-management-api

# Rollback to previous version
aws elasticbeanstalk update-environment \
  --environment-name customer-management-api-env \
  --version-label previous-version-label
```

## 🎯 Best Practices

1. **Test Locally First**: Always test with `./mvnw spring-boot:run`
2. **Database Migrations**: Use `validate` in production, `update` only in development
3. **Environment Variables**: Never commit secrets to git
4. **Monitoring**: Set up CloudWatch alarms for error rates
5. **Blue/Green Deployments**: Use EB's deployment policies for zero-downtime deploys

## 🔗 Useful Links

- [AWS EB Java Guide](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/java-se-platform.html)
- [Spring Boot EB Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [EB CLI Documentation](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3.html)