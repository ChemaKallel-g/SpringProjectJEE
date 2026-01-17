package com.institut.ProjetSpringAC.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "academic_sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionType type = SessionType.ACADEMIC_YEAR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Session parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Session> children = new java.util.ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.PLANNED;

    public Session() {
    }

    public Session(String name, LocalDate startDate, LocalDate endDate, SessionType type) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        updateStatus();
    }

    public void updateStatus() {
        LocalDate today = LocalDate.now();
        if (today.isBefore(startDate)) {
            this.status = SessionStatus.PLANNED;
        } else if (today.isAfter(endDate)) {
            this.status = SessionStatus.ARCHIVED;
        } else {
            this.status = SessionStatus.ACTIVE;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public Session getParent() {
        return parent;
    }

    public void setParent(Session parent) {
        this.parent = parent;
    }

    public java.util.List<Session> getChildren() {
        return children;
    }

    public void setChildren(java.util.List<Session> children) {
        this.children = children;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
