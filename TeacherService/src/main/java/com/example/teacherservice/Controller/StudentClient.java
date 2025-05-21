package com.example.teacherservice.Controller;

import com.example.teacherservice.DTO.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "StudentService", url = "http://localhost:8082")
public interface StudentClient {

    @GetMapping("/student/by-teacher")
    List<StudentDTO> getStudentsByTeacherId(@RequestParam("teacherId") int teacherId);
}