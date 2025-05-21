package com.example.studentservice.Consumer;

import com.example.studentservice.DTO.CreateTaskEventDTO;
import com.example.studentservice.Utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Properties;

@Service
public class CreateTaskEventConsumer {
    private ObjectMapper mapper;

    public CreateTaskEventConsumer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @KafkaListener(topics = "createTask", groupId = "taskService")
    public void handleCreateTaskEvent(String message) throws JsonProcessingException {
        CreateTaskEventDTO createTaskEventDTO = mapper.readValue(message, CreateTaskEventDTO.class);

        List<String> to = createTaskEventDTO.getTo();
        String from = createTaskEventDTO.getFrom();
        String subject = createTaskEventDTO.getTaskSubject();
        String body = createTaskEventDTO.getTaskBody();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        String emailPassword = createTaskEventDTO.getPassword();

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, emailPassword);
            }
        };

        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, to, subject, body);
    }

}
