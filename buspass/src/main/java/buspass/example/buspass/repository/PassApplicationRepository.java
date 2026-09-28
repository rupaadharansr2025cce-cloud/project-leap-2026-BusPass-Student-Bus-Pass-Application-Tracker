package buspass.example.buspass.repository;

import buspass.example.buspass.entity.ApplicationStatus;
import buspass.example.buspass.entity.PassApplication;
import buspass.example.buspass.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PassApplicationRepository
        extends JpaRepository<PassApplication, Long> {

    boolean existsByStudentAndStatus(
            Student student,
            ApplicationStatus status
    );

    List<PassApplication> findByStudent(Student student);

    List<PassApplication> findByStatus(ApplicationStatus status);

    List<PassApplication> findByStatusAndValidUntilBetween(
            ApplicationStatus status,
            LocalDate startDate,
            LocalDate endDate
    );
}