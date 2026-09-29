package com.seatplan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExamRequest {

    @NotBlank(message = "Exam name cannot be blank")
    private String examName;

    @NotBlank(message = "Subject cannot be blank")
    private String subject;

    @NotNull(message = "Exam date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate examDate;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public ExamRequest() {}

    public ExamRequest(String examName, String subject, LocalDate examDate, LocalTime startTime, LocalTime endTime) {
        this.examName = examName;
        this.subject = subject;
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ExamRequestBuilder builder() {
        return new ExamRequestBuilder();
    }

    public static class ExamRequestBuilder {
        private String examName;
        private String subject;
        private LocalDate examDate;
        private LocalTime startTime;
        private LocalTime endTime;

        public ExamRequestBuilder examName(String examName) { this.examName = examName; return this; }
        public ExamRequestBuilder subject(String subject) { this.subject = subject; return this; }
        public ExamRequestBuilder examDate(LocalDate examDate) { this.examDate = examDate; return this; }
        public ExamRequestBuilder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public ExamRequestBuilder endTime(LocalTime endTime) { this.endTime = endTime; return this; }

        public ExamRequest build() {
            return new ExamRequest(examName, subject, examDate, startTime, endTime);
        }
    }

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
}
