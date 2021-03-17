package org.zgr.notification.sender.service.http.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import org.zgr.notification.sender.enums.MessageType;
import org.zgr.notification.sender.enums.RepeatSendState;
import org.zgr.notification.sender.model.recieve.NotificationResponse;
import org.zgr.notification.sender.model.send.CascadeChainLink;
import org.zgr.notification.sender.model.send.CustomPayload;
import org.zgr.notification.sender.model.send.Data;
import org.zgr.notification.sender.model.send.Message;
import org.zgr.notification.sender.model.send.NotificationRequest;
import org.zgr.notification.sender.model.send.PushContent;
import org.zgr.notification.sender.model.send.PushData;
import org.zgr.notification.sender.model.send.ScheduleInfo;
import org.zgr.notification.sender.service.db.DBService;
import org.zgr.notification.sender.service.http.SendNotificationService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@Service
@SuppressWarnings("deprecated")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SendNotificationServiceImpl implements SendNotificationService {

    private final RestTemplate restTemplate;
    private final DBService dbService;

    @Value("${notification.send.login}")
    private String login;
    @Value("${notification.send.password}")
    private String password;
    @Value("${notification.send.ttl}")
    private int messageTtl;
    @Value("${notification.send.ttlUnit}")
    private String ttlUnit;
    @Value("${notification.send.url}")
    private String url;

    @Value("${notification.service-numbers.366.push}")
    private String serviceNumberPush366;
    @Value("${notification.service-numbers.gorzdrav.push}")
    private String serviceNumberPushGorzdrav;
    @Value("${notification.service-numbers.366.sms}")
    private String serviceNumberSMS366;
    @Value("${notification.service-numbers.gorzdrav.sms}")
    private String serviceNumberSMSGorzdrav;


    @Override
    public Runnable sendNotification(IntCommQuery intCommQuery) {
        return () -> {
            NotificationRequest requestBody = buildRequest(intCommQuery);

            RequestEntity<NotificationRequest> request = RequestEntity
                    .post(URI.create(url))
                    .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                    .body(requestBody);

            log.info("send notification request to:{} with body:{}", url, requestBody.toString());
            ResponseEntity<NotificationResponse> response = restTemplate.exchange(request, NotificationResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && Objects.requireNonNull(response.getBody()).hasBody()) {
                log.info("set status SENT to task with contact id:{}", intCommQuery.getContactId());
                dbService.setStatusSent(intCommQuery.getContactId());
            } else {
                log.error("set status ERROR to task with contact id:{}, status code:{}, message body:{}",
                        intCommQuery.getContactId(), response.getStatusCodeValue(), Objects.requireNonNull(response.getBody()).toString()
                );
                dbService.setErrorStatus(intCommQuery.getContactId(), response.getBody());
            }
        };
    }

    private NotificationRequest buildRequest(IntCommQuery intCommQuery) {
        String smsServiceNum = intCommQuery.getMacrobrend().equals("ГОРЗДРАВ") ? serviceNumberSMSGorzdrav : serviceNumberSMS366;
        String pushServiceNum = intCommQuery.getMacrobrend().equals("ГОРЗДРАВ") ? serviceNumberPushGorzdrav : serviceNumberPush366;
        PushContent pushContent = intCommQuery.getMessageUrl() == null
                ? PushContent.builder()
                .actions(new ArrayList<>())
                .build()
                : PushContent.builder()
                .contentCategory("IMAGE")
                .contentUrl(intCommQuery.getMessageUrl())
                .actions(new ArrayList<>())
                .build();
        Message push = Message.builder()
                .type(MessageType.PUSH)
                .data(PushData.builder()
                        .title(intCommQuery.getMessageHeader())
                        .content(pushContent)
                        .text(intCommQuery.getMessageText())
                        .serviceNumber(pushServiceNum)
                        .ttl(messageTtl)
                        .ttlUnit(ttlUnit)
                        .customPayload(
                                CustomPayload.builder()
                                        .deeplink(intCommQuery.getDeepLink())
                                        .build())
                        .build())
                .build();
        Message sms  = Message.builder()
                .type(MessageType.SMS)
                .data(Data.builder()
                        .text(intCommQuery.getMessageText())
                        .serviceNumber(smsServiceNum)
                        .ttl(messageTtl)
                        .ttlUnit(ttlUnit)
                        .build())
                .build();
        return NotificationRequest.builder()
                .login(login)
                .password(password)
                .useTimeDiff(true)
                .id(intCommQuery.getContactId().toString())
                .scheduleInfo(ScheduleInfo.builder()
                        .timeBegin(intCommQuery.getStartTime().toString())
                        .timeEnd(intCommQuery.getEndTime().toString())
                        .build())
                .destAddr(intCommQuery.getMobilePhone())
                .message(push)
                .cascadeChainLink(CascadeChainLink.builder()
                        .state(RepeatSendState.DELIVERED)
                        .message(sms)
                        .build())
                .build();
    }
}
