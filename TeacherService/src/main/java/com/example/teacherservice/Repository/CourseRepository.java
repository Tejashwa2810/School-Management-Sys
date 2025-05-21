package com.example.teacherservice.Repository;

import com.example.teacherservice.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByTeacherId(int teacherId);
}
