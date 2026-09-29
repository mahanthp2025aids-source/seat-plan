package com.seatplan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "column_number", nullable = false)
    private int columnNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    @JsonIgnore
    private Hall hall;

    public Seat() {}

    public Seat(Long id, String seatNumber, int rowNumber, int columnNumber, Hall hall) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.hall = hall;
    }

    public static SeatBuilder builder() {
        return new SeatBuilder();
    }

    public static class SeatBuilder {
        private Long id;
        private String seatNumber;
        private int rowNumber;
        private int columnNumber;
        private Hall hall;

        public SeatBuilder id(Long id) { this.id = id; return this; }
        public SeatBuilder seatNumber(String seatNumber) { this.seatNumber = seatNumber; return this; }
        public SeatBuilder rowNumber(int rowNumber) { this.rowNumber = rowNumber; return this; }
        public SeatBuilder columnNumber(int columnNumber) { this.columnNumber = columnNumber; return this; }
        public SeatBuilder hall(Hall hall) { this.hall = hall; return this; }

        public Seat build() {
            return new Seat(id, seatNumber, rowNumber, columnNumber, hall);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }

    public int getColumnNumber() { return columnNumber; }
    public void setColumnNumber(int columnNumber) { this.columnNumber = columnNumber; }

    public Hall getHall() { return hall; }
    public void setHall(Hall hall) { this.hall = hall; }
}
