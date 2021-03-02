package org.zgr.notification.sender.service.db;

import org.springframework.stereotype.Service;
import org.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import org.zgr.notification.sender.model.recieve.NotificationResponse;

import java.util.List;

@Service
public interface DBService {
    List<IntCommQuery> receiveNotificationTasks();
    void setStatusSent(Long contactId);
    void setErrorStatus(Long contactId, NotificationResponse response);
}
