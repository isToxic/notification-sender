package org.zgr.notification.sender.schedulle;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.zgr.notification.sender.service.TaskReceiveService;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NotificationTaskScheduler {

    private final TaskReceiveService taskReceiveService;

    @Scheduled(fixedDelayString = "${notification.receive.delay-millis}")
    public void process(){
        taskReceiveService.receiveNotificationTasks();
    }
}
