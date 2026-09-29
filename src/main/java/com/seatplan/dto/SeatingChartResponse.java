package com.seatplan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class SeatingChartResponse {
    private String exam;
    private String subject;
    private List<HallSeatingChart> halls;

    public SeatingChartResponse() {}

    public SeatingChartResponse(String exam, String subject, List<HallSeatingChart> halls) {
        this.exam = exam;
        this.subject = subject;
        this.halls = halls;
    }

    public static SeatingChartResponseBuilder builder() {
        return new SeatingChartResponseBuilder();
    }

    public static class SeatingChartResponseBuilder {
        private String exam;
        private String subject;
        private List<HallSeatingChart> halls;

        public SeatingChartResponseBuilder exam(String exam) { this.exam = exam; return this; }
        public SeatingChartResponseBuilder subject(String subject) { this.subject = subject; return this; }
        public SeatingChartResponseBuilder halls(List<HallSeatingChart> halls) { this.halls = halls; return this; }

        public SeatingChartResponse build() {
            return new SeatingChartResponse(exam, subject, halls);
        }
    }

    public String getExam() { return exam; }
    public void setExam(String exam) { this.exam = exam; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public List<HallSeatingChart> getHalls() { return halls; }
    public void setHalls(List<HallSeatingChart> halls) { this.halls = halls; }

    public static class HallSeatingChart {
        private String hallNumber;
        private String hallName;
        private int totalRows;
        private int totalColumns;
        private int capacity;
        private List<List<SeatCell>> rows;

        public HallSeatingChart() {}

        public HallSeatingChart(String hallNumber, String hallName, int totalRows, int totalColumns, int capacity, List<List<SeatCell>> rows) {
            this.hallNumber = hallNumber;
            this.hallName = hallName;
            this.totalRows = totalRows;
            this.totalColumns = totalColumns;
            this.capacity = capacity;
            this.rows = rows;
        }

        public static HallSeatingChartBuilder builder() {
            return new HallSeatingChartBuilder();
        }

        public static class HallSeatingChartBuilder {
            private String hallNumber;
            private String hallName;
            private int totalRows;
            private int totalColumns;
            private int capacity;
            private List<List<SeatCell>> rows;

            public HallSeatingChartBuilder hallNumber(String hallNumber) { this.hallNumber = hallNumber; return this; }
            public HallSeatingChartBuilder hallName(String hallName) { this.hallName = hallName; return this; }
            public HallSeatingChartBuilder totalRows(int totalRows) { this.totalRows = totalRows; return this; }
            public HallSeatingChartBuilder totalColumns(int totalColumns) { this.totalColumns = totalColumns; return this; }
            public HallSeatingChartBuilder capacity(int capacity) { this.capacity = capacity; return this; }
            public HallSeatingChartBuilder rows(List<List<SeatCell>> rows) { this.rows = rows; return this; }

            public HallSeatingChart build() {
                return new HallSeatingChart(hallNumber, hallName, totalRows, totalColumns, capacity, rows);
            }
        }

        public String getHallNumber() { return hallNumber; }
        public void setHallNumber(String hallNumber) { this.hallNumber = hallNumber; }

        public String getHallName() { return hallName; }
        public void setHallName(String hallName) { this.hallName = hallName; }

        public int getTotalRows() { return totalRows; }
        public void setTotalRows(int totalRows) { this.totalRows = totalRows; }

        public int getTotalColumns() { return totalColumns; }
        public void setTotalColumns(int totalColumns) { this.totalColumns = totalColumns; }

        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }

        public List<List<SeatCell>> getRows() { return rows; }
        public void setRows(List<List<SeatCell>> rows) { this.rows = rows; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SeatCell {
        private String seatNumber;
        private int rowNumber;
        private int columnNumber;
        private String student;
        private String rollNumber;
        private String className;
        private String section;

        public SeatCell() {}

        public SeatCell(String seatNumber, int rowNumber, int columnNumber, String student, String rollNumber, String className, String section) {
            this.seatNumber = seatNumber;
            this.rowNumber = rowNumber;
            this.columnNumber = columnNumber;
            this.student = student;
            this.rollNumber = rollNumber;
            this.className = className;
            this.section = section;
        }

        public static SeatCellBuilder builder() {
            return new SeatCellBuilder();
        }

        public static class SeatCellBuilder {
            private String seatNumber;
            private int rowNumber;
            private int columnNumber;
            private String student;
            private String rollNumber;
            private String className;
            private String section;

            public SeatCellBuilder seatNumber(String seatNumber) { this.seatNumber = seatNumber; return this; }
            public SeatCellBuilder rowNumber(int rowNumber) { this.rowNumber = rowNumber; return this; }
            public SeatCellBuilder columnNumber(int columnNumber) { this.columnNumber = columnNumber; return this; }
            public SeatCellBuilder student(String student) { this.student = student; return this; }
            public SeatCellBuilder rollNumber(String rollNumber) { this.rollNumber = rollNumber; return this; }
            public SeatCellBuilder className(String className) { this.className = className; return this; }
            public SeatCellBuilder section(String section) { this.section = section; return this; }

            public SeatCell build() {
                return new SeatCell(seatNumber, rowNumber, columnNumber, student, rollNumber, className, section);
            }
        }

        public String getSeatNumber() { return seatNumber; }
        public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

        public int getRowNumber() { return rowNumber; }
        public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }

        public int getColumnNumber() { return columnNumber; }
        public void setColumnNumber(int columnNumber) { this.columnNumber = columnNumber; }

        public String getStudent() { return student; }
        public void setStudent(String student) { this.student = student; }

        public String getRollNumber() { return rollNumber; }
        public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }

        public String getSection() { return section; }
        public void setSection(String section) { this.section = section; }
    }
}
