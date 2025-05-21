package com.example.studentservice.Controller;

import com.example.studentservice.DTO.TeacherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TeacherService", url = "http://localhost:8081")
public interface TeacherClient {

    @GetMapping("/teacher/{id}")
    TeacherDTO getTeacherById(@PathVariable("id") int id);
}