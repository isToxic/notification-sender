package org.zgr.notification.sender.service.http;

import org.springframework.stereotype.Service;
import org.zgr.notification.sender.model.recieve.NotificationStatus;

@Service
public interface NotificationStatusService {
    void processStatus(NotificationStatus notificationStatus);
}
