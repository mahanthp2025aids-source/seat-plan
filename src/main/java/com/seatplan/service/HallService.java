package com.seatplan.service;

import com.seatplan.dto.HallRequest;
import com.seatplan.dto.HallResponse;
import com.seatplan.entity.Hall;
import com.seatplan.entity.Seat;
import com.seatplan.exception.CapacityExceededException;
import com.seatplan.exception.DuplicateResourceException;
import com.seatplan.exception.ResourceNotFoundException;
import com.seatplan.repository.HallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HallService {

    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Transactional
    public HallResponse createHall(HallRequest request) {
        if (hallRepository.existsByHallNumber(request.getHallNumber())) {
            throw new DuplicateResourceException("Hall with hallNumber '" + request.getHallNumber() + "' already exists.");
        }

        int maxPossibleCapacity = request.getRows() * request.getColumns();
        if (request.getCapacity() > maxPossibleCapacity) {
            throw new CapacityExceededException("Declared capacity (" + request.getCapacity() +
                    ") exceeds total matrix slots (rows * columns = " + maxPossibleCapacity + ").");
        }

        Hall hall = Hall.builder()
                .hallNumber(request.getHallNumber())
                .name(request.getName())
                .rows(request.getRows())
                .columns(request.getColumns())
                .capacity(request.getCapacity())
                .seats(new ArrayList<>())
                .build();

        // Generate seats automatically according to rows and columns up to capacity
        int seatCounter = 1;
        for (int r = 1; r <= request.getRows(); r++) {
            for (int c = 1; c <= request.getColumns(); c++) {
                if (seatCounter > request.getCapacity()) {
                    break;
                }
                Seat seat = Seat.builder()
                        .seatNumber("S" + seatCounter)
                        .rowNumber(r)
                        .columnNumber(c)
                        .hall(hall)
                        .build();
                hall.getSeats().add(seat);
                seatCounter++;
            }
        }

        Hall savedHall = hallRepository.save(hall);
        return mapToResponse(savedHall);
    }

    public List<HallResponse> getAllHalls() {
        return hallRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public HallResponse getHallById(Long id) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with ID: " + id));
        return mapToResponse(hall);
    }

    @Transactional
    public HallResponse updateHall(Long id, HallRequest request) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with ID: " + id));

        if (!hall.getHallNumber().equalsIgnoreCase(request.getHallNumber())
                && hallRepository.existsByHallNumber(request.getHallNumber())) {
            throw new DuplicateResourceException("Hall with hallNumber '" + request.getHallNumber() + "' already exists.");
        }

        int maxPossibleCapacity = request.getRows() * request.getColumns();
        if (request.getCapacity() > maxPossibleCapacity) {
            throw new CapacityExceededException("Declared capacity (" + request.getCapacity() +
                    ") exceeds total matrix slots (rows * columns = " + maxPossibleCapacity + ").");
        }

        hall.setHallNumber(request.getHallNumber());
        hall.setName(request.getName());
        hall.setRows(request.getRows());
        hall.setColumns(request.getColumns());
        hall.setCapacity(request.getCapacity());

        // Regenerate seats
        hall.getSeats().clear();
        int seatCounter = 1;
        for (int r = 1; r <= request.getRows(); r++) {
            for (int c = 1; c <= request.getColumns(); c++) {
                if (seatCounter > request.getCapacity()) {
                    break;
                }
                Seat seat = Seat.builder()
                        .seatNumber("S" + seatCounter)
                        .rowNumber(r)
                        .columnNumber(c)
                        .hall(hall)
                        .build();
                hall.getSeats().add(seat);
                seatCounter++;
            }
        }

        Hall updatedHall = hallRepository.save(hall);
        return mapToResponse(updatedHall);
    }

    @Transactional
    public void deleteHall(Long id) {
        if (!hallRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hall not found with ID: " + id);
        }
        hallRepository.deleteById(id);
    }

    private HallResponse mapToResponse(Hall hall) {
        return HallResponse.builder()
                .id(hall.getId())
                .hallNumber(hall.getHallNumber())
                .name(hall.getName())
                .rows(hall.getRows())
                .columns(hall.getColumns())
                .capacity(hall.getCapacity())
                .totalGeneratedSeats(hall.getSeats() != null ? hall.getSeats().size() : 0)
                .build();
    }
}
