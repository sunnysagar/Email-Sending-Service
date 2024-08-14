package com.sunny.email_sending.service;

import com.sunny.email_sending.model.Notification;
import com.sunny.email_sending.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {


    private final NotificationRepository notificationRepository;

    private final JavaMailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public void sendNotification(Notification notification){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getEmail());
            message.setSubject(notification.getSubject());
            message.setText(notification.getContent());
            mailSender.send(message);

            notification.setStatus("SENT");
        }catch (Exception e){
            notification.setStatus("FAILED");
        }

        notificationRepository.save(notification);
    }

    @Scheduled(fixedDelay = 60000) //Retry every minute
    public void retryFailedNotification(){
        List<Notification> failedNotification = notificationRepository.findByStatus("FAILED");
        for(Notification notification: failedNotification){
            if(notification.getRetryCount()<3){
                notification.setRetryCount(notification.getRetryCount()  + 1);
                sendNotification(notification);
            }
        }
    }
}
