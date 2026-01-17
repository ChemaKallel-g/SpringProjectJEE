package com.institut.ProjetSpringAC.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GradeDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("grade")
    private Double grade;

    @JsonProperty("studentId")
    private Long studentId;

    @JsonProperty("studentName")
    private String studentName;

    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("courseName")
    private String courseName;

    @JsonProperty("date")
    private String date;

    @JsonProperty("comment")
    private String comment;

    public GradeDto() {
    }

    public GradeDto(Long id, Double grade, Long studentId, String studentName, Long courseId, String courseName,
            String date, String comment) {
        this.id = id;
        this.grade = grade;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.date = date;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
