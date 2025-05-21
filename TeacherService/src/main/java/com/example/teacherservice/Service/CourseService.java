package com.example.teacherservice.Service;


import com.example.teacherservice.Model.Course;
import com.example.teacherservice.Repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    @Cacheable(value = "courses", key = "#id")
    public Course getCourse(int id){
        return courseRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "coursesAll")
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @CacheEvict(value = {"coursesAll"}, allEntries = true)
    @Transactional
    public Course createCourse(Course course){
        return courseRepository.save(course);
    }

    @CacheEvict(value = {"courses", "coursesAll"}, key = "#id", allEntries = true)
    @Transactional
    public void deleteCourse(int id){
        courseRepository.deleteById(id);
    }

    @CacheEvict(value = {"courses", "coursesAll"}, key = "#course.id", allEntries = true)
    @Transactional
    public Course updateCourse(Course course){
        Course existing = courseRepository.findById(course.getId()).orElse(null);
        if(existing != null){
            existing.setCourseName(course.getCourseName());
            existing.setDescription(course.getDescription());
            existing.setTeacher(course.getTeacher());
            return courseRepository.save(existing);
        }
        return null;
    }

    public List<Course> getCoursesByTeacherId(int teacherId){
        return courseRepository.findByTeacherId(teacherId);
    }
}
