# MyCarMate

MyCarMate is a car maintenance management application designed to help users track and manage their vehicle-related tasks, such as maintenance records, insurance information, and reminders for upcoming services. Built using JavaFX, Firebase, and PostgreSQL, this application offers a clean, user-friendly interface for efficient vehicle management.

---

## Features

### Dashboard
- Displays a list of user-owned cars with essential details (make, model, year).
- Highlights upcoming reminders for:
    - Registration expiration dates.
    - Inspection expiration dates.
    - Oil changes and other maintenance tasks.
      ![Dashboard Screenshot](https://github.com/antho/MyCarMateProject/blob/main/screenshots/DashboardScreenshot.png)


### Car Management
- Add, edit, or delete cars with detailed information:
    - Make, model, year, and VIN.
    - Mileage, registration expiration date, and inspection expiration date.
- View and update car details seamlessly via a modal interface.

### Maintenance Records
- Log and track maintenance tasks such as:
    - Oil changes, inspections, registrations, and custom service types.
- View past maintenance records in a searchable table.
- Add, edit, and delete maintenance records.
- ![Maintenance Record Screenshot](screenshots/MaintenanceScreenshot.png)

### Insurance Management
- Add, edit, and delete insurance records for each vehicle.
- Track provider, policy number, start and end dates, and coverage amounts.
- View all insurance records in a structured table with action buttons.
- ![Insurance Record Screenshot](screenshots/InsuranceScreenshot.png)

### User Authentication
- Secure login and sign-up system using Firebase Authentication.
- Forgot password feature to reset passwords via email.

### Notifications and Reminders
- Automatic reminders for:
    - Registration and inspection dates expiring soon.
    - Oil changes and other maintenance tasks.
- Ensures proactive vehicle management.

---

## Technologies Used

### Backend
- **Google Cloud (PostgreSQL)**: Database for storing user, car, maintenance, and insurance data.
- **Firebase**: User authentication and password reset functionalities.

### Frontend
- **JavaFX**: Interactive and dynamic GUI components.
- **SceneBuilder**: Used for creating and designing FXML files for the interface.

### Libraries and Dependencies
- **Google Firebase Admin SDK**: For authentication and password management.
- **PostgreSQL JDBC Driver**: For database connectivity, whether used locally or hosted on Google Cloud SQL.
- **Google Cloud SQL Socket Factory**: Enables secure and efficient connectivity to PostgreSQL databases hosted on Google Cloud SQL.
- **JSON**: For handling API requests and responses.
- **Apache HttpClient**: For HTTP-based API calls.

---

## Installation

1. **Prerequisites**:
    - Java 17 or later.
    - Maven installed.
    - PostgreSQL database setup.
    - Firebase project configured with the required credentials.

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/selvap1/MyCarMateProject.git
