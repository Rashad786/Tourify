# 🌍 Tourify

### Full-Stack MERN Platform for Event Discovery & Booking

![Frontend](https://img.shields.io/badge/FRONTEND-REACT-0A66C2?style=for-the-badge&logo=react)
![Backend](https://img.shields.io/badge/BACKEND-SPRING_BOOT-6DB33F?style=for-the-badge&logo=springboot)
![Database](https://img.shields.io/badge/DATABASE-MYSQL-4479A1?style=for-the-badge&logo=mysql)
![Auth](https://img.shields.io/badge/AUTH-JWT-FF6F00?style=for-the-badge&logo=jsonwebtokens)
![Build](https://img.shields.io/badge/BUILD-MAVEN-C71A36?style=for-the-badge&logo=apachemaven)

## 🚀 Projec Overview

Tourify is a full-stack platform designed to simplify **event discovery, booking, and management** by connecting users, artists, and venues in a unified ecosystem.

---

## 🎯 Features

* 🔐 User Authentication (Login/Signup)
* 👤 Role-based Access (User / Admin)
* 🎟️ Event Creation & Management
* 📅 Event Booking System
* 📊 Dashboard for tracking activities
* 🔎 Event Discovery

---

## 🗺️ Routing System

```
/api
├── /auth                 → Authentication Module
├── /admin
│   ├── /transports      → Transport Management
│   ├── /lodgings        → Lodging Management
│   ├── /locations       → Location Management
│   ├── /tours           → Tour Management
│   ├── /tourTicketSummary → Ticket Analytics
│   └── /tourDetails     → Tour + Booking Details
├── /customer
│   ├── /tours           → Browse Tours
│   ├── /myBookings      → User Bookings
│   ├── /create-payment-intent → Payment Initiation
│   ├── /confirm-payment → Booking Confirmation
│   └── /filterTours     → Filter Tours
└── /chat                → AI Chat Service
```

### 🔐 Auth Routes

```
/auth
├── POST   /signup                  → Register user
├── POST   /login                   → Login & get JWT
├── GET    /admin/dashboard         → Admin dashboard
└── GET    /customer/dashboard      → Customer dashboard
```

### 🚚 Transport Routes

```
/admin/transports
├── POST   /                        → Add transport
├── GET    /                        → Get all transports
├── GET    /{id}                    → Get transport by ID
├── PUT    /{id}                    → Update transport
└── DELETE /{id}                    → Delete transport
```

### 🏨 Lodging Routes

```
/admin/lodgings
├── POST   /                        → Add lodging
├── GET    /                        → Get all lodgings
├── GET    /{id}                    → Get lodging by ID
├── PUT    /{id}                    → Update lodging
└── DELETE /{id}                    → Delete lodging
```

### 📍 Location Routes

```
/admin/locations
├── POST   /                        → Add location
├── GET    /                        → Get all locations
├── GET    /{id}                    → Get location by ID
├── PUT    /{id}                    → Update location
└── DELETE /{id}                    → Delete location
```

### 🧳 Tour Routes

```
/admin/tours
├── POST   /                        → Create tour (with images)
├── GET    /                        → Get all tours
├── GET    /{id}                    → Get tour by ID
├── PUT    /{id}                    → Update tour
└── DELETE /{id}                    → Delete tour
```

### 🤖 Chat Route

```
/chat
└── POST   /                        → AI chat response
```

### 🎟️ Customer & Booking Routes

```
/customer
├── GET    /tours                          → Get all tours
├── GET    /tours/{id}                     → Get tour by ID
├── GET    /myBookings                     → Get user bookings
├── POST   /create-payment-intent/{tourId} → Create payment
├── POST   /confirm-payment/{bookingId}    → Confirm booking
└── GET    /filterTours                    → Filter tours
```

### 📊 Admin Analytics Routes

```
/admin
├── GET /tourTicketSummary     → Ticket summary per tour
└── GET /tourDetails/{tourId}  → Tour + booking details
```

---

## 🏗️ Architecture

The project follows a **layered architecture**:

```
Frontend (UI) → Backend (API Layer) → Database
```

* **Frontend**: Handles UI/UX and user interactions
* **Backend**: REST APIs for business logic
* **Database**: Stores users, events, and bookings
  

## 📂 Project Structure

```
Tourify/
│── frontend/        # UI code
│── backend/         # API and server logic
│── database/        # Schema / models
│── README.md
```

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the repository

```
git clone https://github.com/Rashad786/Tourify.git
cd Tourify
```

### 2️⃣ Install dependencies

#### For backend

```
cd backend
npm install
```

#### For frontend

```
cd frontend
npm install
```

### 3️⃣ Run the project

#### Backend

```
npm start
```

#### Frontend

```
npm start
```

---

## 🔗 API Documentation

### 🔐 Auth APIs (`/auth`)

| Method | Endpoint                   | Description             | Access   |
| ------ | -------------------------- | ----------------------- | -------- |
| POST   | `/auth/signup`             | Register a new user     | Public   |
| POST   | `/auth/login`              | Login and get JWT token | Public   |
| GET    | `/auth/admin/dashboard`    | Admin dashboard         | ADMIN    |
| GET    | `/auth/customer/dashboard` | Customer dashboard      | CUSTOMER |

---

### 🚚 Transport APIs (`/admin/transports`)

| Method | Endpoint                 | Description         | Access |
| ------ | ------------------------ | ------------------- | ------ |
| POST   | `/admin/transports`      | Add transport       | ADMIN  |
| GET    | `/admin/transports/{id}` | Get transport by ID | ADMIN  |
| GET    | `/admin/transports`      | Get all transports  | ADMIN  |
| PUT    | `/admin/transports/{id}` | Update transport    | ADMIN  |
| DELETE | `/admin/transports/{id}` | Delete transport    | ADMIN  |

---

### 🏨 Lodging APIs (`/admin/lodgings`)

| Method | Endpoint               | Description       | Access |
| ------ | ---------------------- | ----------------- | ------ |
| POST   | `/admin/lodgings`      | Add lodging       | ADMIN  |
| GET    | `/admin/lodgings/{id}` | Get lodging by ID | ADMIN  |
| GET    | `/admin/lodgings`      | Get all lodgings  | ADMIN  |
| PUT    | `/admin/lodgings/{id}` | Update lodging    | ADMIN  |
| DELETE | `/admin/lodgings/{id}` | Delete lodging    | ADMIN  |

---

### 📍 Location APIs (`/admin/locations`)

| Method | Endpoint                | Description        | Access |
| ------ | ----------------------- | ------------------ | ------ |
| POST   | `/admin/locations`      | Add location       | ADMIN  |
| GET    | `/admin/locations/{id}` | Get location by ID | ADMIN  |
| GET    | `/admin/locations`      | Get all locations  | ADMIN  |
| PUT    | `/admin/locations/{id}` | Update location    | ADMIN  |
| DELETE | `/admin/locations/{id}` | Delete location    | ADMIN  |

---

### 🧳 Tour APIs (`/admin/tours`)

| Method | Endpoint            | Description             | Access |
| ------ | ------------------- | ----------------------- | ------ |
| POST   | `/admin/tours`      | Create tour with images | ADMIN  |
| GET    | `/admin/tours`      | Get all tours           | ADMIN  |
| GET    | `/admin/tours/{id}` | Get tour by ID          | ADMIN  |
| PUT    | `/admin/tours/{id}` | Update tour             | ADMIN  |
| DELETE | `/admin/tours/{id}` | Delete tour             | ADMIN  |

---

### 🤖 Chat API

| Method | Endpoint | Description      | Access |
| ------ | -------- | ---------------- | ------ |
| POST   | `/chat`  | AI chat response | Public |

---

### 🎟️ Booking & Customer APIs

| Method | Endpoint                                   | Description       | Access   |
| ------ | ------------------------------------------ | ----------------- | -------- |
| GET    | `/customer/tours`                          | Get all tours     | CUSTOMER |
| GET    | `/customer/tours/{id}`                     | Get tour by ID    | CUSTOMER |
| GET    | `/customer/myBookings`                     | Get user bookings | CUSTOMER |
| POST   | `/customer/create-payment-intent/{tourId}` | Create payment    | CUSTOMER |
| POST   | `/customer/confirm-payment/{bookingId}`    | Confirm booking   | CUSTOMER |
| GET    | `/customer/filterTours`                    | Filter tours      | CUSTOMER |

---

### 📊 Admin Analytics APIs

| Method | Endpoint                      | Description             | Access |
| ------ | ----------------------------- | ----------------------- | ------ |
| GET    | `/admin/tourTicketSummary`    | Ticket summary per tour | ADMIN  |
| GET    | `/admin/tourDetails/{tourId}` | Tour + booking details  | ADMIN  |

---

## 🧠 Key Concepts Used

* RESTful API Design
* Authentication & Authorization
* Database Relationships
* Modular Code Structure

---

## 🚧 Challenges Faced

* Handling concurrent bookings
* Managing role-based access control
* Designing scalable backend APIs

---

## 🔮 Future Improvements

* 💳 Payment Integration
* 🔔 Real-time Notifications
* 🤖 AI-based Event Recommendations
* ☁️ Cloud Deployment (AWS/GCP)

---

## 🤝 Contribution

Contributions are welcome! Feel free to fork this repo and submit a pull request.

---

## 📜 License

This project is open-source and available under the MIT License.

---

## 👨‍💻 Author

**Mohd Rashad**

* GitHub: [https://github.com/Rashad786](https://github.com/Rashad786)

---

⭐ If you like this project, don't forget to give it a star!
