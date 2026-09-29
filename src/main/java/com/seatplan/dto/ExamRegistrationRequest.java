package com.seatplan.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class ExamRegistrationRequest {

    @NotEmpty(message = "studentIds list cannot be empty")
    private List<Long> studentIds;

    public ExamRegistrationRequest() {}

    public ExamRegistrationRequest(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public List<Long> getStudentIds() { return studentIds; }
    public void setStudentIds(List<Long> studentIds) { this.studentIds = studentIds; }
}
