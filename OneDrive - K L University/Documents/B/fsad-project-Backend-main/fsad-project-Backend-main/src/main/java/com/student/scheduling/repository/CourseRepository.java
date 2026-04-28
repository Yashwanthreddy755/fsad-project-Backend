package com.student.scheduling.repository;

import com.student.scheduling.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    java.util.List<Course> findByInstructor(String instructor);
}
