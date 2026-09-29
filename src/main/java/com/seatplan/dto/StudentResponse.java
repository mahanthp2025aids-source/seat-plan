package com.seatplan.dto;

public class StudentResponse {
    private Long id;
    private String rollNumber;
    private String name;
    private String className;
    private String section;

    public StudentResponse() {}

    public StudentResponse(Long id, String rollNumber, String name, String className, String section) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.name = name;
        this.className = className;
        this.section = section;
    }

    public static StudentResponseBuilder builder() {
        return new StudentResponseBuilder();
    }

    public static class StudentResponseBuilder {
        private Long id;
        private String rollNumber;
        private String name;
        private String className;
        private String section;

        public StudentResponseBuilder id(Long id) { this.id = id; return this; }
        public StudentResponseBuilder rollNumber(String rollNumber) { this.rollNumber = rollNumber; return this; }
        public StudentResponseBuilder name(String name) { this.name = name; return this; }
        public StudentResponseBuilder className(String className) { this.className = className; return this; }
        public StudentResponseBuilder section(String section) { this.section = section; return this; }

        public StudentResponse build() {
            return new StudentResponse(id, rollNumber, name, className, section);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
}
