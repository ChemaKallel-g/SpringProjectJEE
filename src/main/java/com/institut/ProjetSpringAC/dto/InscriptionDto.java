package com.institut.ProjetSpringAC.dto;

import java.time.LocalDate;

public class InscriptionDto {
    private Long id;
    private LocalDate date;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private String status;

    public InscriptionDto() {
    }

    public InscriptionDto(Long id, LocalDate date, Long studentId, String studentName, Long courseId,
            String courseName, String status) {
        this.id = id;
        this.date = date;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
