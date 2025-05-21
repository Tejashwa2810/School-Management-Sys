package com.example.teacherservice.Service;

import com.example.teacherservice.Controller.StudentClient;
import com.example.teacherservice.DTO.StudentDTO;
import com.example.teacherservice.Model.Teacher;
import com.example.teacherservice.Repository.TeacherRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final StudentClient studentClient;

    public TeacherService(TeacherRepository teacherRepository, StudentClient studentClient) {
        this.teacherRepository = teacherRepository;
        this.studentClient = studentClient;
    }

    @Cacheable(value = "teachers", key = "#id")
    public Teacher getTeacher(int id) {
        return teacherRepository.getTeacherById(id);
    }

    @Cacheable(value = "teachersAll")
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @CacheEvict(value = {"teachersAll"}, allEntries = true)
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Transactional
    @CacheEvict(value = {"teachers", "teachersAll"}, key = "#id", allEntries = true)
    public void deleteTeacher(int id) {
        teacherRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {"teachers", "teachersAll"}, key = "#teacher.id", allEntries = true)
    public Teacher updateTeacher(Teacher teacher) {
        Teacher existing = teacherRepository.getTeacherById(teacher.getId());
        existing.setName(teacher.getName());
        existing.setEmail(teacher.getEmail());
        return teacherRepository.save(existing);
    }


    public List<StudentDTO> getStudentsForTeacher(int teacherId) {
        return studentClient.getStudentsByTeacherId(teacherId);
    }

}
