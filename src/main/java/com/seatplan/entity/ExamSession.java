package com.seatplan.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exam_sessions")
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_name", nullable = false)
    private String examName;

    @Column(nullable = false)
    private String subject;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "examSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamRegistration> registrations = new ArrayList<>();

    @OneToMany(mappedBy = "examSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatAllocation> allocations = new ArrayList<>();

    public ExamSession() {}

    public ExamSession(Long id, String examName, String subject, LocalDate examDate, LocalTime startTime, LocalTime endTime, List<ExamRegistration> registrations, List<SeatAllocation> allocations) {
        this.id = id;
        this.examName = examName;
        this.subject = subject;
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = endTime;
        if (registrations != null) this.registrations = registrations;
        if (allocations != null) this.allocations = allocations;
    }

    public static ExamSessionBuilder builder() {
        return new ExamSessionBuilder();
    }

    public static class ExamSessionBuilder {
        private Long id;
        private String examName;
        private String subject;
        private LocalDate examDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private List<ExamRegistration> registrations = new ArrayList<>();
        private List<SeatAllocation> allocations = new ArrayList<>();

        public ExamSessionBuilder id(Long id) { this.id = id; return this; }
        public ExamSessionBuilder examName(String examName) { this.examName = examName; return this; }
        public ExamSessionBuilder subject(String subject) { this.subject = subject; return this; }
        public ExamSessionBuilder examDate(LocalDate examDate) { this.examDate = examDate; return this; }
        public ExamSessionBuilder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public ExamSessionBuilder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public ExamSessionBuilder registrations(List<ExamRegistration> registrations) { if (registrations != null) this.registrations = registrations; return this; }
        public ExamSessionBuilder allocations(List<SeatAllocation> allocations) { if (allocations != null) this.allocations = allocations; return this; }

        public ExamSession build() {
            return new ExamSession(id, examName, subject, examDate, startTime, endTime, registrations, allocations);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public List<ExamRegistration> getRegistrations() { return registrations; }
    public void setRegistrations(List<ExamRegistration> registrations) { this.registrations = registrations; }

    public List<SeatAllocation> getAllocations() { return allocations; }
    public void setAllocations(List<SeatAllocation> allocations) { this.allocations = allocations; }
}
