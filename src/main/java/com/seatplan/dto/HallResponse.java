package com.seatplan.dto;

public class HallResponse {
    private Long id;
    private String hallNumber;
    private String name;
    private int rows;
    private int columns;
    private int capacity;
    private int totalGeneratedSeats;

    public HallResponse() {}

    public HallResponse(Long id, String hallNumber, String name, int rows, int columns, int capacity, int totalGeneratedSeats) {
        this.id = id;
        this.hallNumber = hallNumber;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.capacity = capacity;
        this.totalGeneratedSeats = totalGeneratedSeats;
    }

    public static HallResponseBuilder builder() {
        return new HallResponseBuilder();
    }

    public static class HallResponseBuilder {
        private Long id;
        private String hallNumber;
        private String name;
        private int rows;
        private int columns;
        private int capacity;
        private int totalGeneratedSeats;

        public HallResponseBuilder id(Long id) { this.id = id; return this; }
        public HallResponseBuilder hallNumber(String hallNumber) { this.hallNumber = hallNumber; return this; }
        public HallResponseBuilder name(String name) { this.name = name; return this; }
        public HallResponseBuilder rows(int rows) { this.rows = rows; return this; }
        public HallResponseBuilder columns(int columns) { this.columns = columns; return this; }
        public HallResponseBuilder capacity(int capacity) { this.capacity = capacity; return this; }
        public HallResponseBuilder totalGeneratedSeats(int totalGeneratedSeats) { this.totalGeneratedSeats = totalGeneratedSeats; return this; }

        public HallResponse build() {
            return new HallResponse(id, hallNumber, name, rows, columns, capacity, totalGeneratedSeats);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHallNumber() { return hallNumber; }
    public void setHallNumber(String hallNumber) { this.hallNumber = hallNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getRows() { return rows; }
    public void setRows(int rows) { this.rows = rows; }

    public int getColumns() { return columns; }
    public void setColumns(int columns) { this.columns = columns; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getTotalGeneratedSeats() { return totalGeneratedSeats; }
    public void setTotalGeneratedSeats(int totalGeneratedSeats) { this.totalGeneratedSeats = totalGeneratedSeats; }
}
