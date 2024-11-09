# Rawdapos

Rawdapos est une application Spring Boot qui fournit des fonctionnalités d'authentification et d'enregistrement des utilisateurs en utilisant JWT (JSON Web Token).

## Table des Matières

- [Installation](#installation)
- [Utilisation](#utilisation)
- [Endpoints](#endpoints)
- [Configuration](#configuration)
- [Licence](#licence)

## Installation

1. Clonez le dépôt :
    ```sh
    git clone git@github.com:Rawdapos/rawda_Pos_backend.git
    cd rawda_Pos_backend
    ```

2. Construisez le projet :
    ```sh
    ./mvnw clean install
    ```

3. Exécutez l'application :
    ```sh
    ./mvnw spring-boot:run
    ```

## Utilisation

### Enregistrer un Nouvel Utilisateur

Envoyez une requête POST à `/api/user/register` avec le corps JSON suivant :

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "username": "johndoe",
  "email": "johndoe@example.com",
  "phone": "1234567890",
  "addresse": "123 Main St, Anytown, USA",
  "password": "password123"
}
```

### Login

Send a POST request to `/api/user/login` with the following JSON body:

```json
{
  "username": "johndoe",
  "password": "password123"
}
```

### Get User Profile

Send a GET request to `/api/user/profile` with the JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## Endpoints

- POST `/api/user/register` - Register a new user
- POST `/api/user/login` - Login and receive a JWT token
- GET `/api/user/profile` - Get the profile of the authenticated user

## Configuration

The application uses the following properties in `application.properties`:

```properties
spring.application.name=rawdapos
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/rawdapos
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

security.jwt.secret-key=your_secret_key
security.jwt.issuer=rawdapos
```

## Licence

This project is licensed under the MIT License. See the LICENSE file for details.

Replace `yourusername` with your actual GitHub username and `yourpassword` and `your_secret_key` with your actual database password and JWT secret key, respectively. Save this content in a file named `README.md` in the root directory of your project.
