package org.zgr.notification.sender.controller;

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
import org.zgr.notification.sender.AppTests;
import org.zgr.notification.sender.enums.MessageType;
import org.zgr.notification.sender.model.recieve.NotificationStatus;
import org.zgr.notification.sender.service.http.NotificationStatusService;

import java.time.Instant;

import static io.restassured.RestAssured.with;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.zgr.notification.sender.TestUtil.getLongIn;
import static org.zgr.notification.sender.TestUtil.getRandomString;

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
                .id(getRandomString())
                .mtNum(getRandomString())
                .status(2)
                .type(MessageType.SMS.name())
                .doneDate(Instant.now().toString())
                .submitDate(Instant.now().minusSeconds(700L).toString())
                .destAddr(getRandomString())
                .sourceAddr(getRandomString())
                .text(getRandomString())
                .partCount(getRandomString())
                .errorCode(getRandomString())
                .mccMnc(getRandomString())
                .trafficType(getLongIn(0,3).toString())
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
