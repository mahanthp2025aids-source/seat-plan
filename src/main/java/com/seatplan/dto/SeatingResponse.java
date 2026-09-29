package com.seatplan.dto;

public class SeatingResponse {
    private String status;
    private Long examId;
    private String examName;
    private String subject;
    private int registeredStudentsCount;
    private int allocatedSeatsCount;
    private int totalAvailableCapacity;
    private boolean constraintFullySatisfied;
    private String message;

    public SeatingResponse() {}

    public SeatingResponse(String status, Long examId, String examName, String subject, int registeredStudentsCount, int allocatedSeatsCount, int totalAvailableCapacity, boolean constraintFullySatisfied, String message) {
        this.status = status;
        this.examId = examId;
        this.examName = examName;
        this.subject = subject;
        this.registeredStudentsCount = registeredStudentsCount;
        this.allocatedSeatsCount = allocatedSeatsCount;
        this.totalAvailableCapacity = totalAvailableCapacity;
        this.constraintFullySatisfied = constraintFullySatisfied;
        this.message = message;
    }

    public static SeatingResponseBuilder builder() {
        return new SeatingResponseBuilder();
    }

    public static class SeatingResponseBuilder {
        private String status;
        private Long examId;
        private String examName;
        private String subject;
        private int registeredStudentsCount;
        private int allocatedSeatsCount;
        private int totalAvailableCapacity;
        private boolean constraintFullySatisfied;
        private String message;

        public SeatingResponseBuilder status(String status) { this.status = status; return this; }
        public SeatingResponseBuilder examId(Long examId) { this.examId = examId; return this; }
        public SeatingResponseBuilder examName(String examName) { this.examName = examName; return this; }
        public SeatingResponseBuilder subject(String subject) { this.subject = subject; return this; }
        public SeatingResponseBuilder registeredStudentsCount(int registeredStudentsCount) { this.registeredStudentsCount = registeredStudentsCount; return this; }
        public SeatingResponseBuilder allocatedSeatsCount(int allocatedSeatsCount) { this.allocatedSeatsCount = allocatedSeatsCount; return this; }
        public SeatingResponseBuilder totalAvailableCapacity(int totalAvailableCapacity) { this.totalAvailableCapacity = totalAvailableCapacity; return this; }
        public SeatingResponseBuilder constraintFullySatisfied(boolean constraintFullySatisfied) { this.constraintFullySatisfied = constraintFullySatisfied; return this; }
        public SeatingResponseBuilder message(String message) { this.message = message; return this; }

        public SeatingResponse build() {
            return new SeatingResponse(status, examId, examName, subject, registeredStudentsCount, allocatedSeatsCount, totalAvailableCapacity, constraintFullySatisfied, message);
        }
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getRegisteredStudentsCount() { return registeredStudentsCount; }
    public void setRegisteredStudentsCount(int registeredStudentsCount) { this.registeredStudentsCount = registeredStudentsCount; }

    public int getAllocatedSeatsCount() { return allocatedSeatsCount; }
    public void setAllocatedSeatsCount(int allocatedSeatsCount) { this.allocatedSeatsCount = allocatedSeatsCount; }

    public int getTotalAvailableCapacity() { return totalAvailableCapacity; }
    public void setTotalAvailableCapacity(int totalAvailableCapacity) { this.totalAvailableCapacity = totalAvailableCapacity; }

    public boolean isConstraintFullySatisfied() { return constraintFullySatisfied; }
    public void setConstraintFullySatisfied(boolean constraintFullySatisfied) { this.constraintFullySatisfied = constraintFullySatisfied; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
