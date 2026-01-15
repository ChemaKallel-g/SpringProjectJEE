package com.institut.ProjetSpringAC.service;

import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.repository.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;

    @Autowired
    public StudentGroupService(StudentGroupRepository studentGroupRepository) {
        this.studentGroupRepository = studentGroupRepository;
    }

    public List<StudentGroup> getAllGroups() {
        return studentGroupRepository.findAll();
    }

    public Optional<StudentGroup> getGroupById(Long id) {
        return studentGroupRepository.findById(id);
    }

    public StudentGroup saveGroup(StudentGroup group) {
        return studentGroupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        studentGroupRepository.deleteById(id);
    }
}
