package com.institut.ProjetSpringAC.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseSessionDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("courseName")
    private String courseName;

    @JsonProperty("dayOfWeek")
    private String dayOfWeek;

    @JsonProperty("startTime")
    private String startTime;

    @JsonProperty("endTime")
    private String endTime;

    @JsonProperty("room")
    private String room;

    public CourseSessionDto() {
    }

    public CourseSessionDto(Long id, String courseName, String dayOfWeek, String startTime, String endTime,
            String room) {
        this.id = id;
        this.courseName = courseName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
