# Custom Implementation of Authentication and Authorization Server Using OAuth 2.0 and OpenID Connect.

#### This repository houses a multi-module Spring Security application comprising three main modules: 
- OAuth-Client
- OAuth-Auth-Server
- OAuth-Resource-Server 

#### The project implements Custom Authentication and Authorization servers based on OAuth 2.0 and OpenID Connect standards within a distributed system. Developed using Java, Spring Boot, and SQL.

## Table of Contents

- [Modules](#modules)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Modules

1. **OAuth-Client**: This module serves as the client-side implementation of OAuth 2.0. It facilitates user authentication and authorization using OAuth.

2. **OAuth-Server**: This module is responsible for handling OAuth 2.0 authentication and authorization requests. It issues access tokens and manages user sessions.

3. **OAuth-Resource-Server**: This module acts as a resource server that hosts protected resources. It validates access tokens and provides secure access to protected resources.

## Prerequisites

Before you begin, ensure you have the following prerequisites installed:

- Java Development Kit (JDK) 17 or later
- Spring Boot version 3.2.2
- Maven (for building and managing dependencies)

## Getting Started

To get started with the project, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/BytePiston/spring-security-parent.git
  
2. Build the project using Maven:
   ```bash
   cd spring-security-parent
   mvn clean install

## Configuration

### Create SQL Schema and Update Configuration:
- Create the necessary SQL schema for your application.
- Update the application.yml files in each module with the database configuration, ensuring that the connection details match your setup.

#### Each module in the project has its own configuration options please follow below links for "application.yml" file for each module.

- [OAuth-Authorization-Server Configuration](https://github.com/BytePiston/spring-security-parent/blob/master/oauth-authorization-server/src/main/resources/application.yml)
- [OAuth-Resource-Server Configuration](https://github.com/BytePiston/spring-security-parent/blob/master/oauth-resource-server/src/main/resources/application.yml)
- [Spring-Security-Client Configuration](https://github.com/BytePiston/spring-security-parent/blob/master/spring-security-client/src/main/resources/application.yml)


## Usage

### Postman Collection for User Management:

- To facilitate user management, I have provided a Postman collection that includes endpoints for creating users, verifying user token, regenerating user tokens, and managing passwords. Import the Postman Collection into your Postman workspace.

### Token Verification Expiration Time And Regeneration:
**_NOTE: Please verify Token using the URL received in the response of `/register`, `/resetPassword`, and `/updatePassword` endpoint; Without Token Verification User will be marked as "Disabled";_** 
- For "Newly Created Users" and "Password Reset," the token verification expiration time is currently set to 10 minutes. You can customize this by changing the EXPIRATION_TIME_IN_MINUTES variable in the Constants.java file.
- Ensure that user tokens are validated within the specified expiration time (**Default is 10 minutes**). If the validation time exceeds, regenerate the token using the appropriate endpoint:
- For "Newly Created Users": Use `/resendVerificationToken` endpoint.
- For "Password Reset": Use `/resendResetPasswordToken` endpoint.
- To "Verify Token": Use `/verifyToken` endpoint.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.
