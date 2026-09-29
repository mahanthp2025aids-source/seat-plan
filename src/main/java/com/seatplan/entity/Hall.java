package com.seatplan.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hall_number", nullable = false, unique = true)
    private String hallNumber;

    @Column(nullable = false)
    private String name;

    @Column(name = "hall_rows", nullable = false)
    private int rows;

    @Column(name = "hall_columns", nullable = false)
    private int columns;

    @Column(nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    public Hall() {}

    public Hall(Long id, String hallNumber, String name, int rows, int columns, int capacity, List<Seat> seats) {
        this.id = id;
        this.hallNumber = hallNumber;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.capacity = capacity;
        if (seats != null) {
            this.seats = seats;
        }
    }

    public static HallBuilder builder() {
        return new HallBuilder();
    }

    public static class HallBuilder {
        private Long id;
        private String hallNumber;
        private String name;
        private int rows;
        private int columns;
        private int capacity;
        private List<Seat> seats = new ArrayList<>();

        public HallBuilder id(Long id) { this.id = id; return this; }
        public HallBuilder hallNumber(String hallNumber) { this.hallNumber = hallNumber; return this; }
        public HallBuilder name(String name) { this.name = name; return this; }
        public HallBuilder rows(int rows) { this.rows = rows; return this; }
        public HallBuilder columns(int columns) { this.columns = columns; return this; }
        public HallBuilder capacity(int capacity) { this.capacity = capacity; return this; }
        public HallBuilder seats(List<Seat> seats) { if (seats != null) this.seats = seats; return this; }

        public Hall build() {
            return new Hall(id, hallNumber, name, rows, columns, capacity, seats);
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

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
}
