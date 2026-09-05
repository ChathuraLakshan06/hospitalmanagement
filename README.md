# Hospital Management System

A role-based Hospital Management System developed using **Java, Spring Boot, Spring Security, Thymeleaf, Spring Data JPA, and MySQL**.

The system is designed to automate and manage hospital operations such as patient registration, doctor management, appointment scheduling, electronic medical records, laboratory services, and role-based access control.

---

## Features

### Authentication and Security
- User Login
- User Logout
- Role-Based Access Control
- BCrypt Password Hashing
- Protected Routes
- Database-Based Authentication

### Patient Management
- Register Patient
- View Patient List
- Search Patients
- Update Patient Details
- Delete Patient
- Automatically Generate Patient Number

### Department Management
- Add Department
- View Departments
- Update Department Details

### Doctor Management
- Add Doctor
- View Doctors
- Search Doctors
- Update Doctor Details
- Delete Doctor
- Assign Doctor to Department
- Manage Basic Doctor Availability Schedule
- Automatically Generate Doctor Number

### Appointment Management
- Book Appointment
- Select Patient and Doctor
- Reschedule Appointment
- Cancel Appointment
- Complete Appointment
- Appointment Status Tracking

Appointment statuses:

- `SCHEDULED`
- `COMPLETED`
- `CANCELLED`

### Electronic Medical Records
- Create Patient Medical Record
- Record Diagnosis
- Record Treatment
- Add Prescription
- Add Clinical Notes
- Add Medical Report Summary
- View Patient Medical History
- Track the User Who Created the Record

### Laboratory Management
- Doctor Can Request Laboratory Tests
- Laboratory Staff Can View Requests
- Sample Collection Tracking
- Laboratory Result Entry
- Reference Range Entry
- Laboratory Notes
- Laboratory Report View
- Laboratory Test Status Tracking

Laboratory statuses:

- `REQUESTED`
- `SAMPLE_COLLECTED`
- `COMPLETED`

---

## User Roles

The application supports role-based access for:

- Administrator
- Doctor
- Receptionist
- Laboratory Staff
- Nurse
- Pharmacist
- Accountant

Current implemented access includes:

| Role | Main Access |
|---|---|
| Administrator | System management, patients, doctors, departments, appointments |
| Doctor | Medical records and laboratory test requests |
| Receptionist | Patient registration and appointment management |
| Laboratory Staff | Sample collection and laboratory result processing |

Additional role-specific modules can be extended for Nurse, Pharmacist, and Accountant.

---

## Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

### Frontend
- HTML
- CSS
- Thymeleaf

### Database
- MySQL 8.4

### Build Tool
- Maven

---

## System Architecture

The application follows a layered architecture:

```text
Presentation Layer
        ↓
Controller Layer
        ↓
Service Layer
        ↓
Repository Layer
        ↓
Database Layer
