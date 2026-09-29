package com.seatplan.dto;

public class StudentAllocationResponse {
    private String rollNumber;
    private String studentName;
    private String exam;
    private String hallNumber;
    private String seatNumber;
    private int row;
    private int column;

    public StudentAllocationResponse() {}

    public StudentAllocationResponse(String rollNumber, String studentName, String exam, String hallNumber, String seatNumber, int row, int column) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.exam = exam;
        this.hallNumber = hallNumber;
        this.seatNumber = seatNumber;
        this.row = row;
        this.column = column;
    }

    public static StudentAllocationResponseBuilder builder() {
        return new StudentAllocationResponseBuilder();
    }

    public static class StudentAllocationResponseBuilder {
        private String rollNumber;
        private String studentName;
        private String exam;
        private String hallNumber;
        private String seatNumber;
        private int row;
        private int column;

        public StudentAllocationResponseBuilder rollNumber(String rollNumber) { this.rollNumber = rollNumber; return this; }
        public StudentAllocationResponseBuilder studentName(String studentName) { this.studentName = studentName; return this; }
        public StudentAllocationResponseBuilder exam(String exam) { this.exam = exam; return this; }
        public StudentAllocationResponseBuilder hallNumber(String hallNumber) { this.hallNumber = hallNumber; return this; }
        public StudentAllocationResponseBuilder seatNumber(String seatNumber) { this.seatNumber = seatNumber; return this; }
        public StudentAllocationResponseBuilder row(int row) { this.row = row; return this; }
        public StudentAllocationResponseBuilder column(int column) { this.column = column; return this; }

        public StudentAllocationResponse build() {
            return new StudentAllocationResponse(rollNumber, studentName, exam, hallNumber, seatNumber, row, column);
        }
    }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getExam() { return exam; }
    public void setExam(String exam) { this.exam = exam; }

    public String getHallNumber() { return hallNumber; }
    public void setHallNumber(String hallNumber) { this.hallNumber = hallNumber; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }

    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
}
