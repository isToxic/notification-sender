package org.zgr.notification.sender.service.http;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.zgr.notification.sender.AppTests;
import org.zgr.notification.sender.StatusLogic;
import org.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import org.zgr.notification.sender.enums.MessageType;
import org.zgr.notification.sender.model.recieve.NotificationStatus;
import org.zgr.notification.sender.service.db.DBService;

import java.sql.Timestamp;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.zgr.notification.sender.TestUtil.getLongIn;
import static org.zgr.notification.sender.TestUtil.getRandomString;

@Import(NotificationStatusServiceTest.TestConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NotificationStatusServiceTest extends AppTests {

    private static StatusLogic logic;
    private final NotificationStatusService notificationStatusService;

    @Test
    public void processStatusSMSTest() {
        NotificationStatus notificationStatus = NotificationStatus.builder()
                .id(getLongIn(0L, 9999L).toString())
                .mtNum(getRandomString())
                .status(2)
                .type(MessageType.SMS.name())
                .doneDate(Timestamp.from(Instant.now()))
                .submitDate(Timestamp.from(Instant.now()))
                .destAddr(getLongIn(70000000000L, 79999999999L).toString())
                .sourceAddr(getRandomString())
                .text(getRandomString())
                .partCount(getLongIn(0L, 10L).toString())
                .errorCode("0")
                .mccMnc(getLongIn(10000L, 99999L).toString())
                .trafficType(getLongIn(0,3).intValue())
                .segmentPrice("0.88")
                .build();
        notificationStatusService.processStatus(notificationStatus);
        Assertions.assertEquals(StatusLogic.SMS, logic);
    }

    @Test
    public void processStatusSMSErrorTest(){
        NotificationStatus notificationStatus = NotificationStatus.builder()
                .id(getLongIn(0L, 9999L).toString())
                .mtNum(getRandomString())
                .status(5)
                .type(MessageType.SMS.name())
                .doneDate(Timestamp.from(Instant.now()))
                .submitDate(Timestamp.from(Instant.now()))
                .destAddr(getLongIn(70000000000L, 79999999999L).toString())
                .sourceAddr(getRandomString())
                .text(getRandomString())
                .partCount(getLongIn(0L, 10L).toString())
                .errorCode("4")
                .mccMnc(getLongIn(10000L, 99999L).toString())
                .trafficType(getLongIn(0,3).intValue())
                .segmentPrice("0.88")
                .build();
        notificationStatusService.processStatus(notificationStatus);
        Assertions.assertEquals(StatusLogic.SMS_ERROR, logic);
    }

    @Test
    public void processStatusPushTest(){
        NotificationStatus notificationStatus = NotificationStatus.builder()
                .id(getLongIn(0L, 9999L).toString())
                .mtNum(getRandomString())
                .status(2)
                .type(MessageType.PUSH.name())
                .doneDate(Timestamp.from(Instant.now()))
                .submitDate(Timestamp.from(Instant.now()))
                .destAddr(getLongIn(70000000000L, 79999999999L).toString())
                .sourceAddr(getRandomString())
                .text(getRandomString())
                .errorCode("0")
                .mccMnc(getLongIn(10000L, 99999L).toString())
                .trafficType(getLongIn(0,3).intValue())
                .segmentPrice("0.88")
                .build();
        notificationStatusService.processStatus(notificationStatus);
        Assertions.assertEquals(StatusLogic.PUSH, logic);
    }

    @Test
    public void processStatusPushErrorTest(){
        NotificationStatus notificationStatus = NotificationStatus.builder()
                .id(getLongIn(0L, 9999L).toString())
                .mtNum(getRandomString())
                .status(5)
                .type(MessageType.PUSH.name())
                .doneDate(Timestamp.from(Instant.now()))
                .submitDate(Timestamp.from(Instant.now()))
                .destAddr(getLongIn(70000000000L, 79999999999L).toString())
                .sourceAddr(getRandomString())
                .text(getRandomString())
                .errorCode("013")
                .mccMnc(getLongIn(10000L, 99999L).toString())
                .trafficType(getLongIn(0,3).intValue())
                .segmentPrice("0.88")
                .build();
        notificationStatusService.processStatus(notificationStatus);
        Assertions.assertEquals(StatusLogic.PUSH_ERROR, logic);
    }


    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public SendNotificationService notificationStatusTest() {
            SendNotificationService std = mock(SendNotificationService.class);
            doAnswer(invocation -> (Runnable) () -> {
            })
                    .when(std)
                    .sendNotification(any(IntCommQuery.class));
            return std;
        }

        @Bean
        @Primary
        public DBService dbServiceTest() {
            DBService std = mock(DBService.class);
            doAnswer(invocation -> logic = StatusLogic.SMS)
                    .when(std)
                    .saveNotificationStatusSMS(any());
            doAnswer(invocation -> logic = StatusLogic.SMS_ERROR)
                    .when(std)
                    .saveNotificationStatusSMSError(any());
            doAnswer(invocation -> logic = StatusLogic.PUSH)
                    .when(std)
                    .saveNotificationStatusPUSH(any());
            doAnswer(invocation -> logic = StatusLogic.PUSH_ERROR)
                    .when(std)
                    .saveNotificationStatusPUSHError(any());
            return std;
        }
    }
}
