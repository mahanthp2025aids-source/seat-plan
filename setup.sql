-- ==================================================
-- SeatPlan — MySQL Database Setup Script
-- ==================================================

-- 1. Create Database
CREATE DATABASE IF NOT EXISTS seatplan_db;
USE seatplan_db;

-- 2. Create Tables (Hibernate auto-generates these on startup, but SQL commands are provided for reference)

-- Table: halls
CREATE TABLE IF NOT EXISTS halls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hall_number VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    hall_rows INT NOT NULL,
    hall_columns INT NOT NULL,
    capacity INT NOT NULL
);

-- Table: seats
CREATE TABLE IF NOT EXISTS seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number VARCHAR(255) NOT NULL,
    row_number INT NOT NULL,
    column_number INT NOT NULL,
    hall_id BIGINT NOT NULL,
    FOREIGN KEY (hall_id) REFERENCES halls(id) ON DELETE CASCADE
);

-- Table: students
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    section VARCHAR(255) NOT NULL
);

-- Table: exam_sessions
CREATE TABLE IF NOT EXISTS exam_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_name VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    exam_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

-- Table: exam_registrations
CREATE TABLE IF NOT EXISTS exam_registrations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    exam_session_id BIGINT NOT NULL,
    UNIQUE KEY uk_student_exam (student_id, exam_session_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (exam_session_id) REFERENCES exam_sessions(id) ON DELETE CASCADE
);

-- Table: seat_allocations
CREATE TABLE IF NOT EXISTS seat_allocations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    allocated_at DATETIME NOT NULL,
    UNIQUE KEY uk_exam_student (exam_session_id, student_id),
    UNIQUE KEY uk_exam_seat (exam_session_id, seat_id),
    FOREIGN KEY (exam_session_id) REFERENCES exam_sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE CASCADE
);
