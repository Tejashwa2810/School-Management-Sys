package com.example.teacherservice.Controller;

import com.example.teacherservice.DTO.CreateTaskDTO;
import com.example.teacherservice.DTO.StudentDTO;
import com.example.teacherservice.Model.Course;
import com.example.teacherservice.Model.Teacher;
import com.example.teacherservice.Service.CourseService;
import com.example.teacherservice.Service.TeacherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private TeacherService teacherService;
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    public TeacherController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable("id") int id) {
        return teacherService.getTeacher(id);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/getall")
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherService.createTeacher(teacher);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{id}")
    public void deleteTeacher(@PathVariable("id") int id) {
        teacherService.deleteTeacher(id);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{id}")
    public Teacher updateTeacher(@PathVariable("id") int id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        return teacherService.updateTeacher(teacher);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{id}/students")
    public List<StudentDTO> getStudentsForTeacher(@PathVariable("id") int teacherId) {
        return teacherService.getStudentsForTeacher(teacherId);
    }


    // Publisher
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createtask")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        String message = "Email Sent";
        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);

        try{
            kafkaTemplate.send(
                    "createTask",
                    objectMapper.writeValueAsString(createTaskDTO));
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        return response;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/home")
    public String studentOnlyStuff() {
        return "Welcome teacher!";
    }



    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{teacherId}/courses")
    public List<Course> getCoursesByTeacher(@PathVariable int teacherId){
        return courseService.getCoursesByTeacherId(teacherId);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{teacherId}/courses")
    public Course createCourse(@PathVariable int teacherId, @RequestBody Course course){
        Teacher teacher = teacherService.getTeacher(teacherId);
        course.setTeacher(teacher);
        return courseService.createCourse(course);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/courses/{id}")
    public Course updateCourse(@PathVariable int id, @RequestBody Course course){
        course.setId(id);
        return courseService.updateCourse(course);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/courses/{id}")
    public void deleteCourse(@PathVariable int id){
        courseService.deleteCourse(id);
    }


}
