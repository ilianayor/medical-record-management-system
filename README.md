A Spring Boot web application for managing patient medical records, doctors, diagnoses, visits, and sick leave data with role-based access control.

## Features

- CRUD operations for managing doctors, patients, diagnoses, visits, and sick leave  
- Assign general practitioners and track insurance status  
- Doctors can diagnose, prescribe, and issue sick leave  
- Reporting and statistics on medical data  
- Role-based access control for patients, doctors, and administrators  

## Tech Stack
- Spring Boot, REST APIs  
- Relational Database  
- Spring Security  
- JUnit & Mockito  
- Exception handling and validation

## Getting Started

1. Clone the repository, configure database, and run:  
   ```bash
   git clone https://github.com/ilianayor/medical-record-management-system.git
   cd medical-record-management-system
   # Edit application.properties to configure your database
   ./mvnw spring-boot:run
