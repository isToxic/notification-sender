package com.zgr.notification.sender.controller;

import com.zgr.notification.sender.AppTests;
import com.zgr.notification.sender.TestUtil;
import com.zgr.notification.sender.enums.MessageType;
import com.zgr.notification.sender.model.recieve.NotificationStatus;
import com.zgr.notification.sender.service.http.NotificationStatusService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.sql.Timestamp;
import java.time.Instant;

import static io.restassured.RestAssured.with;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@Import(NotificationStatusControllerTest.TestConfig.class)
public class NotificationStatusControllerTest extends AppTests {
    private static boolean inMock = false;
    private static NotificationStatus inMockStatus;

    @NonNull
    @Value("${notification.receive.mapping}")
    private String mapping;

    @NonNull
    @Value("${server.port}")
    private Integer port;

    @Test
    public void receiveStatusTest(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        NotificationStatus request = NotificationStatus.builder()
                .id(TestUtil.getRandomString())
                .mtNum(TestUtil.getRandomString())
                .status(2)
                .type(MessageType.SMS.name())
                .doneDate(Timestamp.from(Instant.now()))
                .submitDate(Timestamp.from(Instant.now().minusSeconds(700L)))
                .destAddr(TestUtil.getRandomString())
                .sourceAddr(TestUtil.getRandomString())
                .text(TestUtil.getRandomString())
                .partCount(TestUtil.getRandomString())
                .errorCode(TestUtil.getRandomString())
                .mccMnc(TestUtil.getRandomString())
                .trafficType(TestUtil.getLongIn(0,3).intValue())
                .build();

        with()
                .header("Content-Type", ContentType.JSON)
                .body(request)
                .when()
                .request(Method.POST, mapping.substring(1))
                .then()
                .statusCode(200);

        Assertions.assertTrue(inMock);

        Assertions.assertEquals(request, inMockStatus);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public NotificationStatusService notificationStatusTest() {
            NotificationStatusService std = mock(NotificationStatusService.class);
            doAnswer(invocation -> {
                inMock = true;
                inMockStatus = invocation.getArgument(0);
                return null;
            })
                    .when(std)
                    .processStatus(any(NotificationStatus.class));
            return std;
        }
    }
}
