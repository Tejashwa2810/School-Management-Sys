package com.example.studentservice.Controller;

import com.example.studentservice.Models.Course;
import com.example.studentservice.Models.Student;
import com.example.studentservice.Service.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable("id") int id) {
        Student student = studentService.getStudent(id);
        return student;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/getall")
    public List<Student> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return students;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/create")
    public Student createStudent(@RequestBody Student student) {
        Student stu = studentService.createStudent(student);
        return stu;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("delete/{id}")
    public void deleteStudent(@PathVariable("id") int id) {
        studentService.deleteStudent(id);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("update/{id}")
    public Student updateStudent(@PathVariable("id") int id, @RequestBody Student student) {
        student.setId(id);
        return studentService.updateStudent(student);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/assign-teacher")
    public Student assignTeacher(@RequestParam int studentId, @RequestParam int teacherId) {
        return studentService.assignTeacher(studentId, teacherId);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/by-teacher")
    public List<Student> getStudentsByTeacher(@RequestParam int teacherId) {
        return studentService.getStudentsByTeacherId(teacherId);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/home")
    public String studentOnlyStuff() {
        return "Welcome student!";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{studentId}/enroll/{courseId}")
    public Student enrollInCourse(@PathVariable int studentId, @PathVariable int courseId){
        return studentService.enrollInCourse(studentId, courseId);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{studentId}/unenroll/{courseId}")
    public Student removeCourseEnrollment(@PathVariable int studentId, @PathVariable int courseId){
        return studentService.removeCourseEnrollment(studentId, courseId);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{studentId}/courses")
    public Set<Course> getEnrolledCourses(@PathVariable int studentId){
        return studentService.getEnrolledCourses(studentId);
    }
}
