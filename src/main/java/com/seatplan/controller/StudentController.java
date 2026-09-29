package com.seatplan.controller;

import com.seatplan.dto.StudentAllocationResponse;
import com.seatplan.dto.StudentRequest;
import com.seatplan.dto.StudentResponse;
import com.seatplan.service.SeatingService;
import com.seatplan.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "APIs for managing students and seat allocations lookup")
public class StudentController {

    private final StudentService studentService;
    private final SeatingService seatingService;

    public StudentController(StudentService studentService, SeatingService seatingService) {
        this.studentService = studentService;
        this.seatingService = seatingService;
    }

    @PostMapping
    @Operation(summary = "Register a new student")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get list of all students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student details by ID")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/roll/{rollNumber}")
    @Operation(summary = "Get student details by Roll Number")
    public ResponseEntity<StudentResponse> getStudentByRollNumber(@PathVariable String rollNumber) {
        return ResponseEntity.ok(studentService.getStudentByRollNumber(rollNumber));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing student")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{rollNumber}/allocation/{examId}")
    @Operation(summary = "Search student's allocated hall and seat for a specific exam session")
    public ResponseEntity<StudentAllocationResponse> getStudentAllocation(
            @PathVariable String rollNumber,
            @PathVariable Long examId) {
        return ResponseEntity.ok(seatingService.getStudentAllocation(rollNumber, examId));
    }
}
