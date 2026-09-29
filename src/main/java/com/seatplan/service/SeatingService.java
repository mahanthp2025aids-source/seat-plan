package com.seatplan.service;

import com.seatplan.dto.SeatingChartResponse;
import com.seatplan.dto.SeatingResponse;
import com.seatplan.dto.StudentAllocationResponse;
import com.seatplan.entity.ExamSession;
import com.seatplan.entity.Hall;
import com.seatplan.entity.Seat;
import com.seatplan.entity.SeatAllocation;
import com.seatplan.entity.Student;
import com.seatplan.exception.AllocationException;
import com.seatplan.exception.CapacityExceededException;
import com.seatplan.exception.ResourceNotFoundException;
import com.seatplan.repository.ExamRegistrationRepository;
import com.seatplan.repository.ExamSessionRepository;
import com.seatplan.repository.HallRepository;
import com.seatplan.repository.SeatAllocationRepository;
import com.seatplan.repository.SeatRepository;
import com.seatplan.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatingService {

    private final ExamSessionRepository examSessionRepository;
    private final ExamRegistrationRepository examRegistrationRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final SeatAllocationRepository seatAllocationRepository;
    private final StudentRepository studentRepository;

    public SeatingService(ExamSessionRepository examSessionRepository,
                          ExamRegistrationRepository examRegistrationRepository,
                          HallRepository hallRepository,
                          SeatRepository seatRepository,
                          SeatAllocationRepository seatAllocationRepository,
                          StudentRepository studentRepository) {
        this.examSessionRepository = examSessionRepository;
        this.examRegistrationRepository = examRegistrationRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
        this.seatAllocationRepository = seatAllocationRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public SeatingResponse generateSeating(Long examId) {
        ExamSession examSession = examSessionRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam session not found with ID: " + examId));

        if (seatAllocationRepository.existsByExamSessionId(examId)) {
            throw new AllocationException("Seating arrangement already generated for this exam. Use regenerate endpoint to regenerate.");
        }

        List<Student> registeredStudents = examRegistrationRepository.findStudentsByExamSessionId(examId);
        if (registeredStudents.isEmpty()) {
            throw new AllocationException("No students registered for exam ID: " + examId);
        }

        List<Hall> halls = hallRepository.findAll();
        if (halls.isEmpty()) {
            throw new AllocationException("No examination halls registered in the system.");
        }

        int totalAvailableCapacity = halls.stream().mapToInt(Hall::getCapacity).sum();
        if (registeredStudents.size() > totalAvailableCapacity) {
            throw new CapacityExceededException("Insufficient seats. " + registeredStudents.size() +
                    " students registered but only " + totalAvailableCapacity + " seats available.");
        }

        // Group registered students by section key (ClassName-Section)
        Map<String, Queue<Student>> sectionQueues = new HashMap<>();
        for (Student student : registeredStudents) {
            String key = student.getClassName() + "-" + student.getSection();
            sectionQueues.computeIfAbsent(key, k -> new LinkedList<>()).add(student);
        }

        List<SeatAllocation> allocations = new ArrayList<>();
        boolean constraintFullySatisfied = true;
        int remainingStudentCount = registeredStudents.size();

        // Sort halls by hallNumber
        halls.sort(Comparator.comparing(Hall::getHallNumber));

        for (Hall hall : halls) {
            if (remainingStudentCount == 0) {
                break;
            }

            List<Seat> seats = seatRepository.findByHallIdOrderByRowNumberAscColumnNumberAsc(hall.getId());
            String[][] grid = new String[hall.getRows() + 1][hall.getColumns() + 1];

            for (Seat seat : seats) {
                if (remainingStudentCount == 0) {
                    break;
                }

                int r = seat.getRowNumber();
                int c = seat.getColumnNumber();

                String leftNeighbor = (c > 1) ? grid[r][c - 1] : null;
                String topNeighbor = (r > 1) ? grid[r - 1][c] : null;

                // Candidate selection strategy
                String selectedKey = chooseBestSectionKey(sectionQueues, leftNeighbor, topNeighbor);

                if (selectedKey == null) {
                    // Fallback to any section with remaining students
                    selectedKey = getAnyRemainingSectionKey(sectionQueues);
                    constraintFullySatisfied = false;
                } else if (isConstraintViolated(selectedKey, leftNeighbor, topNeighbor)) {
                    constraintFullySatisfied = false;
                }

                if (selectedKey == null) {
                    break;
                }

                Student student = sectionQueues.get(selectedKey).poll();
                grid[r][c] = selectedKey;

                SeatAllocation allocation = SeatAllocation.builder()
                        .examSession(examSession)
                        .student(student)
                        .seat(seat)
                        .allocatedAt(LocalDateTime.now())
                        .build();

                allocations.add(allocation);
                remainingStudentCount--;
            }
        }

        seatAllocationRepository.saveAll(allocations);

        return SeatingResponse.builder()
                .status("SUCCESS")
                .examId(examSession.getId())
                .examName(examSession.getExamName())
                .subject(examSession.getSubject())
                .registeredStudentsCount(registeredStudents.size())
                .allocatedSeatsCount(allocations.size())
                .totalAvailableCapacity(totalAvailableCapacity)
                .constraintFullySatisfied(constraintFullySatisfied)
                .message(constraintFullySatisfied ?
                        "Seating arrangement generated successfully with optimal section distribution." :
                        "Seating arrangement generated. Note: Same-section adjacency constraint could not be fully avoided for all seats.")
                .build();
    }

    @Transactional
    public SeatingResponse regenerateSeating(Long examId) {
        if (!examSessionRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam session not found with ID: " + examId);
        }
        seatAllocationRepository.deleteByExamSessionId(examId);
        return generateSeating(examId);
    }

    @Transactional
    public void clearAllocations(Long examId) {
        if (!examSessionRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam session not found with ID: " + examId);
        }
        seatAllocationRepository.deleteByExamSessionId(examId);
    }

    public SeatingChartResponse getSeatingChart(Long examId) {
        ExamSession examSession = examSessionRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam session not found with ID: " + examId));

        List<SeatAllocation> allocations = seatAllocationRepository.findByExamSessionId(examId);
        Map<Long, SeatAllocation> seatAllocationMap = allocations.stream()
                .collect(Collectors.toMap(a -> a.getSeat().getId(), a -> a));

        List<Hall> halls = hallRepository.findAll();
        halls.sort(Comparator.comparing(Hall::getHallNumber));

        List<SeatingChartResponse.HallSeatingChart> hallCharts = new ArrayList<>();

        for (Hall hall : halls) {
            List<Seat> seats = seatRepository.findByHallIdOrderByRowNumberAscColumnNumberAsc(hall.getId());
            Map<Integer, List<Seat>> seatsByRow = seats.stream()
                    .collect(Collectors.groupingBy(Seat::getRowNumber, TreeMap::new, Collectors.toList()));

            List<List<SeatingChartResponse.SeatCell>> rowList = new ArrayList<>();

            for (Map.Entry<Integer, List<Seat>> rowEntry : seatsByRow.entrySet()) {
                List<Seat> rowSeats = rowEntry.getValue();
                rowSeats.sort(Comparator.comparing(Seat::getColumnNumber));

                List<SeatingChartResponse.SeatCell> cells = new ArrayList<>();
                for (Seat seat : rowSeats) {
                    SeatAllocation alloc = seatAllocationMap.get(seat.getId());

                    SeatingChartResponse.SeatCell cell = SeatingChartResponse.SeatCell.builder()
                            .seatNumber(seat.getSeatNumber())
                            .rowNumber(seat.getRowNumber())
                            .columnNumber(seat.getColumnNumber())
                            .student(alloc != null ? alloc.getStudent().getName() : null)
                            .rollNumber(alloc != null ? alloc.getStudent().getRollNumber() : null)
                            .className(alloc != null ? alloc.getStudent().getClassName() : null)
                            .section(alloc != null ? alloc.getStudent().getSection() : null)
                            .build();

                    cells.add(cell);
                }
                rowList.add(cells);
            }

            hallCharts.add(SeatingChartResponse.HallSeatingChart.builder()
                    .hallNumber(hall.getHallNumber())
                    .hallName(hall.getName())
                    .totalRows(hall.getRows())
                    .totalColumns(hall.getColumns())
                    .capacity(hall.getCapacity())
                    .rows(rowList)
                    .build());
        }

        return SeatingChartResponse.builder()
                .exam(examSession.getExamName())
                .subject(examSession.getSubject())
                .halls(hallCharts)
                .build();
    }

    public StudentAllocationResponse getStudentAllocation(String rollNumber, Long examId) {
        if (!studentRepository.existsByRollNumber(rollNumber)) {
            throw new ResourceNotFoundException("Student not found with roll number: " + rollNumber);
        }

        if (!examSessionRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam session not found with ID: " + examId);
        }

        SeatAllocation allocation = seatAllocationRepository.findByExamSessionIdAndStudentRollNumber(examId, rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student has not been allocated a seat for this exam."));

        return StudentAllocationResponse.builder()
                .rollNumber(allocation.getStudent().getRollNumber())
                .studentName(allocation.getStudent().getName())
                .exam(allocation.getExamSession().getExamName())
                .hallNumber(allocation.getSeat().getHall().getHallNumber())
                .seatNumber(allocation.getSeat().getSeatNumber())
                .row(allocation.getSeat().getRowNumber())
                .column(allocation.getSeat().getColumnNumber())
                .build();
    }

    private String chooseBestSectionKey(Map<String, Queue<Student>> sectionQueues, String leftNeighbor, String topNeighbor) {
        // Priority 1: Does not match left or top
        List<String> idealKeys = sectionQueues.keySet().stream()
                .filter(k -> !sectionQueues.get(k).isEmpty())
                .filter(k -> !k.equalsIgnoreCase(leftNeighbor) && !k.equalsIgnoreCase(topNeighbor))
                .collect(Collectors.toList());

        if (!idealKeys.isEmpty()) {
            return idealKeys.stream()
                    .max(Comparator.comparingInt(k -> sectionQueues.get(k).size()))
                    .orElse(null);
        }

        // Priority 2: Does not match left OR top (matches only 1 neighbor)
        List<String> partialKeys = sectionQueues.keySet().stream()
                .filter(k -> !sectionQueues.get(k).isEmpty())
                .filter(k -> !k.equalsIgnoreCase(leftNeighbor) || !k.equalsIgnoreCase(topNeighbor))
                .collect(Collectors.toList());

        if (!partialKeys.isEmpty()) {
            return partialKeys.stream()
                    .max(Comparator.comparingInt(k -> sectionQueues.get(k).size()))
                    .orElse(null);
        }

        return null;
    }

    private String getAnyRemainingSectionKey(Map<String, Queue<Student>> sectionQueues) {
        return sectionQueues.keySet().stream()
                .filter(k -> !sectionQueues.get(k).isEmpty())
                .max(Comparator.comparingInt(k -> sectionQueues.get(k).size()))
                .orElse(null);
    }

    private boolean isConstraintViolated(String key, String leftNeighbor, String topNeighbor) {
        if (key == null) return false;
        return key.equalsIgnoreCase(leftNeighbor) || key.equalsIgnoreCase(topNeighbor);
    }
}
