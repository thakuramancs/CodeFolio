# CodeFolio - Portfolio Platform for Developers

A comprehensive platform for developers to showcase their projects, track coding contest performances, and manage their professional profiles.

## Project Structure

```
codefolio/
├── configServer/        # Centralized Configuration Server
├── registryServer/      # Service Discovery (Eureka Server)
├── apiGateway/         # API Gateway Service
├── authService/        # Authentication Service
├── profileService/     # Profile Management Service
├── contestService/     # Contest Tracking Service
└── frontend/          # React Frontend Application
```

## Prerequisites

- Java 17
- Node.js 16+
- Maven
- MySQL

## Local Development Setup

1. Set up environment variables:
   ```bash
   # GitHub Personal Access Token for Profile Service
   export GITHUB_TOKEN=your_github_token
   
   # Database Configuration
   export DB_USERNAME=your_db_username
   export DB_PASSWORD=your_db_password
   
   # Auth0 Configuration
   export AUTH0_DOMAIN=your_auth0_domain
   export AUTH0_CLIENT_ID=your_auth0_client_id
   export AUTH0_AUDIENCE=your_auth0_audience
   ```

2. Create local configuration files:
   ```bash
   # For Profile Service
   cp profileService/run.sh.template profileService/run.sh
   # Edit run.sh with your local configuration
   ```

## Quick Start

1. Start Config Server:
   ```bash
   cd configServer
   ./mvnw spring-boot:run
   ```

2. Start Registry Server:
   ```bash
   cd registryServer
   ./mvnw spring-boot:run
   ```

3. Start Other Services:
   ```bash
   # Start API Gateway
   cd apiGateway
   ./mvnw spring-boot:run

   # Start Auth Service
   cd authService
   ./mvnw spring-boot:run

   # Start Profile Service
   cd profileService
   ./run.sh  # Uses local configuration

   # Start Contest Service
   cd contestService
   ./mvnw spring-boot:run
   ```

4. Start Frontend:
   ```bash
   cd frontend
   npm install
   npm start
   ```

## Environment Variables

Create a `.env` file in the frontend directory with:

```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_AUTH0_DOMAIN=your-auth0-domain
REACT_APP_AUTH0_CLIENT_ID=your-auth0-client-id
REACT_APP_AUTH0_AUDIENCE=your-auth0-audience
```

## Deployment

The project is deployed using the following setup:
- Backend Services: GitHub Actions + Cloud Platform
- Frontend: Vercel
- Configuration: GitHub Repository

### Required Secrets for Deployment
- `GITHUB_TOKEN`: GitHub Personal Access Token
- `VERCEL_TOKEN`: Vercel deployment token
- `CLOUD_PLATFORM_TOKEN`: Your cloud platform credentials
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request 