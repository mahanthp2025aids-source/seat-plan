package com.seatplan.repository;

import com.seatplan.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    boolean existsByHallNumber(String hallNumber);
    Optional<Hall> findByHallNumber(String hallNumber);
}
