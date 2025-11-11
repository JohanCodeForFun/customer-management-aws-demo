#!/bin/bash

# AWS CLI Deployment Script for Elastic Beanstalk
# Prerequisites: AWS CLI installed and configured
# Usage: ./deploy-aws-cli.sh [environment-name]

set -e

# Configuration - Update these values
APPLICATION_NAME="customer-management-api"
DEFAULT_ENVIRONMENT="customer-management-api-env"
REGION="eu-north-1"

# Use provided environment name or default
ENVIRONMENT_NAME=${1:-$DEFAULT_ENVIRONMENT}

echo "🚀 Deploying to Elastic Beanstalk..."
echo "Application: $APPLICATION_NAME"
echo "Environment: $ENVIRONMENT_NAME"
echo "Region: $REGION"
echo ""

# Check if AWS CLI is installed and configured
if ! command -v aws &> /dev/null; then
    echo "❌ AWS CLI is not installed. Please install it first:"
    echo "   https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html"
    exit 1
fi

# Verify AWS credentials
if ! aws sts get-caller-identity &> /dev/null; then
    echo "❌ AWS credentials not configured. Run 'aws configure' first."
    exit 1
fi

# Build the application
echo "🏗️ Building application..."
./mvnw clean package -DskipTests

# Prepare deployment package
echo "📦 Creating deployment package..."
TIMESTAMP=$(date +%Y%m%d-%H%M%S)
VERSION_LABEL="v$TIMESTAMP-$(git rev-parse --short HEAD 2>/dev/null || echo 'local')"
DEPLOY_ZIP="deploy-$VERSION_LABEL.zip"

mkdir -p deploy/temp
cp target/relational-data-access-complete-0.0.1-SNAPSHOT.jar deploy/temp/
cp deploy/Procfile deploy/temp/
cp -r deploy/.ebextensions deploy/temp/

cd deploy/temp
zip -r "../$DEPLOY_ZIP" .
cd ../..
rm -rf deploy/temp

echo "✅ Created deployment package: deploy/$DEPLOY_ZIP"

# Upload to S3 (Elastic Beanstalk requirement)
echo "⬆️ Uploading to S3..."
S3_BUCKET="elasticbeanstalk-$REGION-$(aws sts get-caller-identity --query Account --output text)"
S3_KEY="$APPLICATION_NAME/$DEPLOY_ZIP"

aws s3 cp "deploy/$DEPLOY_ZIP" "s3://$S3_BUCKET/$S3_KEY" --region $REGION

# Create application version
echo "📝 Creating application version..."
aws elasticbeanstalk create-application-version \
    --application-name "$APPLICATION_NAME" \
    --version-label "$VERSION_LABEL" \
    --source-bundle "S3Bucket=$S3_BUCKET,S3Key=$S3_KEY" \
    --region $REGION

# Deploy to environment
echo "🚀 Deploying to environment..."
aws elasticbeanstalk update-environment \
    --application-name "$APPLICATION_NAME" \
    --environment-name "$ENVIRONMENT_NAME" \
    --version-label "$VERSION_LABEL" \
    --region $REGION

echo ""
echo "✅ Deployment initiated!"
echo "📊 Monitor deployment status:"
echo "   aws elasticbeanstalk describe-environments --application-name $APPLICATION_NAME --environment-names $ENVIRONMENT_NAME --region $REGION"
echo ""
echo "🌐 Environment URL will be available once deployment completes."

# Wait for deployment to complete (optional)
read -p "🔄 Wait for deployment to complete? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "⏳ Waiting for deployment..."
    aws elasticbeanstalk wait environment-updated \
        --application-name "$APPLICATION_NAME" \
        --environment-names "$ENVIRONMENT_NAME" \
        --region $REGION
    
    # Get environment info
    ENV_INFO=$(aws elasticbeanstalk describe-environments \
        --application-name "$APPLICATION_NAME" \
        --environment-names "$ENVIRONMENT_NAME" \
        --region $REGION \
        --query 'Environments[0].{Health:Health,Status:Status,URL:CNAME}' \
        --output table)
    
    echo "🎉 Deployment completed!"
    echo "$ENV_INFO"
fi