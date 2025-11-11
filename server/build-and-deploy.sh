#!/bin/bash

# Build and Deploy Script for Elastic Beanstalk
# Usage: ./build-and-deploy.sh

set -e  # Exit on any error

echo "🏗️  Building Spring Boot Application..."

# Clean and build the application
echo "Cleaning previous builds..."
./mvnw clean

echo "Building JAR file..."
./mvnw package -DskipTests

# Check if build was successful
if [ ! -f "target/relational-data-access-complete-0.0.1-SNAPSHOT.jar" ]; then
    echo "❌ Build failed - JAR file not found!"
    exit 1
fi

echo "✅ Build successful!"

# Prepare deployment directory
echo "📦 Preparing deployment package..."

# Clean deploy directory
rm -rf deploy/temp
mkdir -p deploy/temp

# Copy JAR file to deploy directory
cp target/relational-data-access-complete-0.0.1-SNAPSHOT.jar deploy/temp/

# Copy Procfile and .ebextensions
cp deploy/Procfile deploy/temp/
cp -r deploy/.ebextensions deploy/temp/

# Create deployment zip
echo "Creating deployment zip..."
cd deploy/temp
zip -r ../deploy-$(date +%Y%m%d-%H%M%S).zip .
cd ../..

# Clean up temp directory
rm -rf deploy/temp

echo "✅ Deployment package created: deploy/deploy-$(date +%Y%m%d-%H%M%S).zip"
echo ""
echo "📋 Next steps:"
echo "1. Go to AWS Elastic Beanstalk Console"
echo "2. Select your application environment"
echo "3. Click 'Upload and Deploy'"
echo "4. Upload the zip file: deploy/deploy-$(date +%Y%m%d-%H%M%S).zip"
echo "5. Deploy!"