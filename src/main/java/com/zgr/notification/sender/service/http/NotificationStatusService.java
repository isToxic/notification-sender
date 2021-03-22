package com.zgr.notification.sender.service.http;

import com.zgr.notification.sender.model.recieve.NotificationStatus;
import org.springframework.stereotype.Service;

@Service
public interface NotificationStatusService {
    void processStatus(NotificationStatus notificationStatus);
}
