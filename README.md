
# Go Linguage Server

## Overview
Go Language Server is a Spring Boot project designed to provide a backend service for learning English. It offers various endpoints to facilitate language learning through exercises, quizzes, and other interactive methods.

## Features
- User authentication and authorization
- Vocabulary exercises
- Grammar quizzes
- Progress tracking
- RESTful API endpoints

## Technologies Used
- Java
- Spring Boot
- Spring Security
- AI
- Cloudinary
- Hibernate
- Postgresql
- Maven

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Postgresql

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/viethantrinh/go_linguage_server
   ```

2. Navigate to the project directory:
   ```sh
   cd go_linguage_server
   ```

3. Configure the database:
   - Create a Postgresql database named `go_linguage`.
   - Update the `application.yaml` file with your database credentials.

4. Build the project:
   ```sh
   mvn clean install
   ```

5. Run the application:
   ```sh
   mvn spring-boot:run
   ```

### API Documentation
The API documentation is available at `http://localhost:8080/swagger-ui.html` after running the application.

## Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries, please contact [your email address].
