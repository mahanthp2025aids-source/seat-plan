package com.seatplan.repository;

import com.seatplan.entity.SeatAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatAllocationRepository extends JpaRepository<SeatAllocation, Long> {
    List<SeatAllocation> findByExamSessionId(Long examSessionId);
    Optional<SeatAllocation> findByExamSessionIdAndStudentRollNumber(Long examSessionId, String rollNumber);
    boolean existsByExamSessionId(Long examSessionId);
    void deleteByExamSessionId(Long examSessionId);
}
