package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.dto.StudentDto;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class ApiEtudiantController {

    private final StudentService studentService;

    public ApiEtudiantController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(student -> ResponseEntity.ok(convertToDto(student)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public StudentDto createStudent(@RequestBody Student student) {
        return convertToDto(studentService.saveStudent(student));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentService.getStudentById(id)
                .map(existingStudent -> {
                    existingStudent.setFirstName(studentDetails.getFirstName());
                    existingStudent.setLastName(studentDetails.getLastName());
                    existingStudent.setEmail(studentDetails.getEmail());
                    existingStudent.setRegistrationNumber(studentDetails.getRegistrationNumber());
                    existingStudent.setRegistrationDate(studentDetails.getRegistrationDate());
                    return ResponseEntity.ok(convertToDto(studentService.saveStudent(existingStudent)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    private StudentDto convertToDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getRegistrationNumber(),
                student.getEmail(),
                student.getRegistrationDate(),
                student.getGroup() != null ? student.getGroup().getName() : null);
    }
}
