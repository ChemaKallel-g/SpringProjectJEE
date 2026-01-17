package com.institut.ProjetSpringAC.dto;

import java.time.LocalDate;

public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String registrationNumber;
    private String email;
    private LocalDate registrationDate;
    private Long groupId;
    private String groupName;

    public StudentDto() {
    }

    public StudentDto(Long id, String firstName, String lastName, String registrationNumber, String email,
            LocalDate registrationDate, Long groupId, String groupName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.registrationDate = registrationDate;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    // Constructor for conversion from Entity (simplified)
    public StudentDto(Long id, String firstName, String lastName, String registrationNumber, String email,
            LocalDate registrationDate, String groupName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.registrationDate = registrationDate;
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
