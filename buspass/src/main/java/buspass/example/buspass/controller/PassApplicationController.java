package buspass.example.buspass.controller;

import buspass.example.buspass.entity.PassApplication;
import buspass.example.buspass.service.PassApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class PassApplicationController {

    private final PassApplicationService passApplicationService;

    public PassApplicationController(
            PassApplicationService passApplicationService) {

        this.passApplicationService = passApplicationService;
    }

    // Student applies for bus pass
    @PostMapping
    public ResponseEntity<PassApplication> applyForPass(
            @RequestParam Long studentId,
            @RequestParam Long routeId,
            @RequestParam String boardingPoint,
            @RequestParam String photoReference) {

        PassApplication application =
                passApplicationService.applyForPass(
                        studentId,
                        routeId,
                        boardingPoint,
                        photoReference
                );

        return new ResponseEntity<>(
                application,
                HttpStatus.CREATED
        );
    }

    // Get all applications
    @GetMapping
    public ResponseEntity<List<PassApplication>> getAllApplications() {

        return ResponseEntity.ok(
                passApplicationService.getAllApplications()
        );
    }

    // Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<PassApplication> getApplicationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                passApplicationService.getApplicationById(id)
        );
    }

    // Get applications by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<PassApplication>>
    getApplicationsByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                passApplicationService
                        .getApplicationsByStudent(studentId)
        );
    }

    // Admin approves application
    @PutMapping("/{id}/approve")
    public ResponseEntity<PassApplication> approveApplication(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                passApplicationService
                        .approveApplication(id)
        );
    }

    // Admin rejects application
    @PutMapping("/{id}/reject")
    public ResponseEntity<PassApplication> rejectApplication(
            @PathVariable Long id,
            @RequestParam String reason) {

        return ResponseEntity.ok(
                passApplicationService
                        .rejectApplication(id, reason)
        );
    }

    // Passes expiring within 30 days
    @GetMapping("/expiring")
    public ResponseEntity<List<PassApplication>>
    getExpiringPasses() {

        return ResponseEntity.ok(
                passApplicationService
                        .getPassesExpiringNext30Days()
        );
    }
}