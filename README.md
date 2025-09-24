# Sports Booking System

This project is a **microservice-based application** for managing sports bookings, built with **Spring Boot (Java)** on the backend and **Angular** on the frontend.  
It was developed during an internship at Schneider Electric with the goal of learning **microservice architecture**, **aspect-oriented programming (AOP)**, **reflection**, and **custom permission management** using annotations.

---

## Project Overview
The system enables users to register, log in, and book sports sessions (e.g., training with coaches, fields, or sports groups).  
Admins and coaches can manage sessions, groups, and reservations, while the system ensures **secure authentication and authorization** across all services.

---

## Key Features
- **User Management**
  - Add, view, edit, and delete users.
  - Role-based access control (Admin, Coach, User, Editor).
  - Authentication and authorization via **JWT tokens**.

- **Sports Booking**
  - Create, schedule, and cancel reservations.
  - Automatic status updates for reservations (Created → Scheduled → Completed / Canceled).
  - Real-time status refresh (implemented with polling; WebSockets/SSE planned).

- **Permissions & Security**
  - Custom annotations like `@RequiresRole` and `@RequiresPermission`.
  - **AOP + Reflection** used to intercept requests and validate access dynamically.

- **Notifications**
  - Asynchronous **email notifications** using **JMS (ActiveMQ)** and `JavaMailSender`.
  - Notifications for registration, booking confirmation, and cancellations.

- **Error Handling**
  - Centralized error logging in a dedicated `ErrorMessage` table.
  - Consistent API responses for exceptions.

- **Tech Stack**
  - **Backend:** Spring Boot, Spring Data JPA, Hibernate
  - **Frontend:** Angular
  - **Database:** PostgreSQL
  - **Messaging:** JMS (ActiveMQ)
  - **Security:** JWT authentication & authorization

---

## Learning Objectives
- Understand the principles and benefits of **microservice architecture**.  
- Gain hands-on experience with **aspect-oriented programming** and **reflection** in Java.  
- Implement a **custom permission system** using annotations.  
- Integrate **asynchronous messaging** for decoupled services.  

---


