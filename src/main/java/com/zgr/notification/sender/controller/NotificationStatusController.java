package com.zgr.notification.sender.controller;

import com.zgr.notification.sender.model.recieve.NotificationStatus;
import com.zgr.notification.sender.service.http.NotificationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NotificationStatusController {

    private final NotificationStatusService notificationStatusService;

    @PostMapping(value = "${notification.receive.mapping}", consumes = "application/json", produces = "application/json")
    public ResponseEntity receiveStatus(@Valid @RequestBody NotificationStatus status) {
        notificationStatusService.processStatus(status);
        return new ResponseEntity(HttpStatus.OK);
    }
}
