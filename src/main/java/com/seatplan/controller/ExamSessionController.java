package com.seatplan.controller;

import com.seatplan.dto.ExamRegistrationRequest;
import com.seatplan.dto.ExamRequest;
import com.seatplan.dto.ExamResponse;
import com.seatplan.dto.StudentResponse;
import com.seatplan.service.ExamSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@Tag(name = "Exam Session Management", description = "APIs for creating exam sessions and registering students")
public class ExamSessionController {

    private final ExamSessionService examSessionService;

    public ExamSessionController(ExamSessionService examSessionService) {
        this.examSessionService = examSessionService;
    }

    @PostMapping
    @Operation(summary = "Create a new examination session")
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody ExamRequest request) {
        ExamResponse response = examSessionService.createExam(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get list of all examination sessions")
    public ResponseEntity<List<ExamResponse>> getAllExams() {
        return ResponseEntity.ok(examSessionService.getAllExams());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get examination session details by ID")
    public ResponseEntity<ExamResponse> getExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examSessionService.getExamById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing examination session")
    public ResponseEntity<ExamResponse> updateExam(@PathVariable Long id, @Valid @RequestBody ExamRequest request) {
        return ResponseEntity.ok(examSessionService.updateExam(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an examination session")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examSessionService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{examId}/students")
    @Operation(summary = "Register students for an examination session")
    public ResponseEntity<List<StudentResponse>> registerStudents(
            @PathVariable Long examId,
            @Valid @RequestBody ExamRegistrationRequest request) {
        List<StudentResponse> registeredStudents = examSessionService.registerStudentsForExam(examId, request.getStudentIds());
        return ResponseEntity.ok(registeredStudents);
    }

    @GetMapping("/{examId}/students")
    @Operation(summary = "Get list of students registered for an examination session")
    public ResponseEntity<List<StudentResponse>> getRegisteredStudents(@PathVariable Long examId) {
        return ResponseEntity.ok(examSessionService.getRegisteredStudents(examId));
    }
}
