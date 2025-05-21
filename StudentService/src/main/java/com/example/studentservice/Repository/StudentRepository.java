package com.example.studentservice.Repository;

import com.example.studentservice.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student getStudentsById(int id);
    List<Student> findByTeacherId(int teacherId);
    Student findByEmail(String email);
}
