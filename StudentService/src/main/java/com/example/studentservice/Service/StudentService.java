package com.example.studentservice.Service;

import com.example.studentservice.Controller.CourseClient;
import com.example.studentservice.Controller.TeacherClient;
import com.example.studentservice.DTO.TeacherDTO;
import com.example.studentservice.Models.Course;
import com.example.studentservice.Models.Student;
import com.example.studentservice.Repository.CourseRepository;
import com.example.studentservice.Repository.StudentRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final TeacherClient teacherClient;
    private final CourseClient courseClient;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, TeacherClient teacherClient, CourseClient courseClient, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.teacherClient = teacherClient;
        this.courseClient = courseClient;
        this.courseRepository = courseRepository;
    }

    @Cacheable(value = "students", key = "#id")
    public Student getStudent(int id) {
        System.out.println("Fetching from DB for ID: " + id);
        return studentRepository.getStudentsById(id);
    }

    @Cacheable(value = "studentsAll")
    public List<Student> getAllStudents() {
        System.out.println("Fetching all students from DB");
        return studentRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "students", key = "#id")
    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {"students", "studentsAll"}, allEntries = true)
    public Student updateStudent(Student student) {
        Student stud = studentRepository.getStudentsById(student.getId());
        stud.setName(student.getName());
        stud.setAge(student.getAge());
        stud.setEmail(student.getEmail());
        return studentRepository.save(stud);
    }

    @CacheEvict(value = {"studentsAll"}, allEntries = true)
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }


    @Transactional
    public Student assignTeacher(int studentId, int teacherId) {
        try {
            TeacherDTO teacher = teacherClient.getTeacherById(teacherId);
        } catch (Exception e) {
            throw new RuntimeException("Teacher not found with id: " + teacherId);
        }

        Student student = studentRepository.getStudentsById(studentId);
        student.setTeacherId(teacherId);
        return studentRepository.save(student);
    }

    public List<Student> getStudentsByTeacherId(int teacherId) {
        return studentRepository.findByTeacherId(teacherId);
    }


    @Transactional
    @CacheEvict(value = {"students", "studentsAll"}, allEntries = true)
    public Student enrollInCourse(int studentId, int courseId) {
        Student student = studentRepository.getStudentsById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        if (!courseClient.isCourseExist(courseId)) {
            throw new RuntimeException("Course not found");
        }

        Course course = new Course();
        course.setId(courseId);

        student.getCourses().add(course);
        return studentRepository.save(student);
    }

    @Transactional
    @CacheEvict(value = {"students", "studentsAll"}, allEntries = true)
    public Student removeCourseEnrollment(int studentId, int courseId) {
        Student student = studentRepository.getStudentsById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        student.getCourses().removeIf(c -> c.getId() == courseId);
        return studentRepository.save(student);
    }

    public Set<Course> getEnrolledCourses(int studentId) {
        Student student = studentRepository.getStudentsById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        return student.getCourses();
    }
}
