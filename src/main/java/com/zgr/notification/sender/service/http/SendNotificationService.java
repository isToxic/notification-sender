package com.zgr.notification.sender.service.http;

import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import org.springframework.stereotype.Service;

@Service
public interface SendNotificationService {
    Runnable sendNotification(IntCommQuery intCommQuery);
}
