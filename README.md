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

- Docker and Docker Compose
- GitHub Account (for GitHub API token)
- Auth0 Account (for authentication)

## Quick Start with Docker

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/codefolio.git
   cd codefolio
   ```

2. Create environment file:
   ```bash
   cp .env.template .env
   # Edit .env with your configurations
   ```

3. Start the application:
   ```bash
   docker-compose up -d
   ```

4. Access the application:
   - Frontend: http://localhost:3000
   - API Gateway: http://localhost:8080
   - Eureka Dashboard: http://localhost:8761
   - Config Server: http://localhost:8888

## Free Cloud Deployment

1. Fork this repository

2. Deploy Database:
   - Create a free MySQL database on PlanetScale or Railway
   - Update the database connection details in your environment variables

3. Deploy Backend Services:
   - Sign up for Oracle Cloud Free Tier
   - Create a VM instance (Always Free)
   - SSH into your VM and clone your repository
   - Install Docker and Docker Compose
   - Set up environment variables
   - Run `docker-compose up -d`

4. Deploy Frontend:
   - Create a Vercel account
   - Import your forked repository
   - Configure environment variables in Vercel dashboard
   - Deploy

5. Update configurations:
   - Update API Gateway CORS settings for your Vercel domain
   - Update Auth0 allowed callbacks and origins

## Environment Variables

Required environment variables:
```env
# Database Configuration
DB_USERNAME=codefolio_user
DB_PASSWORD=your_db_password
DB_ROOT_PASSWORD=your_root_password

# GitHub Configuration
GITHUB_TOKEN=your_github_token

# Auth0 Configuration
AUTH0_DOMAIN=your_auth0_domain
AUTH0_CLIENT_ID=your_auth0_client_id
AUTH0_AUDIENCE=your_auth0_audience
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 