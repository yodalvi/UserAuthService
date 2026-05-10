Features
User registration and login API
JWT-based stateless authentication
Role-based access control (RBAC)
Secure password hashing using BCrypt
Protected REST endpoints using Spring Security
Token validation and filter-based security

Tech Stack
Java
Spring Boot
Spring Security
JWT (JSON Web Token)
Maven
REST APIs

Project Architecture
	Controller Layer → Handles API requests
	Service Layer → Business logic + JWT filter + authentication handling
	Repository Layer → Database interaction


Authentication Flow
	User registers with credentials
	Login request generates JWT token
	Token is returned to client
	Client sends token in Authorization header
	Spring Security validates token for every request

API Endpoints
	Register User
	POST /auth/register

	Login User
	POST /auth/login

	Protected Endpoint Example
	GET /api/secure-data
	Authorization: Bearer <JWT_TOKEN>

How to Run
# Clone the repo
git clone https://github.com/yodalvi/UserAuthService

# Build project
mvn clean install

# Run application
mvn spring-boot:run

Security Highlights
JWT token-based authentication (no session storage)
Password encryption using BCrypt
Filter-based request validation
Secure API access using Spring Security

Future Improvements
OAuth2 / Google login integration
Refresh token mechanism
User management dashboard

Dockerization & deployment on AWS
Author

Built as a personal project to strengthen backend development skills in secure authentication systems.
