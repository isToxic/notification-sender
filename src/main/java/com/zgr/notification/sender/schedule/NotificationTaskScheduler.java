package com.zgr.notification.sender.schedule;

import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import com.zgr.notification.sender.service.db.DBService;
import com.zgr.notification.sender.service.http.SendNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NotificationTaskScheduler {

    private final DBService dbService;
    private final SendNotificationService sendNotificationService;

    @Value("${notification.receive.core-pool-size}")
    int corePoolSize;
    @Value("${notification.receive.max-pool-size}")
    int maxPoolSize;
    @Value("${notification.receive.keep-alive-time}")
    int keepAliveTime;

    @Scheduled(fixedDelayString = "${notification.receive.delay-millis}")
    public void process() {
        List<IntCommQuery> tasksForProcess = dbService.receiveNotificationTasks();
        if (tasksForProcess.isEmpty()) {
            return;
        }

        log.info("messages received for sending: {}", tasksForProcess.size());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(tasksForProcess.size())
        );

        try {
            List<Future<?>> futures = new ArrayList<>(tasksForProcess.size());

            for (IntCommQuery notificationTask : tasksForProcess) {
                Runnable task = sendNotificationService.sendNotification(notificationTask);
                futures.add(executor.submit(task));
            }

            for (Future<?> eachFuture : futures) {
                try {
                    eachFuture.get();
                } catch (Exception ex) {
                    log.error("message sending failed, interrupted", ex);
                }
            }
        } finally {
            executor.shutdown();
        }
    }
}
