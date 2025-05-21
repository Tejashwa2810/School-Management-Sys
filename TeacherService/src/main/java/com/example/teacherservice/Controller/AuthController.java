package com.example.teacherservice.Controller;

import com.example.teacherservice.AuthDTO.AuthRequest;
import com.example.teacherservice.AuthDTO.AuthResponse;
import com.example.teacherservice.Model.Teacher;
import com.example.teacherservice.Repository.TeacherRepository;
import com.example.teacherservice.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Teacher teacher = teacherRepository.findByEmail(request.getEmail());
        if (teacher != null && passwordEncoder.matches(request.getPassword(), teacher.getPassword())) {
            String token = jwtUtil.generateToken(teacher.getEmail(), teacher.getRole());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.setRole("ROLE_TEACHER");
        teacherRepository.save(teacher);
        return ResponseEntity.ok("User registered");
    }
}
