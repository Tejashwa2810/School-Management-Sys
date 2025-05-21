package com.example.studentservice.Controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TeacherService", url = "http://localhost:8081")
public interface CourseClient {
    @GetMapping("/teacher/courses/{courseId}/exists")
    boolean isCourseExist(@PathVariable("courseId") int courseId);
}
