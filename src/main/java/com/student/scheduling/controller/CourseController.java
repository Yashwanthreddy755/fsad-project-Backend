package com.student.scheduling.controller;

import com.student.scheduling.entity.Course;
import com.student.scheduling.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {
    
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseRepository.findById(id).map(course -> {
            course.setCourseCode(courseDetails.getCourseCode());
            course.setCourseName(courseDetails.getCourseName());
            course.setInstructor(courseDetails.getInstructor());
            course.setCredits(courseDetails.getCredits());
            course.setDay(courseDetails.getDay());
            course.setStartTime(courseDetails.getStartTime());
            course.setEndTime(courseDetails.getEndTime());
            if (courseDetails.getCapacity() > 0) {
                course.setCapacity(courseDetails.getCapacity());
            }
            return ResponseEntity.ok(courseRepository.save(course));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        return courseRepository.findById(id).map(course -> {
            courseRepository.delete(course);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
