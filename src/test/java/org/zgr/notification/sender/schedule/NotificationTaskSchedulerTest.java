package org.zgr.notification.sender.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.zgr.notification.sender.AppTests;
import org.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import org.zgr.notification.sender.enums.IntCommStatus;
import org.zgr.notification.sender.service.db.DBService;
import org.zgr.notification.sender.service.http.SendNotificationService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.zgr.notification.sender.TestUtil.getLongIn;
import static org.zgr.notification.sender.TestUtil.getRandomString;

@Slf4j
@Import(NotificationTaskSchedulerTest.TestConfig.class)
public class NotificationTaskSchedulerTest extends AppTests {

    private static final List<IntCommQuery> generatedForTest = new ArrayList<>();
    private static final List<IntCommQuery> inMockQuery = new ArrayList<>();
    private static final int messages = 50;

    @Test
    public void processingTest() {
        await().atMost(Duration.ofMinutes(1L)).until(()-> messages == inMockQuery.size());

        Assertions.assertEquals(generatedForTest.size(), inMockQuery.size());
        Assertions.assertTrue(inMockQuery.containsAll(generatedForTest));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public DBService dbServiceTest() {
            DBService std = mock(DBService.class);
            Stream.generate(() ->
                    new IntCommQuery(
                            getRandomString(),
                            getLongIn(0L, 999L),
                            getLongIn(0L, 999L),
                            getLongIn(0L, 999L).intValue(),
                            java.sql.Date.valueOf(LocalDate.now()),
                            java.sql.Date.valueOf(LocalDate.now().plus(1, DAYS)),
                            Time.valueOf(LocalTime.now()),
                            Time.valueOf(LocalTime.now().plus(3, HOURS)),
                            getRandomString(),
                            getRandomString(),
                            getRandomString(),
                            getRandomString(),
                            getRandomString(),
                            getRandomString(),
                            getLongIn(0L, 999L),
                            Timestamp.from(Instant.now().minus(10L, HOURS)),
                            Timestamp.from(Instant.now().minus(10L, HOURS)),
                            IntCommStatus.NEW.name(),
                            null,
                            null,
                            null
                    ))
                    .limit(messages)
                    .forEach(generatedForTest::add);
            doReturn(generatedForTest)
                    .when(std)
                    .receiveNotificationTasks();
            return std;
        }

        @Bean
        @Primary
        public SendNotificationService notificationStatusTest() {
            SendNotificationService std = mock(SendNotificationService.class);
            doAnswer(invocation ->
                    (Runnable) () -> inMockQuery.add(invocation.getArgument(0)))
                    .when(std)
                    .sendNotification(any(IntCommQuery.class));
            return std;
        }
    }
}
