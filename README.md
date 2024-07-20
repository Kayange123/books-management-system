# Books Management System

The Books Management System is a comprehensive platform designed to manage and organize book collections efficiently. It caters to libraries, bookstores, and individual book enthusiasts by providing tools for cataloging, tracking, and maintaining a database of books. The system offers features like book registration, borrowing, returning, and searching capabilities, making it easier to manage large collections and enhance user experience.

## Features

- **Book Cataloging**: Easily add, edit, and delete book records with detailed information such as title, author, genre, and publication year.
- **Borrowing and Returning**: Manage the lending process with options to track borrowed books, due dates, and return statuses.
- **User Management**: Handle multiple user roles with varying permissions, including administrators, librarians, and patrons.
- **Search and Filter**: Quickly find books using advanced search and filtering options based on various criteria.
- **Notifications**: Receive alerts and notifications for due dates, overdue books, and system updates.
- **Reports and Analytics**: Generate insightful reports on book usage, borrowing trends, and user activity.

## Technologies Used

- **Backend**: Built with Spring Boot, providing a robust and scalable server-side application.
- **Frontend**: Developed using Angular, offering a dynamic and responsive user interface.
- **Database**: Utilizes PostgreSQL for data storage, ensuring reliable and efficient data management.
- **Authentication**: Secured with JWT-based authentication for user login and access control.

## Installation

### Prerequisites

Ensure you have the following software installed on your machine:

- Java Development Kit (JDK) 11 or higher
- Maven
- Node.js and npm
- PostgreSQL
- Optional Docker Desktop (with postgres and maildev/maildev images)

### Backend Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/Kayange123/books-management-system.git
   cd books-management-system/backend

2. **Configure the database:**
Update the database configuration in src/main/resources/application.example.properties:
  ```sh
  spring.datasource.url=jdbc:postgresql://localhost:5432/your-database
  spring.datasource.username=your-username
  spring.datasource.password=your-password
  spring.jpa.hibernate.ddl-auto=update
  ```
  
3. **Build the backend:**
```sh
Copy code
mvn clean install
```

4. **Run the backend:**
```sh
mvn spring-boot:run
```

The backend server should now be running on `http://localhost:8080`.

## Frontend Setup

Navigate to the frontend directory:

```sh
cd /booker-ui
```

Install Angular CLI globally if you haven't already:

```sh
npm install -g @angular/cli
```

Install dependencies:

```sh
npm install
```

Run the frontend:

```sh
ng serve
```

The frontend application should now be running on `http://localhost:4200`.

## Configuration

Backend: Configure the backend application properties in `src/main/resources/application.properties.`
Frontend: Configure the frontend environment variables in `src/environments/environment.ts`.

## Usage
Open your web browser and navigate to `http://localhost:4200`.
Log in using your credentials (if applicable).
Manage book records, handle borrowing and returning processes, and generate reports.

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgements
We would like to thank all contributors and the open-source community for their valuable support and resources.
