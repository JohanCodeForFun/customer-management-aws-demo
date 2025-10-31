# AWS Deployment Checklist

## Prerequisites ✅

- [ ] AWS Account with billing enabled
- [ ] AWS CLI installed and configured
- [ ] GitHub repository created
- [ ] Java 17+ installed locally
- [ ] Node.js 18+ installed locally

## AWS Infrastructure Setup

### 1. RDS PostgreSQL Database

- [ ] Create RDS PostgreSQL instance
  - Instance class: `db.t3.micro` (free tier)
  - Engine version: PostgreSQL 14+
  - Database name: `customerdb`
  - Master username: `customeruser`
  - Master password: (secure password)
- [ ] Configure security group to allow EB access
- [ ] Note down RDS endpoint

### 2. Elastic Beanstalk Application

- [ ] Create EB application: `customer-management-api`
- [ ] Create environment: `customer-management-api-env`
- [ ] Platform: Java 17 (Corretto)
- [ ] Set environment variables:
  - [ ] `RDS_HOSTNAME`: Your RDS endpoint
  - [ ] `RDS_PORT`: 5432
  - [ ] `RDS_DB_NAME`: customerdb
  - [ ] `RDS_USERNAME`: customeruser
  - [ ] `RDS_PASSWORD`: Your RDS password
  - [ ] `CORS_ALLOWED_ORIGINS`: Your Amplify domain
- [ ] Test deployment with sample app

### 3. AWS Amplify Frontend

- [ ] Create Amplify app connected to GitHub
- [ ] Set branch: `main`
- [ ] Configure build settings (use amplify.yml)
- [ ] Set environment variable:
  - [ ] `VITE_API_BASE_URL`: Your EB environment URL + /api
- [ ] Enable automatic deployments

## GitHub Repository Setup

### Required Secrets

Configure these in GitHub repository settings > Secrets and variables > Actions:

- [ ] `AWS_ACCESS_KEY_ID`: Your AWS access key
- [ ] `AWS_SECRET_ACCESS_KEY`: Your AWS secret key
- [ ] `VITE_API_BASE_URL`: Your EB URL + /api
- [ ] `AMPLIFY_APP_ID`: Your Amplify app ID

## Security Configuration

### IAM Roles and Policies

- [ ] Create IAM user for GitHub Actions with policies:
  - ElasticBeanstalkFullAccess
  - AmplifyFullAccess
  - RDSFullAccess (for initial setup)
- [ ] Create EB service role
- [ ] Create EB instance profile

### Security Groups

- [ ] EB security group allows HTTP/HTTPS traffic
- [ ] RDS security group allows traffic from EB security group
- [ ] No direct public access to RDS

## Testing Checklist

### Local Testing

- [ ] Backend starts successfully with local PostgreSQL
- [ ] Frontend connects to local backend API
- [ ] CRUD operations work correctly
- [ ] CORS configured for localhost

### Production Testing

- [ ] EB environment is healthy
- [ ] API endpoints respond correctly
- [ ] Database connection working
- [ ] Amplify build succeeds
- [ ] Frontend loads and connects to backend
- [ ] CORS working for production domains

## Deployment Process

### Initial Deployment

1. [ ] Push code to GitHub main branch
2. [ ] GitHub Actions build and test pass
3. [ ] Backend deploys to Elastic Beanstalk
4. [ ] Frontend deploys to Amplify
5. [ ] Verify application works end-to-end

### Ongoing Deployments

- [ ] Feature branch workflow established
- [ ] Pull request triggers tests
- [ ] Main branch merges trigger deployments
- [ ] Deployment status monitoring configured

## Monitoring and Maintenance

### CloudWatch Setup

- [ ] EB application logs streaming to CloudWatch
- [ ] RDS monitoring enabled
- [ ] Cost alerts configured
- [ ] Performance monitoring dashboard

### Backup and Recovery

- [ ] RDS automated backups enabled
- [ ] Database snapshot schedule configured
- [ ] Application code backed up in GitHub

## Cost Management

- [ ] Use free tier resources where possible
- [ ] Set up billing alerts
- [ ] Regular cost review scheduled
- [ ] Auto-scaling configured to manage costs

## Documentation

- [ ] README updated with deployment instructions
- [ ] API documentation available
- [ ] Architecture diagram created
- [ ] Troubleshooting guide documented

## Common URLs After Deployment

Replace these with your actual URLs:

- **Frontend (Amplify)**: https://main.d1234567890123.amplifyapp.com
- **Backend (EB)**: https://customer-management-api-env.region.elasticbeanstalk.com
- **API Health Check**: https://your-eb-url.com/api/health
- **Customer API**: https://your-eb-url.com/api/customers

## Troubleshooting Quick Reference

### Common Issues

1. **502 Bad Gateway**: Check EB application logs, verify Java version
2. **CORS Errors**: Verify CORS_ALLOWED_ORIGINS environment variable
3. **Database Connection**: Check RDS security group and credentials
4. **Build Failures**: Verify GitHub secrets and IAM permissions

### Useful Commands

```bash
# EB CLI commands
eb status
eb logs
eb config

# AWS CLI commands
aws elasticbeanstalk describe-environments
aws amplify list-apps
aws rds describe-db-instances
```
