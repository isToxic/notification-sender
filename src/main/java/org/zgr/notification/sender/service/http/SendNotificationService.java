package org.zgr.notification.sender.service.http;

import org.springframework.stereotype.Service;
import org.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;

@Service
public interface SendNotificationService {
    Runnable sendNotification(IntCommQuery intCommQuery);
}
