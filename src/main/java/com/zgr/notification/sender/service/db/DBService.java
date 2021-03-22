package com.zgr.notification.sender.service.db;

import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommResponse;
import com.zgr.notification.sender.model.recieve.NotificationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DBService {
    List<IntCommQuery> receiveNotificationTasks();

    void setStatusSent(Long contactId);

    void setErrorStatus(Long contactId, NotificationResponse response);

    void saveNotificationStatusSMS(IntCommResponse intCommResponse);

    void saveNotificationStatusSMSError(IntCommResponse intCommResponse);

    void saveNotificationStatusPUSH(IntCommResponse intCommResponse);

    void saveNotificationStatusPUSHError(IntCommResponse intCommResponse);
}
