package com.student.scheduling.repository;

import com.student.scheduling.entity.WaitlistRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitlistRequestRepository extends JpaRepository<WaitlistRequest, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    List<WaitlistRequest> findByCourseIdOrderByRequestedAtAsc(Long courseId);
}
