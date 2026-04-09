package com.student.scheduling.repository;

import com.student.scheduling.entity.DropRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DropRequestRepository extends JpaRepository<DropRequest, Long> {
    List<DropRequest> findByStudentId(Long studentId);
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, String status);
}
