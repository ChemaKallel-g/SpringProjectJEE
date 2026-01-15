package com.institut.ProjetSpringAC.repository;

import com.institut.ProjetSpringAC.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
