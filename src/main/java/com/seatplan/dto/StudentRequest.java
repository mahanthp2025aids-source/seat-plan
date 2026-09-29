package com.seatplan.dto;

import jakarta.validation.constraints.NotBlank;

public class StudentRequest {

    @NotBlank(message = "Roll number cannot be empty")
    private String rollNumber;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Class name cannot be empty")
    private String className;

    @NotBlank(message = "Section cannot be empty")
    private String section;

    public StudentRequest() {}

    public StudentRequest(String rollNumber, String name, String className, String section) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.className = className;
        this.section = section;
    }

    public static StudentRequestBuilder builder() {
        return new StudentRequestBuilder();
    }

    public static class StudentRequestBuilder {
        private String rollNumber;
        private String name;
        private String className;
        private String section;

        public StudentRequestBuilder rollNumber(String rollNumber) { this.rollNumber = rollNumber; return this; }
        public StudentRequestBuilder name(String name) { this.name = name; return this; }
        public StudentRequestBuilder className(String className) { this.className = className; return this; }
        public StudentRequestBuilder section(String section) { this.section = section; return this; }

        public StudentRequest build() {
            return new StudentRequest(rollNumber, name, className, section);
        }
    }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
}
