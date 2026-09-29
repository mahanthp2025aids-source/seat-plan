package com.seatplan.controller;

import com.seatplan.dto.HallRequest;
import com.seatplan.dto.HallResponse;
import com.seatplan.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@Tag(name = "Hall Management", description = "APIs for managing examination halls and seats")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @PostMapping
    @Operation(summary = "Register a new examination hall and auto-generate seats")
    public ResponseEntity<HallResponse> createHall(@Valid @RequestBody HallRequest request) {
        HallResponse response = hallService.createHall(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get list of all examination halls")
    public ResponseEntity<List<HallResponse>> getAllHalls() {
        return ResponseEntity.ok(hallService.getAllHalls());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hall details by ID")
    public ResponseEntity<HallResponse> getHallById(@PathVariable Long id) {
        return ResponseEntity.ok(hallService.getHallById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing examination hall")
    public ResponseEntity<HallResponse> updateHall(@PathVariable Long id, @Valid @RequestBody HallRequest request) {
        return ResponseEntity.ok(hallService.updateHall(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an examination hall")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }
}
