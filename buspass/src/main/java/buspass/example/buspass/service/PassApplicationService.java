package buspass.example.buspass.service;

import buspass.example.buspass.entity.ApplicationStatus;
import buspass.example.buspass.entity.BusRoute;
import buspass.example.buspass.entity.PassApplication;
import buspass.example.buspass.entity.Student;
import buspass.example.buspass.repository.BusRouteRepository;
import buspass.example.buspass.repository.PassApplicationRepository;
import buspass.example.buspass.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PassApplicationService {

    private final PassApplicationRepository passApplicationRepository;
    private final StudentRepository studentRepository;
    private final BusRouteRepository busRouteRepository;

    public PassApplicationService(
            PassApplicationRepository passApplicationRepository,
            StudentRepository studentRepository,
            BusRouteRepository busRouteRepository) {

        this.passApplicationRepository = passApplicationRepository;
        this.studentRepository = studentRepository;
        this.busRouteRepository = busRouteRepository;
    }

    // Student applies for a bus pass
    public PassApplication applyForPass(
            Long studentId,
            Long routeId,
            String boardingPoint,
            String photoReference) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with id: " + studentId));

        BusRoute busRoute = busRouteRepository.findById(routeId)
                .orElseThrow(() ->
                        new RuntimeException("Bus route not found with id: " + routeId));

        // Business Rule:
        // A student can have only one active pass at a time.
        List<PassApplication> applications =
                passApplicationRepository.findByStudent(student);

        boolean hasActivePass = applications.stream()
                .anyMatch(application ->
                        application.getStatus() == ApplicationStatus.APPROVED
                                && application.getValidUntil() != null
                                && !application.getValidUntil()
                                .isBefore(LocalDate.now()));

        if (hasActivePass) {
            throw new RuntimeException(
                    "Student already has an active bus pass");
        }

        PassApplication application = new PassApplication();

        application.setStudent(student);
        application.setBusRoute(busRoute);
        application.setBoardingPoint(boardingPoint);
        application.setPhotoReference(photoReference);
        application.setStatus(ApplicationStatus.PENDING);

        return passApplicationRepository.save(application);
    }

    // Get all applications
    public List<PassApplication> getAllApplications() {
        return passApplicationRepository.findAll();
    }

    // Get application by ID
    public PassApplication getApplicationById(Long id) {

        return passApplicationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Application not found with id: " + id));
    }

    // Get applications of a particular student
    public List<PassApplication> getApplicationsByStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with id: " + studentId));

        return passApplicationRepository.findByStudent(student);
    }

    // Admin approves application
    public PassApplication approveApplication(Long id) {

        PassApplication application = getApplicationById(id);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending applications can be approved");
        }

        // Double-check active pass rule before approval
        Student student = application.getStudent();

        List<PassApplication> applications =
                passApplicationRepository.findByStudent(student);

        boolean hasAnotherActivePass = applications.stream()
                .filter(existing -> !existing.getId().equals(id))
                .anyMatch(existing ->
                        existing.getStatus() == ApplicationStatus.APPROVED
                                && existing.getValidUntil() != null
                                && !existing.getValidUntil()
                                .isBefore(LocalDate.now()));

        if (hasAnotherActivePass) {
            throw new RuntimeException(
                    "Student already has another active bus pass");
        }

        application.setStatus(ApplicationStatus.APPROVED);

        LocalDate validFrom = LocalDate.now();
        LocalDate validUntil = validFrom.plusYears(1);

        application.setValidFrom(validFrom);
        application.setValidUntil(validUntil);

        PassApplication savedApplication =
                passApplicationRepository.save(application);

        // Generate pass number after database ID is available
        savedApplication.setPassNumber(
                "BP-" + String.format("%05d", savedApplication.getId()));

        return passApplicationRepository.save(savedApplication);
    }

    // Admin rejects application
    public PassApplication rejectApplication(
            Long id,
            String rejectionReason) {

        if (rejectionReason == null ||
                rejectionReason.trim().isEmpty()) {

            throw new RuntimeException(
                    "Rejection reason is required");
        }

        PassApplication application = getApplicationById(id);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending applications can be rejected");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        application.setRejectionReason(rejectionReason);

        return passApplicationRepository.save(application);
    }

    // Admin lists passes expiring in next 30 days
    public List<PassApplication> getPassesExpiringNext30Days() {

        LocalDate today = LocalDate.now();
        LocalDate next30Days = today.plusDays(30);

        return passApplicationRepository
                .findByStatusAndValidUntilBetween(
                        ApplicationStatus.APPROVED,
                        today,
                        next30Days);
    }
}