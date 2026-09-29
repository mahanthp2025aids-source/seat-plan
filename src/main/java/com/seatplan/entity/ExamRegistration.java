package com.seatplan.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_registrations", uniqueConstraints = {
    @UniqueConstraint(name = "uk_student_exam", columnNames = {"student_id", "exam_session_id"})
})
public class ExamRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession examSession;

    public ExamRegistration() {}

    public ExamRegistration(Long id, Student student, ExamSession examSession) {
        this.id = id;
        this.student = student;
        this.examSession = examSession;
    }

    public static ExamRegistrationBuilder builder() {
        return new ExamRegistrationBuilder();
    }

    public static class ExamRegistrationBuilder {
        private Long id;
        private Student student;
        private ExamSession examSession;

        public ExamRegistrationBuilder id(Long id) { this.id = id; return this; }
        public ExamRegistrationBuilder student(Student student) { this.student = student; return this; }
        public ExamRegistrationBuilder examSession(ExamSession examSession) { this.examSession = examSession; return this; }

        public ExamRegistration build() {
            return new ExamRegistration(id, student, examSession);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public ExamSession getExamSession() { return examSession; }
    public void setExamSession(ExamSession examSession) { this.examSession = examSession; }
}
