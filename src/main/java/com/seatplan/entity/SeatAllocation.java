package com.seatplan.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seat_allocations", uniqueConstraints = {
    @UniqueConstraint(name = "uk_exam_student", columnNames = {"exam_session_id", "student_id"}),
    @UniqueConstraint(name = "uk_exam_seat", columnNames = {"exam_session_id", "seat_id"})
})
public class SeatAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession examSession;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "allocated_at", nullable = false)
    private LocalDateTime allocatedAt;

    public SeatAllocation() {}

    public SeatAllocation(Long id, ExamSession examSession, Student student, Seat seat, LocalDateTime allocatedAt) {
        this.id = id;
        this.examSession = examSession;
        this.student = student;
        this.seat = seat;
        this.allocatedAt = allocatedAt;
    }

    public static SeatAllocationBuilder builder() {
        return new SeatAllocationBuilder();
    }

    public static class SeatAllocationBuilder {
        private Long id;
        private ExamSession examSession;
        private Student student;
        private Seat seat;
        private LocalDateTime allocatedAt;

        public SeatAllocationBuilder id(Long id) { this.id = id; return this; }
        public SeatAllocationBuilder examSession(ExamSession examSession) { this.examSession = examSession; return this; }
        public SeatAllocationBuilder student(Student student) { this.student = student; return this; }
        public SeatAllocationBuilder seat(Seat seat) { this.seat = seat; return this; }
        public SeatAllocationBuilder allocatedAt(LocalDateTime allocatedAt) { this.allocatedAt = allocatedAt; return this; }

        public SeatAllocation build() {
            return new SeatAllocation(id, examSession, student, seat, allocatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ExamSession getExamSession() { return examSession; }
    public void setExamSession(ExamSession examSession) { this.examSession = examSession; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    public LocalDateTime getAllocatedAt() { return allocatedAt; }
    public void setAllocatedAt(LocalDateTime allocatedAt) { this.allocatedAt = allocatedAt; }
}
