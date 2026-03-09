package com.example.demo.service;

import com.example.demo.model.CustomerOrder;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

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
        message.setText("""
                Hola,

                Has solicitado restablecer tu contraseña.

                Para crear una nueva contraseña, haz clic en el siguiente enlace:
                %s

                Este enlace expirará en 30 minutos.

                Si no solicitaste este cambio, ignora este correo.

                Saludos,
                El equipo de soporte
                """.formatted(resetUrl));
        
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

    // RF09 - Confirmación de pedido al usuario
    public void sendOrderConfirmationEmail(String toEmail, String userName, CustomerOrder order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        StringBuilder itemsList = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsList.append("  - ")
                    .append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = $").append(item.getSubtotal())
                    .append("\n");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Confirmación de Pedido #" + order.getId());
        message.setText("Hola " + userName + ",\n\n" +
                "¡Tu pedido ha sido registrado exitosamente!\n\n" +
                "Detalles del pedido #" + order.getId() + ":\n" +
                "Fecha: " + order.getCreatedAt().format(formatter) + "\n" +
                "Estado: " + order.getStatusLabel() + "\n\n" +
                "Productos:\n" + itemsList + "\n" +
                "Total: $" + order.getTotalAmount() + "\n\n" +
                "Dirección de envío: " + (order.getShippingAddress() != null ? order.getShippingAddress() : "N/A") + "\n\n" +
                "Puedes hacer seguimiento de tu pedido en:\n" +
                frontendUrl + "/orders/" + order.getId() + "\n\n" +
                "¡Gracias por tu compra!\n" +
                "El equipo de soporte");

        mailSender.send(message);
    }

    // Notificación al administrador de nuevo pedido
    public void sendNewOrderNotificationToAdmin(String toEmail, String adminName, CustomerOrder order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        StringBuilder itemsList = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsList.append("  - ")
                    .append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = $").append(item.getSubtotal())
                    .append("\n");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Nuevo Pedido #" + order.getId() + " - Acción Requerida");
        message.setText("Hola " + adminName + ",\n\n" +
                "Se ha recibido un nuevo pedido.\n\n" +
                "Pedido #" + order.getId() + "\n" +
                "Cliente: " + order.getUser().getName() + " (" + order.getUser().getEmail() + ")\n" +
                "Fecha: " + order.getCreatedAt().format(formatter) + "\n\n" +
                "Productos:\n" + itemsList + "\n" +
                "Total: $" + order.getTotalAmount() + "\n\n" +
                "Dirección de envío: " + (order.getShippingAddress() != null ? order.getShippingAddress() : "N/A") + "\n\n" +
                "Por favor, actualiza el estado del pedido en el panel de administración:\n" +
                frontendUrl + "/admin/orders\n\n" +
                "Saludos,\n" +
                "Sistema de Pedidos");

        mailSender.send(message);
    }

    // Notificación al usuario de cambio de estado
    public void sendOrderStatusUpdateEmail(String toEmail, String userName, CustomerOrder order, OrderStatus previousStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Actualización de Pedido #" + order.getId() + " - " + order.getStatusLabel());
        message.setText("Hola " + userName + ",\n\n" +
                "El estado de tu pedido #" + order.getId() + " ha sido actualizado.\n\n" +
                "Estado anterior: " + getStatusLabel(previousStatus) + "\n" +
                "Nuevo estado: " + order.getStatusLabel() + "\n\n" +
                getStatusMessage(order.getStatus()) + "\n\n" +
                "Puedes hacer seguimiento de tu pedido en:\n" +
                frontendUrl + "/orders/" + order.getId() + "\n\n" +
                "Saludos,\n" +
                "El equipo de soporte");

        mailSender.send(message);
    }

    private String getStatusLabel(OrderStatus status) {
        return switch (status) {
            case PENDIENTE -> "Pendiente";
            case EN_PREPARACION -> "En Preparación";
            case EN_CAMINO -> "En Camino";
            case ENTREGADO -> "Entregado";
            case CANCELADO -> "Cancelado";
        };
    }

    private String getStatusMessage(OrderStatus status) {
        return switch (status) {
            case PENDIENTE -> "Tu pedido está pendiente de confirmación.";
            case EN_PREPARACION -> "¡Tu pedido está siendo preparado!";
            case EN_CAMINO -> "¡Tu pedido va en camino! Pronto llegará a tu dirección.";
            case ENTREGADO -> "¡Tu pedido ha sido entregado! Esperamos que lo disfrutes.";
            case CANCELADO -> "Tu pedido ha sido cancelado. Si tienes preguntas, contáctanos.";
        };
    }
}
