package com.sunny.email_sending.controller;

import com.sunny.email_sending.model.Notification;
import com.sunny.email_sending.repository.NotificationRepository;
import com.sunny.email_sending.service.NotificationService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping
    public Notification createNotification(@RequestBody Notification notification){
        notification.setStatus("PENDING");
        notificationRepository.save(notification);
        notificationService.sendNotification(notification);
        return notification;
    }
}
