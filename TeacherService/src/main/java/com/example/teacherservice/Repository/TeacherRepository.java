package com.example.teacherservice.Repository;

import com.example.teacherservice.Model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher getTeacherById(int id);
    Teacher findByEmail(String email);
}
