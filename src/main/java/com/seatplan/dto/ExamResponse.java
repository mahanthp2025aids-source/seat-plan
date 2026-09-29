package com.seatplan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class ExamResponse {
    private Long id;
    private String examName;
    private String subject;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate examDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private int registeredStudentsCount;
    private int allocatedSeatsCount;

    public ExamResponse() {}

    public ExamResponse(Long id, String examName, String subject, LocalDate examDate, LocalTime startTime, LocalTime endTime, int registeredStudentsCount, int allocatedSeatsCount) {
        this.id = id;
        this.examName = examName;
        this.subject = subject;
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registeredStudentsCount = registeredStudentsCount;
        this.allocatedSeatsCount = allocatedSeatsCount;
    }

    public static ExamResponseBuilder builder() {
        return new ExamResponseBuilder();
    }

    public static class ExamResponseBuilder {
        private Long id;
        private String examName;
        private String subject;
        private LocalDate examDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private int registeredStudentsCount;
        private int allocatedSeatsCount;

        public ExamResponseBuilder id(Long id) { this.id = id; return this; }
        public ExamResponseBuilder examName(String examName) { this.examName = examName; return this; }
        public ExamResponseBuilder subject(String subject) { this.subject = subject; return this; }
        public ExamResponseBuilder examDate(LocalDate examDate) { this.examDate = examDate; return this; }
        public ExamResponseBuilder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public ExamResponseBuilder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public ExamResponseBuilder registeredStudentsCount(int registeredStudentsCount) { this.registeredStudentsCount = registeredStudentsCount; return this; }
        public ExamResponseBuilder allocatedSeatsCount(int allocatedSeatsCount) { this.allocatedSeatsCount = allocatedSeatsCount; return this; }

        public ExamResponse build() {
            return new ExamResponse(id, examName, subject, examDate, startTime, endTime, registeredStudentsCount, allocatedSeatsCount);
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

    public int getRegisteredStudentsCount() { return registeredStudentsCount; }
    public void setRegisteredStudentsCount(int registeredStudentsCount) { this.registeredStudentsCount = registeredStudentsCount; }

    public int getAllocatedSeatsCount() { return allocatedSeatsCount; }
    public void setAllocatedSeatsCount(int allocatedSeatsCount) { this.allocatedSeatsCount = allocatedSeatsCount; }
}
