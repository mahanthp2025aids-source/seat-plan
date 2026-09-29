package com.seatplan.controller;

import com.seatplan.dto.SeatingChartResponse;
import com.seatplan.dto.SeatingResponse;
import com.seatplan.service.SeatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@Tag(name = "Seating Allocation Management", description = "APIs for generating seating arrangements and seating charts")
public class SeatingController {

    private final SeatingService seatingService;

    public SeatingController(SeatingService seatingService) {
        this.seatingService = seatingService;
    }

    @PostMapping("/{examId}/generate-seating")
    @Operation(summary = "Automatically generate seating arrangement for an exam session")
    public ResponseEntity<SeatingResponse> generateSeating(@PathVariable Long examId) {
        SeatingResponse response = seatingService.generateSeating(examId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{examId}/regenerate-seating")
    @Operation(summary = "Regenerate seating arrangement for an exam session (clears previous allocations)")
    public ResponseEntity<SeatingResponse> regenerateSeating(@PathVariable Long examId) {
        SeatingResponse response = seatingService.regenerateSeating(examId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{examId}/seating-chart")
    @Operation(summary = "Get printable seating chart grouped by examination halls")
    public ResponseEntity<SeatingChartResponse> getSeatingChart(@PathVariable Long examId) {
        SeatingChartResponse response = seatingService.getSeatingChart(examId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{examId}/allocations")
    @Operation(summary = "Delete seating allocations for an exam session")
    public ResponseEntity<Void> clearAllocations(@PathVariable Long examId) {
        seatingService.clearAllocations(examId);
        return ResponseEntity.noContent().build();
    }
}
