# SeatPlan — Exam Hall Seating Arrangement Generator

**SeatPlan** is a production-grade Spring Boot 3 REST API system designed for educational institutions and examination cells to automate exam hall seating arrangements across multiple halls while avoiding placing students from the same class/section in adjacent seats.

---

## 🛠️ Technology Stack

- **Java**: Java 17+ (Tested on OpenJDK / Oracle JDK)
- **Framework**: Spring Boot 3.2.5
- **Modules**: Spring Web, Spring Data JPA, Jakarta Validation
- **ORM / Persistence**: Hibernate 6.x
- **Database**: MySQL 8.0+ / MySQL 9.0+
- **Database Management**: MySQL Workbench
- **Build Tool**: Apache Maven 3.9+
- **API Documentation**: Springdoc OpenAPI (Swagger UI v2.5.0)
- **API Testing**: Postman

---

## 📋 Core System Features

1. **Hall Registration & Auto Seat Generation**: Automatically computes matrix layout (`rows x columns`) and creates individual seat records (`S1`, `S2`, ... `SN`).
2. **Student Registration**: Registers students with unique roll numbers, full name, class name, and section.
3. **Exam Session Management**: Creates exam sessions with exam title, subject name, exam date, start time, and end time.
4. **Student Exam Registration**: Bulk or individual registration of students for specific exam sessions with duplicate prevention.
5. **Smart Seating Allocation Algorithm**:
   - Ensures no student receives more than 1 seat for an exam.
   - Prevents duplicate seat assignment for the same exam.
   - Interleaves sections to strictly avoid horizontal and vertical adjacency of same-section students.
   - Enforces total hall capacity constraints.
6. **Printable Seating Chart API**: Returns hall-wise 2D grid matrix suitable for printing and displaying outside examination halls.
7. **Student Seat Lookup API**: Allows searching for any student's allocated hall, seat number, row, and column by roll number.
8. **Regeneration & Reset**: Supports clearing and re-allocating seating plans.
9. **Global Exception Handling & Validation**: Returns clean, standard HTTP error responses (200, 201, 400, 404, 409, 500).

---

## 🗄️ Database Design & Entity Relationships

Database Name: `seatplan_db`

```
  +------------------+         1:N          +------------------+
  |      Hall        | -------------------> |       Seat       |
  +------------------+                      +------------------+
           |                                         |
           | 1:N                                     | 1:N
           v                                         v
  +------------------------------------------------------------+
  |                      SeatAllocation                        |
  +------------------------------------------------------------+
           ^                                         ^
           | 1:N                                     | 1:N
  +------------------+         1:N          +------------------+
  |     Student      | <------------------- | ExamRegistration |
  +------------------+                      +------------------+
           | 1:N                                     ^ 1:N
           v                                         |
  +------------------------------------------------------------+
  |                        ExamSession                         |
  +------------------------------------------------------------+
```

### Table Definitions

1. **`halls`**: `id`, `hall_number` (UNIQUE), `name`, `hall_rows`, `hall_columns`, `capacity`
2. **`seats`**: `id`, `seat_number`, `row_number`, `column_number`, `hall_id` (FK -> halls.id)
3. **`students`**: `id`, `roll_number` (UNIQUE), `name`, `class_name`, `section`
4. **`exam_sessions`**: `id`, `exam_name`, `subject`, `exam_date`, `start_time`, `end_time`
5. **`exam_registrations`**: `id`, `student_id` (FK), `exam_session_id` (FK) [UNIQUE Constraint: (student_id, exam_session_id)]
6. **`seat_allocations`**: `id`, `exam_session_id` (FK), `student_id` (FK), `seat_id` (FK), `allocated_at`
   - [UNIQUE Constraint: (exam_session_id, student_id)]
   - [UNIQUE Constraint: (exam_session_id, seat_id)]

---

## 🧠 Seating Allocation Algorithm

The core allocation engine in [`SeatingService.java`](src/main/java/com/seatplan/service/SeatingService.java) executes the following steps:

1. **Capacity Validation**: Checks if `registered_students.size() <= total_capacity_across_all_halls`. If capacity is exceeded, it immediately aborts with `CapacityExceededException` (400 Bad Request).
2. **Duplication Check**: Verifies that seating has not already been generated for the given exam session (prevents accidental double allocation).
3. **Section Grouping**: Groups registered students by section identifier (`className + "-" + section`), creating a `Map<String, Queue<Student>>`.
4. **Grid Traversing & Adjacent Constraint Enforcement**:
   - Traverses halls sequentially (ordered by hall number).
   - For each seat position `(r, c)` in row-major order:
     - Identifies the section key of the **Left Neighbor** `(r, c-1)` and **Top Neighbor** `(r-1, c)`.
     - **Candidate Priority 1 (Optimal)**: Picks a section whose key matches **neither** Left nor Top neighbor. If multiple sections qualify, selects the section with the **largest remaining student queue** (greedy approach to prevent section bottlenecks).
     - **Candidate Priority 2 (Partial)**: If no section satisfies both neighbors, picks a section that conflicts with only one neighbor.
     - **Candidate Priority 3 (Fallback)**: If only one section remains with unassigned students, assigns the seat and marks `constraintFullySatisfied = false`.
5. **Persistence**: Saves all generated `SeatAllocation` records in a single transactional batch.

---

## 🔌 REST API Endpoints Summary

| Module | Method | Endpoint | Description |
|---|---|---|---|
| **Hall** | POST | `/api/halls` | Register hall & auto-generate seats |
| | GET | `/api/halls` | Get list of all halls |
| | GET | `/api/halls/{id}` | Get hall details by ID |
| | PUT | `/api/halls/{id}` | Update hall details |
| | DELETE | `/api/halls/{id}` | Delete hall |
| **Student** | POST | `/api/students` | Register student |
| | GET | `/api/students` | List all students |
| | GET | `/api/students/{id}` | Get student by ID |
| | GET | `/api/students/roll/{rollNumber}` | Get student by Roll Number |
| | PUT | `/api/students/{id}` | Update student |
| | DELETE | `/api/students/{id}` | Delete student |
| **Exam** | POST | `/api/exams` | Create exam session |
| | GET | `/api/exams` | List all exam sessions |
| | GET | `/api/exams/{id}` | Get exam session by ID |
| | PUT | `/api/exams/{id}` | Update exam session |
| | DELETE | `/api/exams/{id}` | Delete exam session |
| **Registration** | POST | `/api/exams/{examId}/students` | Register list of student IDs for exam |
| | GET | `/api/exams/{examId}/students` | List students registered for exam |
| **Seating** | POST | `/api/exams/{examId}/generate-seating` | Run auto seating allocation |
| | POST | `/api/exams/{examId}/regenerate-seating` | Reset & re-run seating allocation |
| | GET | `/api/exams/{examId}/seating-chart` | Get hall-wise 2D seating chart |
| | DELETE | `/api/exams/{examId}/allocations` | Delete all allocations for exam |
| **Lookup** | GET | `/api/students/{rollNumber}/allocation/{examId}` | Lookup student's seat & hall |

---

## 🚀 Installation & Setup Guide

### Step 1: MySQL Database Setup

1. Open **MySQL Workbench** or MySQL Command Line Client.
2. Run the database creation SQL command:
   ```sql
   CREATE DATABASE seatplan_db;
   ```
3. Alternatively, execute the provided setup script:
   ```bash
   mysql -u root -p < setup.sql
   ```

### Step 2: Configure Database Credentials

Open `src/main/resources/application.properties` and verify your MySQL connection details:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seatplan_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

### Step 3: Build & Run the Application

#### Using Maven:
```bash
mvn clean spring-boot:run
```

#### Using Executable JAR:
```bash
mvn clean package -DskipTests
java -jar target/seatplan-1.0.0.jar
```

The application will start on `http://localhost:8080`.

---

## 📖 Swagger / OpenAPI Documentation

Once the application is running, access Swagger UI in your browser:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🧪 Postman API Testing Guide

### Testing Sequence:

#### 1. Register Examination Hall
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/halls`
- **Headers**: `Content-Type: application/json`
- **Body**:
```json
{
  "hallNumber": "H101",
  "name": "Main Examination Hall",
  "rows": 5,
  "columns": 6,
  "capacity": 30
}
```
- **Expected Response (201 Created)**:
```json
{
  "id": 1,
  "hallNumber": "H101",
  "name": "Main Examination Hall",
  "rows": 5,
  "columns": 6,
  "capacity": 30,
  "totalGeneratedSeats": 30
}
```

#### 2. Register Students
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/students`
- **Body**:
```json
{
  "rollNumber": "23CS101",
  "name": "Arun Kumar",
  "className": "CSE",
  "section": "A"
}
```
- **Expected Response (201 Created)**:
```json
{
  "id": 1,
  "rollNumber": "23CS101",
  "name": "Arun Kumar",
  "className": "CSE",
  "section": "A"
}
```

#### 3. Create Exam Session
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/exams`
- **Body**:
```json
{
  "examName": "Semester Examination",
  "subject": "Database Management Systems",
  "examDate": "2026-10-15",
  "startTime": "09:30",
  "endTime": "12:30"
}
```
- **Expected Response (201 Created)**:
```json
{
  "id": 1,
  "examName": "Semester Examination",
  "subject": "Database Management Systems",
  "examDate": "2026-10-15",
  "startTime": "09:30",
  "endTime": "12:30",
  "registeredStudentsCount": 0,
  "allocatedSeatsCount": 0
}
```

#### 4. Register Students for Exam
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/exams/1/students`
- **Body**:
```json
{
  "studentIds": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25]
}
```

#### 5. Generate Seating Arrangement
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/exams/1/generate-seating`
- **Expected Response (200 OK)**:
```json
{
  "status": "SUCCESS",
  "examId": 1,
  "examName": "Semester Examination",
  "subject": "Database Management Systems",
  "registeredStudentsCount": 25,
  "allocatedSeatsCount": 25,
  "totalAvailableCapacity": 75,
  "constraintFullySatisfied": true,
  "message": "Seating arrangement generated successfully with optimal section distribution."
}
```

#### 6. View Seating Chart
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/exams/1/seating-chart`
- **Expected Response (200 OK)**:
```json
{
  "exam": "Semester Examination",
  "subject": "Database Management Systems",
  "halls": [
    {
      "hallNumber": "H101",
      "hallName": "Main Examination Hall",
      "totalRows": 5,
      "totalColumns": 6,
      "capacity": 30,
      "rows": [
        [
          {
            "seatNumber": "S1",
            "rowNumber": 1,
            "columnNumber": 1,
            "student": "Karthik Raja",
            "rollNumber": "23EC101",
            "className": "ECE",
            "section": "A"
          },
          {
            "seatNumber": "S2",
            "rowNumber": 1,
            "columnNumber": 2,
            "student": "Farooq Ahmed",
            "rollNumber": "23CS201",
            "className": "CSE",
            "section": "B"
          }
        ]
      ]
    }
  ]
}
```

#### 7. Student Seat Lookup
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/students/23CS101/allocation/1`
- **Expected Response (200 OK)**:
```json
{
  "rollNumber": "23CS101",
  "studentName": "Arun Kumar",
  "exam": "Semester Examination",
  "hallNumber": "H101",
  "seatNumber": "S5",
  "row": 1,
  "column": 5
}
```

---

## ❌ Validation & Error Case Demonstrations

#### Failure Case 1: Insufficient Seats
- Try generating seating when registered students count (e.g. 75) > total hall capacity (e.g. 30).
- **HTTP Status**: `400 Bad Request`
- **Response Body**:
```json
{
  "timestamp": "2026-09-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Insufficient seats. 75 students registered but only 30 seats available."
}
```

#### Failure Case 2: Duplicate Hall Number
- **HTTP Status**: `409 Conflict`
- **Response Body**:
```json
{
  "timestamp": "2026-09-28T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Hall with hallNumber 'H101' already exists."
}
```

#### Failure Case 3: Student Lookup Not Found
- Search for a student roll number that is not allocated.
- **HTTP Status**: `404 Not Found`
- **Response Body**:
```json
{
  "timestamp": "2026-09-28T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student has not been allocated a seat for this exam."
}
```

---

## 📄 License

Distributed under the Apache 2.0 License.
