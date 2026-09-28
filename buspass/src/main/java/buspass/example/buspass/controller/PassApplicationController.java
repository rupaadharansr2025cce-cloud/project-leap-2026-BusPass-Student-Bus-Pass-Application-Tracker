
package buspass.example.buspass.controller;

import buspass.example.buspass.entity.PassApplication;
import buspass.example.buspass.service.PassApplicationService;
import buspass.example.buspass.service.PhotoUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class PassApplicationController {

    private final PassApplicationService passApplicationService;
    private final PhotoUploadService photoUploadService;

    public PassApplicationController(
            PassApplicationService passApplicationService,
            PhotoUploadService photoUploadService) {
        this.passApplicationService = passApplicationService;
        this.photoUploadService = photoUploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PassApplication> applyForPass(
            @RequestParam Long studentId,
            @RequestParam Long routeId,
            @RequestParam String boardingPoint,
            @RequestParam("photo") MultipartFile photo) {

        String photoReference =
                photoUploadService.uploadPhoto(photo);

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

    @GetMapping
    public ResponseEntity<List<PassApplication>> getAllApplications() {
        return ResponseEntity.ok(
                passApplicationService.getAllApplications()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassApplication> getApplicationById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                passApplicationService.getApplicationById(id)
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<PassApplication>>
    getApplicationsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(
                passApplicationService
                        .getApplicationsByStudent(studentId)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PassApplication> approveApplication(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                passApplicationService.approveApplication(id)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PassApplication> rejectApplication(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(
                passApplicationService.rejectApplication(id, reason)
        );
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<PassApplication>>
    getExpiringPasses() {
        return ResponseEntity.ok(
                passApplicationService
                        .getPassesExpiringNext30Days()
        );
    }
}