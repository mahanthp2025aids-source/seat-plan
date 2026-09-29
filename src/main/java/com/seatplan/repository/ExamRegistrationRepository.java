package com.seatplan.repository;

import com.seatplan.entity.ExamRegistration;
import com.seatplan.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRegistrationRepository extends JpaRepository<ExamRegistration, Long> {
    boolean existsByExamSessionIdAndStudentId(Long examSessionId, Long studentId);
    List<ExamRegistration> findByExamSessionId(Long examSessionId);

    @Query("SELECT r.student FROM ExamRegistration r WHERE r.examSession.id = :examId")
    List<Student> findStudentsByExamSessionId(@Param("examId") Long examId);
}
