package com.student.scheduling.controller;

import com.student.scheduling.entity.Course;
import com.student.scheduling.entity.DropRequest;
import com.student.scheduling.entity.Notification;
import com.student.scheduling.entity.RegistrationRequest;
import com.student.scheduling.entity.User;
import com.student.scheduling.entity.WaitlistRequest;
import com.student.scheduling.repository.DropRequestRepository;
import com.student.scheduling.repository.NotificationRepository;
import com.student.scheduling.repository.RegistrationRequestRepository;
import com.student.scheduling.repository.UserRepository;
import com.student.scheduling.repository.WaitlistRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired private DropRequestRepository dropRequestRepository;
    @Autowired private RegistrationRequestRepository registrationRequestRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WaitlistRequestRepository waitlistRequestRepository;
    @Autowired private NotificationRepository notificationRepository;

    @GetMapping("/drop-requests")
    public List<DropRequest> getAllDropRequests() {
        return dropRequestRepository.findAll();
    }

    @PostMapping("/drop-requests/{requestId}/approve")
    public ResponseEntity<?> approveDrop(@PathVariable Long requestId) {
        Optional<DropRequest> reqOpt = dropRequestRepository.findById(requestId);
        if (reqOpt.isPresent()) {
            DropRequest request = reqOpt.get();
            request.setStatus("approved");
            
            User student = request.getStudent();
            student.getRegisteredCourses().remove(request.getCourse());
            userRepository.save(student);
            
            dropRequestRepository.save(request);
            notificationRepository.save(new Notification(student, "Drop request approved for " + request.getCourse().getCourseName()));

            // Check waitlist for this course
            List<WaitlistRequest> waitlist = waitlistRequestRepository.findByCourseIdOrderByRequestedAtAsc(request.getCourse().getId());
            if (!waitlist.isEmpty()) {
                WaitlistRequest first = waitlist.get(0);
                User waitlistedStudent = first.getStudent();
                waitlistedStudent.getRegisteredCourses().add(request.getCourse());
                userRepository.save(waitlistedStudent);
                waitlistRequestRepository.delete(first);
                
                notificationRepository.save(new Notification(waitlistedStudent, "Great news! You were automatically enrolled in " + request.getCourse().getCourseName() + " from the waitlist."));
            }
            
            return ResponseEntity.ok(Map.of("message", "Approved"));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/drop-requests/{requestId}/reject")
    public ResponseEntity<?> rejectDrop(@PathVariable Long requestId) {
        Optional<DropRequest> reqOpt = dropRequestRepository.findById(requestId);
        if (reqOpt.isPresent()) {
            DropRequest request = reqOpt.get();
            request.setStatus("rejected");
            dropRequestRepository.save(request);
            notificationRepository.save(new Notification(request.getStudent(), "Drop request rejected for " + request.getCourse().getCourseName()));
            return ResponseEntity.ok(Map.of("message", "Rejected"));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/students")
    public ResponseEntity<?> addStudent(@RequestBody User student) {
        if(userRepository.findByUsername(student.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
        }
        student.setRole("student");
        userRepository.save(student);
        return ResponseEntity.ok(Map.of("message", "Student added successfully", "student", student));
    }

    @GetMapping("/registration-requests")
    public List<RegistrationRequest> getAllRegistrationRequests() {
        return registrationRequestRepository.findAll();
    }

    @PostMapping("/registration-requests/{requestId}/approve")
    public ResponseEntity<?> approveRegistration(@PathVariable Long requestId) {
        Optional<RegistrationRequest> reqOpt = registrationRequestRepository.findById(requestId);
        if (reqOpt.isPresent()) {
            RegistrationRequest request = reqOpt.get();
            request.setStatus("approved");
            
            User student = request.getStudent();
            Course course = request.getCourse();
            
            student.getRegisteredCourses().add(course);
            userRepository.save(student);
            
            registrationRequestRepository.save(request);
            notificationRepository.save(new Notification(student, "Registration request approved for " + course.getCourseName()));
            
            return ResponseEntity.ok(Map.of("message", "Approved"));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/registration-requests/{requestId}/reject")
    public ResponseEntity<?> rejectRegistration(@PathVariable Long requestId) {
        Optional<RegistrationRequest> reqOpt = registrationRequestRepository.findById(requestId);
        if (reqOpt.isPresent()) {
            RegistrationRequest request = reqOpt.get();
            request.setStatus("rejected");
            registrationRequestRepository.save(request);
            
            User student = request.getStudent();
            Course course = request.getCourse();
            
            notificationRepository.save(new Notification(student, "Registration request rejected for " + course.getCourseName()));
            
            return ResponseEntity.ok(Map.of("message", "Rejected"));
        }
        return ResponseEntity.notFound().build();
    }
}
