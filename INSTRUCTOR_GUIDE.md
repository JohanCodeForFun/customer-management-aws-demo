# 👨‍🏫 Instructor Guide: AWS Deployment Tutorial

> **Quick reference for instructors teaching AWS deployment**

## 🎯 Learning Objectives

Students will be able to:

- [ ] Deploy React applications to AWS Amplify
- [ ] Deploy Spring Boot applications to AWS Elastic Beanstalk
- [ ] Configure AWS RDS PostgreSQL databases
- [ ] Set up CI/CD pipelines with GitHub Actions
- [ ] Implement security best practices
- [ ] Monitor and troubleshoot cloud applications
- [ ] Manage AWS costs and resources

## ⏱️ Time Allocation (4-hour session)

| Phase       | Duration | Activity                     |
| ----------- | -------- | ---------------------------- |
| **Phase 1** | 30 min   | Local setup and verification |
| **Phase 2** | 45 min   | AWS infrastructure setup     |
| **Phase 3** | 20 min   | Environment configuration    |
| **Phase 4** | 45 min   | Application deployment       |
| **Phase 5** | 30 min   | CI/CD pipeline setup         |
| **Phase 6** | 20 min   | Testing and verification     |
| **Phase 7** | 30 min   | Troubleshooting practice     |
| **Phase 8** | 10 min   | Cost management review       |
| **Phase 9** | 10 min   | Wrap-up and next steps       |

## 📋 Pre-Session Checklist

### For Instructors:

- [ ] AWS account with instructor access
- [ ] Demo environment pre-deployed
- [ ] Backup GitHub repository ready
- [ ] Cost alerts configured
- [ ] Screen sharing setup tested

### For Students:

- [ ] Verify AWS account creation (free tier)
- [ ] GitHub account confirmation
- [ ] Software installation checklist
- [ ] Share repository access

## 🚨 Common Student Issues

### Issue 1: AWS Account Problems

**Symptoms:** Can't create AWS account, billing issues
**Solutions:**

- Guide through account verification
- Explain free tier limitations
- Help with credit card verification

### Issue 2: Permission Errors

**Symptoms:** IAM permission denied, can't create resources
**Solutions:**

- Check AWS account type (root vs IAM user)
- Verify region selection
- Guide through IAM policy attachment

### Issue 3: Local Environment Issues

**Symptoms:** Java/Node version conflicts, PostgreSQL not running
**Solutions:**

- Use provided Docker setup if available
- Guide through version managers (sdkman, nvm)
- Alternative: Cloud development environments

### Issue 4: Network/Firewall Issues

**Symptoms:** Can't access AWS services, connection timeouts
**Solutions:**

- Check corporate firewall settings
- Verify VPN configurations
- Use AWS CloudShell as alternative

## 🛠️ Demo Checkpoints

### Checkpoint 1: Local Environment (Phase 1)

**Verify:**

```bash
curl http://localhost:8080/api/health
curl http://localhost:5173
```

**Expected:** Both services respond correctly

### Checkpoint 2: AWS Infrastructure (Phase 2)

**Verify:**

- RDS instance status: "Available"
- Elastic Beanstalk environment: "Ok"
- Amplify app: Created successfully

### Checkpoint 3: Backend Deployment (Phase 4)

**Verify:**

```bash
curl http://eb-env-url.elasticbeanstalk.com/api/health
```

**Expected:** "Customer Management API is running!"

### Checkpoint 4: Frontend Deployment (Phase 4)

**Verify:**

- Amplify URL loads React app
- Can interact with backend API
- CORS working correctly

### Checkpoint 5: CI/CD Pipeline (Phase 5)

**Verify:**

- GitHub Actions running successfully
- Automated deployment working
- Proper secret configuration

## 💡 Teaching Tips

### Effective Strategies:

1. **Start with the end goal** - Show completed application first
2. **Use pair programming** - Students help each other
3. **Regular checkpoints** - Don't let anyone fall behind
4. **Screenshot everything** - AWS Console changes frequently
5. **Prepare for failures** - Use failures as learning opportunities

### Common Pitfalls to Avoid:

- **Rushing through AWS setup** - Students need time to understand
- **Ignoring cost implications** - Always emphasize cleanup
- **Not testing locally first** - Local environment must work
- **Skipping security discussions** - Explain why security matters

## 🔧 Troubleshooting Scripts

### Quick Health Check:

```bash
#!/bin/bash
echo "=== Environment Health Check ==="
echo "Java Version: $(java -version 2>&1 | head -1)"
echo "Node Version: $(node -version)"
echo "AWS CLI: $(aws --version)"
echo "Git: $(git --version)"
echo "PostgreSQL: $(psql --version)"

echo "=== Service Status ==="
curl -I http://localhost:8080/api/health 2>/dev/null && echo "Backend: OK" || echo "Backend: FAIL"
curl -I http://localhost:5173 2>/dev/null && echo "Frontend: OK" || echo "Frontend: FAIL"
```

### AWS Resource Cleanup:

```bash
#!/bin/bash
echo "=== Cleaning up AWS resources ==="
aws elasticbeanstalk terminate-environment --environment-name customer-management-api-env
aws rds delete-db-instance --db-instance-identifier customer-db --skip-final-snapshot
aws amplify delete-app --app-id $AMPLIFY_APP_ID
echo "Cleanup initiated - check AWS Console for completion"
```

## 📊 Assessment Rubric

### Deployment Success (40 points)

- [ ] Local environment working (10 pts)
- [ ] AWS infrastructure created (10 pts)
- [ ] Backend deployed successfully (10 pts)
- [ ] Frontend deployed successfully (10 pts)

### Configuration & Security (30 points)

- [ ] Environment variables configured (10 pts)
- [ ] Security groups properly set (10 pts)
- [ ] CORS configuration working (10 pts)

### CI/CD Implementation (20 points)

- [ ] GitHub Actions workflow running (10 pts)
- [ ] Automated deployment working (10 pts)

### Documentation & Cleanup (10 points)

- [ ] Proper documentation of process (5 pts)
- [ ] Resources cleaned up properly (5 pts)

## 🎓 Extension Activities

### For Advanced Students:

1. **Add Authentication:** Implement JWT with AWS Cognito
2. **Performance Monitoring:** Set up CloudWatch dashboards
3. **Blue-Green Deployment:** Implement zero-downtime deployments
4. **Infrastructure as Code:** Convert setup to CloudFormation/CDK
5. **Security Hardening:** Implement additional security measures

### For Struggling Students:

1. **Simplified Version:** Deploy only frontend or backend
2. **Guided Mode:** Provide more detailed step-by-step instructions
3. **Pair with Mentor:** Assign advanced students as helpers
4. **Alternative Platforms:** Use simpler deployment options

## 📚 Additional Resources

### For Instructors:

- [AWS Educate Program](https://aws.amazon.com/education/awseducate/)
- [AWS Academy Curriculum](https://aws.amazon.com/training/awsacademy/)
- [Spring Boot AWS Workshop](https://spring.io/guides/gs/spring-boot-docker/)

### For Students:

- [AWS Free Tier Guide](https://aws.amazon.com/free/)
- [12-Factor App Methodology](https://12factor.net/)
- [Cloud Native Patterns](https://docs.microsoft.com/en-us/azure/architecture/patterns/)

## 💰 Cost Management for Classes

### Recommended Approach:

1. **Use AWS Educate Credits** when available
2. **Set strict time limits** for resource usage
3. **Automated cleanup scripts** run after each session
4. **Monitor costs daily** during teaching period
5. **Have backup plan** if costs exceed budget

### Typical Costs (per student):

- **During tutorial**: $1-3/day
- **If left running**: $30-50/month
- **With proper cleanup**: $0-1/month

---

## 🎯 Session Agenda Template

### Opening (15 minutes)

- [ ] Welcome and introductions
- [ ] Learning objectives review
- [ ] Prerequisite verification
- [ ] AWS account confirmation

### Phase 1: Local Setup (30 minutes)

- [ ] Clone repository
- [ ] Run setup script
- [ ] Verify local environment
- [ ] Troubleshoot issues

### Phase 2: AWS Infrastructure (45 minutes)

- [ ] AWS Console overview
- [ ] Create RDS database
- [ ] Set up Elastic Beanstalk
- [ ] Create Amplify app

### Break (15 minutes)

### Phase 3-4: Deployment (65 minutes)

- [ ] Configure environment variables
- [ ] Deploy backend application
- [ ] Deploy frontend application
- [ ] Test end-to-end functionality

### Phase 5: CI/CD (30 minutes)

- [ ] GitHub Actions overview
- [ ] Configure secrets
- [ ] Test automated deployment

### Phase 6-7: Testing & Troubleshooting (50 minutes)

- [ ] End-to-end testing
- [ ] Common issue resolution
- [ ] Monitoring and logging

### Closing (10 minutes)

- [ ] Cost management review
- [ ] Cleanup instructions
- [ ] Next steps and resources
- [ ] Q&A session

---

**📋 Remember:** The goal is not just deployment, but understanding cloud-native application development patterns that students will use in their careers.
