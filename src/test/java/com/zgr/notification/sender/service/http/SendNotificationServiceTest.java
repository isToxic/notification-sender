package com.zgr.notification.sender.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zgr.notification.sender.AppTests;
import com.zgr.notification.sender.StatusLogic;
import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import com.zgr.notification.sender.enums.IntCommStatus;
import com.zgr.notification.sender.enums.MessageType;
import com.zgr.notification.sender.enums.ResponseError;
import com.zgr.notification.sender.model.recieve.NotificationResponse;
import com.zgr.notification.sender.service.db.DBService;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.zgr.notification.sender.TestUtil.getLongIn;
import static com.zgr.notification.sender.TestUtil.getRandomString;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@AutoConfigureCache
@AutoConfigureWebClient
@AutoConfigureMockRestServiceServer
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(SendNotificationServiceTest.TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SendNotificationServiceTest extends AppTests {

    @Value("${notification.send.url}")
    private String url;
    @Value("${notification.send.login}")
    private String login;
    @Value("${notification.send.password}")
    private String password;
    @Value("${notification.send.ttl}")
    private int messageTtl;
    @Value("${notification.send.ttlUnit}")
    private String ttlUnit;

    @Value("${notification.service-numbers.366.push}")
    private String serviceNumberPush366;
    @Value("${notification.service-numbers.gorzdrav.push}")
    private String serviceNumberPushGorzdrav;
    @Value("${notification.service-numbers.366.sms}")
    private String serviceNumberSMS366;
    @Value("${notification.service-numbers.gorzdrav.sms}")
    private String serviceNumberSMSGorzdrav;

    private final RestTemplate restTemplate;
    private final SendNotificationService sendNotificationService;
    private MockRestServiceServer server;

    private static StatusLogic logic;

    @Test
    public void sendNotificationTest() throws JsonProcessingException {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        server = MockRestServiceServer.createServer(gateway);
        String response = new ObjectMapper().writeValueAsString(
                NotificationResponse.builder()
                        .id(getRandomString())
                        .mtNum(getRandomString())
                        .build());

        IntCommQuery requestQuery = new IntCommQuery(
                getRandomString(),
                getLongIn(0L, 999L),
                getLongIn(0L, 999L),
                getLongIn(0L, 999L).intValue(),
                java.sql.Date.valueOf(LocalDate.now()),
                java.sql.Date.valueOf(LocalDate.now().plus(1, DAYS)),
                Time.valueOf(LocalTime.now()),
                Time.valueOf(LocalTime.now().plus(3, HOURS)),
                "ГОРЗДРАВ",
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
        );
        server.expect(ExpectedCount.once(), requestTo(url))
                .andExpect(MockRestRequestMatchers.jsonPath("$.id", Matchers.equalToIgnoringCase(requestQuery.getContactId().toString())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.scheduleInfo.timeBegin", Matchers.equalToIgnoringCase(requestQuery.getStartTime().toString())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.scheduleInfo.timeEnd", Matchers.equalToIgnoringCase(requestQuery.getEndTime().toString())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.login", Matchers.equalToIgnoringCase(login)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.password", Matchers.equalToIgnoringCase(password)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.useTimeDiff", Matchers.equalTo(true)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.destAddr", Matchers.equalToIgnoringCase(requestQuery.getMobilePhone())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.type", Matchers.equalToIgnoringCase(MessageType.PUSH.name())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.title", Matchers.equalToIgnoringCase(requestQuery.getMessageHeader())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.text", Matchers.equalToIgnoringCase(requestQuery.getMessageText())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.serviceNumber", Matchers.equalToIgnoringCase(serviceNumberPushGorzdrav)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.ttl", Matchers.equalTo(messageTtl)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.ttlUnit", Matchers.equalTo(ttlUnit)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.customPayload.deeplink", Matchers.equalTo(requestQuery.getDeepLink())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.content.contentUrl", Matchers.equalToIgnoringCase(requestQuery.getMessageUrl())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.content.contentCategory", Matchers.equalToIgnoringCase("IMAGE")))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.state", Matchers.equalToIgnoringCase("DELIVERED")))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.type", Matchers.equalToIgnoringCase(MessageType.SMS.name())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.text", Matchers.equalToIgnoringCase(requestQuery.getMessageText())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.serviceNumber", Matchers.equalToIgnoringCase(serviceNumberSMSGorzdrav)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.ttl", Matchers.equalTo(messageTtl)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.ttlUnit", Matchers.equalTo(ttlUnit)))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        sendNotificationService.sendNotification(requestQuery).run();
        server.verify();

        Assertions.assertEquals(StatusLogic.SENT, logic);
    }

    @Test
    public void sendNotificationErrorTest() throws JsonProcessingException {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        server = MockRestServiceServer.createServer(gateway);
        String response = new ObjectMapper().writeValueAsString(
                NotificationResponse.builder()
                        .id(getRandomString())
                        .error(ResponseError.INTERNAL_ERROR)
                        .build());

        IntCommQuery requestQuery = new IntCommQuery(
                getRandomString(),
                getLongIn(0L, 999L),
                getLongIn(0L, 999L),
                getLongIn(0L, 999L).intValue(),
                java.sql.Date.valueOf(LocalDate.now()),
                java.sql.Date.valueOf(LocalDate.now().plus(1, DAYS)),
                Time.valueOf(LocalTime.now()),
                Time.valueOf(LocalTime.now().plus(3, HOURS)),
                "36.6",
                getRandomString(),
                getRandomString(),
                getRandomString(),
                getRandomString(),
                null,
                getLongIn(0L, 999L),
                Timestamp.from(Instant.now().minus(10L, HOURS)),
                Timestamp.from(Instant.now().minus(10L, HOURS)),
                IntCommStatus.NEW.name(),
                null,
                null,
                null
        );
        server.expect(ExpectedCount.once(), requestTo(url))
                .andExpect(MockRestRequestMatchers.jsonPath("$.id", Matchers.equalToIgnoringCase(requestQuery.getContactId().toString())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.scheduleInfo.timeBegin", Matchers.equalToIgnoringCase(requestQuery.getStartTime().toString())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.scheduleInfo.timeEnd", Matchers.equalToIgnoringCase(requestQuery.getEndTime().toString())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.login", Matchers.equalToIgnoringCase(login)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.password", Matchers.equalToIgnoringCase(password)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.useTimeDiff", Matchers.equalTo(true)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.destAddr", Matchers.equalToIgnoringCase(requestQuery.getMobilePhone())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.type", Matchers.equalToIgnoringCase(MessageType.PUSH.name())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.title", Matchers.equalToIgnoringCase(requestQuery.getMessageHeader())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.text", Matchers.equalToIgnoringCase(requestQuery.getMessageText())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.serviceNumber", Matchers.equalToIgnoringCase(serviceNumberPush366)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.ttl", Matchers.equalTo(messageTtl)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.ttlUnit", Matchers.equalTo(ttlUnit)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.customPayload.deeplink", Matchers.equalTo(requestQuery.getDeepLink())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.content.contentUrl", Matchers.nullValue()))
                .andExpect(MockRestRequestMatchers.jsonPath("$.message.data.content.contentCategory", Matchers.nullValue()))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.state", Matchers.equalToIgnoringCase("DELIVERED")))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.type", Matchers.equalToIgnoringCase(MessageType.SMS.name())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.text", Matchers.equalToIgnoringCase(requestQuery.getMessageText())))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.serviceNumber", Matchers.equalToIgnoringCase(serviceNumberSMS366)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.ttl", Matchers.equalTo(messageTtl)))
                .andExpect(MockRestRequestMatchers.jsonPath("$.cascadeChainLink.message.data.ttlUnit", Matchers.equalTo(ttlUnit)))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        sendNotificationService.sendNotification(requestQuery).run();
        server.verify();

        Assertions.assertEquals(StatusLogic.ERROR, logic);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public DBService notificationStatusTest() {
            DBService std = mock(DBService.class);
            doAnswer(invocation -> logic = StatusLogic.SENT)
                    .when(std)
                    .setStatusSent(any());
            doAnswer(invocation -> logic = StatusLogic.ERROR)
                    .when(std)
                    .setErrorStatus(any(), any());
            return std;
        }
    }
}
