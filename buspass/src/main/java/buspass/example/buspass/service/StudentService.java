package buspass.example.buspass.service;

import buspass.example.buspass.entity.Student;
import buspass.example.buspass.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create student
    public Student createStudent(Student student) {

        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Student with this email already exists");
        }

        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with id: " + id));
    }

    // Update student
    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = getStudentById(id);

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setPhone(updatedStudent.getPhone());
        existingStudent.setCollegeName(updatedStudent.getCollegeName());

        return studentRepository.save(existingStudent);
    }

    // Delete student
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }
}