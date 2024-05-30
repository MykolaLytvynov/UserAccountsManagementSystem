# User Accounts Management System

A REST API application for managing information about user accounts 
(ID, username, gender, age, account creation timestamp), allowing for creation, reading, updating and deletion functionalities. 
User accounts data are stored in PostgreSQL database. API responses are in JSON format.
Code is covered by unit tests using Spring.

## Setup Instructions
1. Clone this repository to your local machine.
2. Open the project in your preferred IDE.
3. Run the application using Maven or your IDE's run configuration.

## API Endpoints
- **GET /users/{id}:** Retrieve a user by ID.
- **POST /users:** Create a new user.
- **PATCH /users/{id}:** Update specific fields of a user by ID.
- **DELETE /users/{id}:** Delete a user by ID.


## Example JSON Request to create user
```json
{
  "username": "JohnBo",
  "gender": "MALE",
  "birthDate": "2001-05-25"
}
```

## Example JSON Request to update user
```json
{
  "gender": "FEMALE",
  "birthDate": "2000-05-25"
}
```

## Technologies Used
- Java
- Spring Boot
- Spring Web
- Spring Boot Starter Validation
- JUnit
- Mockito
- Lombok
- Spring Data Jpa
- PostgreSQL
- Maven


Thank you!
