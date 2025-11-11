# Swagger UI Deployment Configuration

## Environment Variables

For proper Swagger UI functionality in different environments, set the following environment variable:

### Local Development

No additional configuration needed. The application will automatically use `http://localhost:8080`.

### AWS Elastic Beanstalk Deployment

Set the following environment variable in your Elastic Beanstalk environment:

```
SERVER_URL=https://your-elasticbeanstalk-app-url.region.elasticbeanstalk.com
```

### Other Cloud Deployments

Set the `SERVER_URL` environment variable to your deployed backend URL:

```
SERVER_URL=https://your-backend-api-domain.com
```

## How to Set Environment Variables in AWS Elastic Beanstalk

### Method 1: AWS Console

1. Go to your Elastic Beanstalk application
2. Navigate to Configuration → Software
3. In Environment Properties, add:
   - **Name**: `SERVER_URL`
   - **Value**: Your actual backend URL

### Method 2: .ebextensions Configuration

Create or update `.ebextensions/environment.config`:

```yaml
option_settings:
  aws:elasticbeanstalk:application:environment:
    SERVER_URL: https://your-app-name.region.elasticbeanstalk.com
    CORS_ALLOWED_ORIGINS: https://your-frontend-domain.com
```

### Method 3: EB CLI

```bash
eb setenv SERVER_URL=https://your-app-name.region.elasticbeanstalk.com
```

## Accessing Swagger UI

Once deployed with the proper `SERVER_URL`:

- **Local**: http://localhost:8080/api/swagger-ui
- **Production**: https://your-backend-url.com/api/swagger-ui

## Troubleshooting

If Swagger UI shows incorrect server URLs:

1. Verify the `SERVER_URL` environment variable is set correctly
2. Check that the URL doesn't have trailing slashes
3. Ensure the URL is accessible (not behind authentication)
4. Restart the application after setting environment variables

## Security Note

In production, consider:

- Restricting access to Swagger UI in production environments
- Using authentication for API documentation
- Setting `springdoc.swagger-ui.enabled=false` for production if needed
