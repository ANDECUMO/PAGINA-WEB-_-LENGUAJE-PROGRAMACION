package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Recuperación de Contraseña");
        message.setText("Hola,\n\n" +
                "Has solicitado restablecer tu contraseña.\n\n" +
                "Para crear una nueva contraseña, haz clic en el siguiente enlace:\n" +
                resetUrl + "\n\n" +
                "Este enlace expirará en 30 minutos.\n\n" +
                "Si no solicitaste este cambio, ignora este correo.\n\n" +
                "Saludos,\n" +
                "El equipo de soporte");
        
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("¡Bienvenido!");
        message.setText("Hola " + username + ",\n\n" +
                "¡Gracias por registrarte en nuestra plataforma!\n\n" +
                "Tu cuenta ha sido creada exitosamente.\n\n" +
                "Saludos,\n" +
                "El equipo de soporte");
        
        mailSender.send(message);
    }
}
