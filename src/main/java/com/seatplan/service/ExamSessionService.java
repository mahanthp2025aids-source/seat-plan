package com.seatplan.service;

import com.seatplan.dto.ExamRequest;
import com.seatplan.dto.ExamResponse;
import com.seatplan.dto.StudentResponse;
import com.seatplan.entity.ExamRegistration;
import com.seatplan.entity.ExamSession;
import com.seatplan.entity.Student;
import com.seatplan.exception.ResourceNotFoundException;
import com.seatplan.repository.ExamRegistrationRepository;
import com.seatplan.repository.ExamSessionRepository;
import com.seatplan.repository.SeatAllocationRepository;
import com.seatplan.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamSessionService {

    private final ExamSessionRepository examSessionRepository;
    private final StudentRepository studentRepository;
    private final ExamRegistrationRepository examRegistrationRepository;
    private final SeatAllocationRepository seatAllocationRepository;

    public ExamSessionService(ExamSessionRepository examSessionRepository,
                              StudentRepository studentRepository,
                              ExamRegistrationRepository examRegistrationRepository,
                              SeatAllocationRepository seatAllocationRepository) {
        this.examSessionRepository = examSessionRepository;
        this.studentRepository = studentRepository;
        this.examRegistrationRepository = examRegistrationRepository;
        this.seatAllocationRepository = seatAllocationRepository;
    }

    @Transactional
    public ExamResponse createExam(ExamRequest request) {
        ExamSession examSession = ExamSession.builder()
                .examName(request.getExamName())
                .subject(request.getSubject())
                .examDate(request.getExamDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .registrations(new ArrayList<>())
                .allocations(new ArrayList<>())
                .build();

        ExamSession savedExam = examSessionRepository.save(examSession);
        return mapToResponse(savedExam);
    }

    public List<ExamResponse> getAllExams() {
        return examSessionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ExamResponse getExamById(Long id) {
        ExamSession examSession = examSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam session not found with ID: " + id));
        return mapToResponse(examSession);
    }

    @Transactional
    public ExamResponse updateExam(Long id, ExamRequest request) {
        ExamSession examSession = examSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam session not found with ID: " + id));

        examSession.setExamName(request.getExamName());
        examSession.setSubject(request.getSubject());
        examSession.setExamDate(request.getExamDate());
        examSession.setStartTime(request.getStartTime());
        examSession.setEndTime(request.getEndTime());

        ExamSession updatedExam = examSessionRepository.save(examSession);
        return mapToResponse(updatedExam);
    }

    @Transactional
    public void deleteExam(Long id) {
        if (!examSessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exam session not found with ID: " + id);
        }
        examSessionRepository.deleteById(id);
    }

    @Transactional
    public List<StudentResponse> registerStudentsForExam(Long examId, List<Long> studentIds) {
        ExamSession examSession = examSessionRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam session not found with ID: " + examId));

        for (Long studentId : studentIds) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

            if (examRegistrationRepository.existsByExamSessionIdAndStudentId(examId, studentId)) {
                continue;
            }

            ExamRegistration registration = ExamRegistration.builder()
                    .examSession(examSession)
                    .student(student)
                    .build();

            examRegistrationRepository.save(registration);
        }

        return examRegistrationRepository.findStudentsByExamSessionId(examId).stream()
                .map(this::mapStudentToResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getRegisteredStudents(Long examId) {
        if (!examSessionRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam session not found with ID: " + examId);
        }

        return examRegistrationRepository.findStudentsByExamSessionId(examId).stream()
                .map(this::mapStudentToResponse)
                .collect(Collectors.toList());
    }

    private ExamResponse mapToResponse(ExamSession examSession) {
        int registeredCount = examRegistrationRepository.findStudentsByExamSessionId(examSession.getId()).size();
        int allocatedCount = seatAllocationRepository.findByExamSessionId(examSession.getId()).size();

        return ExamResponse.builder()
                .id(examSession.getId())
                .examName(examSession.getExamName())
                .subject(examSession.getSubject())
                .examDate(examSession.getExamDate())
                .startTime(examSession.getStartTime())
                .endTime(examSession.getEndTime())
                .registeredStudentsCount(registeredCount)
                .allocatedSeatsCount(allocatedCount)
                .build();
    }

    private StudentResponse mapStudentToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .rollNumber(student.getRollNumber())
                .name(student.getName())
                .className(student.getClassName())
                .section(student.getSection())
                .build();
    }
}
