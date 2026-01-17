package com.institut.ProjetSpringAC.dto;

public class CourseDto {
    private Long id;
    private String title;
    private String code;
    private String description;
    private Long trainerId;
    private String trainerName;

    public CourseDto() {
    }

    public CourseDto(Long id, String title, String code, String description, Long trainerId, String trainerName) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.description = description;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}
