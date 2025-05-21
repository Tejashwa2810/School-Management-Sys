package com.example.studentservice.Controller;

import com.example.studentservice.AuthDTO.AuthRequest;
import com.example.studentservice.AuthDTO.AuthResponse;
import com.example.studentservice.Models.Student;
import com.example.studentservice.Repository.StudentRepository;
import com.example.studentservice.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Student student = studentRepo.findByEmail(request.getEmail());
        if (student != null && passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            String token = jwtUtil.generateToken(student.getEmail(), student.getRole());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRole("ROLE_STUDENT"); //
        studentRepo.save(student);
        return ResponseEntity.ok("User registered");
    }
}
