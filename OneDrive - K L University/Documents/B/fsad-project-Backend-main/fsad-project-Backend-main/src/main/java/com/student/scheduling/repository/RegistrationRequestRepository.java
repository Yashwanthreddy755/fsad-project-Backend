package com.student.scheduling.repository;

import com.student.scheduling.entity.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, String status);
}
