package com.seatplan.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roll_number", nullable = false, unique = true)
    private String rollNumber;

    @Column(nullable = false)
    private String name;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(nullable = false)
    private String section;

    public Student() {}

    public Student(Long id, String rollNumber, String name, String className, String section) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.name = name;
        this.className = className;
        this.section = section;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private Long id;
        private String rollNumber;
        private String name;
        private String className;
        private String section;

        public StudentBuilder id(Long id) { this.id = id; return this; }
        public StudentBuilder rollNumber(String rollNumber) { this.rollNumber = rollNumber; return this; }
        public StudentBuilder name(String name) { this.name = name; return this; }
        public StudentBuilder className(String className) { this.className = className; return this; }
        public StudentBuilder section(String section) { this.section = section; return this; }

        public Student build() {
            return new Student(id, rollNumber, name, className, section);
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
