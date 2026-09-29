package com.seatplan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HallRequest {

    @NotBlank(message = "Hall number cannot be blank")
    private String hallNumber;

    @NotBlank(message = "Hall name cannot be blank")
    private String name;

    @NotNull(message = "Rows count is required")
    @Min(value = 1, message = "Rows must be greater than 0")
    private Integer rows;

    @NotNull(message = "Columns count is required")
    @Min(value = 1, message = "Columns must be greater than 0")
    private Integer columns;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;

    public HallRequest() {}

    public HallRequest(String hallNumber, String name, Integer rows, Integer columns, Integer capacity) {
        this.hallNumber = hallNumber;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.capacity = capacity;
    }

    public static HallRequestBuilder builder() {
        return new HallRequestBuilder();
    }

    public static class HallRequestBuilder {
        private String hallNumber;
        private String name;
        private Integer rows;
        private Integer columns;
        private Integer capacity;

        public HallRequestBuilder hallNumber(String hallNumber) { this.hallNumber = hallNumber; return this; }
        public HallRequestBuilder name(String name) { this.name = name; return this; }
        public HallRequestBuilder rows(Integer rows) { this.rows = rows; return this; }
        public HallRequestBuilder columns(Integer columns) { this.columns = columns; return this; }
        public HallRequestBuilder capacity(Integer capacity) { this.capacity = capacity; return this; }

        public HallRequest build() {
            return new HallRequest(hallNumber, name, rows, columns, capacity);
        }
    }

    public String getHallNumber() { return hallNumber; }
    public void setHallNumber(String hallNumber) { this.hallNumber = hallNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getRows() { return rows; }
    public void setRows(Integer rows) { this.rows = rows; }

    public Integer getColumns() { return columns; }
    public void setColumns(Integer columns) { this.columns = columns; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}
