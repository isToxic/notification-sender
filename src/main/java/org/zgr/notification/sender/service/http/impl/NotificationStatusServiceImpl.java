package org.zgr.notification.sender.service.http.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zgr.notification.sender.db.jooq.tables.pojos.IntCommResponse;
import org.zgr.notification.sender.enums.IntCommStatus;
import org.zgr.notification.sender.enums.MessageType;
import org.zgr.notification.sender.enums.NotificationResponseError;
import org.zgr.notification.sender.enums.NotificationResponseStatus;
import org.zgr.notification.sender.model.recieve.NotificationStatus;
import org.zgr.notification.sender.service.db.DBService;
import org.zgr.notification.sender.service.http.NotificationStatusService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NotificationStatusServiceImpl implements NotificationStatusService {

    private final DBService dbService;

    @Override
    public void processStatus(NotificationStatus notificationStatus) {
        IntCommResponse result = new IntCommResponse();
        log.info("process notification status message:{}", notificationStatus.toString());
        if (notificationStatus.getType().equals(MessageType.SMS.name())) {
            if (NotificationResponseStatus.getByCode(notificationStatus.getStatus()).equals(NotificationResponseStatus.UNDELIVERED)) {
                getRecordForSMSError(result, notificationStatus);
                dbService.saveNotificationStatusSMSError(result);
            } else {
                getRecordForSMS(result, notificationStatus);
                dbService.saveNotificationStatusSMS(result);
            }
        } else {
            if (NotificationResponseStatus.getByCode(notificationStatus.getStatus()).equals(NotificationResponseStatus.UNDELIVERED)) {

                getRecordForPUSHError(result, notificationStatus);
                dbService.saveNotificationStatusPUSHError(result);
            } else {

                getRecordForPUSH(result, notificationStatus);
                dbService.saveNotificationStatusPUSH(result);
            }
        }
        log.info("process notification status message with contact id:{} successfully finished", notificationStatus.getId());
    }

    private void getRecordForSMS(IntCommResponse intCommResponse, NotificationStatus notificationStatus) {
        prepareSMSResponse(intCommResponse, notificationStatus);
        intCommResponse.setErrorCode(0);
    }

    private void getRecordForSMSError(IntCommResponse intCommResponse, NotificationStatus notificationStatus) {
        prepareSMSResponse(intCommResponse, notificationStatus);
        intCommResponse.setErrorCode(Integer.valueOf(notificationStatus.getErrorCode()));
        intCommResponse.setErrorText(NotificationResponseError.getDescriptionByCode(Integer.parseInt(notificationStatus.getErrorCode())));
    }

    private void getRecordForPUSH(IntCommResponse intCommResponse, NotificationStatus notificationStatus) {
        preparePushResponse(intCommResponse, notificationStatus);
        intCommResponse.setErrorCode(0);
    }

    private void getRecordForPUSHError(IntCommResponse intCommResponse, NotificationStatus notificationStatus) {
        preparePushResponse(intCommResponse, notificationStatus);
        intCommResponse.setErrorCode(Integer.valueOf(notificationStatus.getErrorCode()));
        intCommResponse.setErrorText(NotificationResponseError.getDescriptionByCode(Integer.parseInt(notificationStatus.getErrorCode())));
    }

    private void prepareSMSResponse(IntCommResponse intCommResponse, NotificationStatus notificationStatus) {
        prepareForAll(intCommResponse, notificationStatus);
        intCommResponse.setPartCount(Integer.valueOf(notificationStatus.getPartCount()));
        intCommResponse.setCommCost(new BigDecimal(notificationStatus.getSegmentPrice()).multiply(BigDecimal.valueOf(intCommResponse.getPartCount())));
    }

    private void preparePushResponse(IntCommResponse intCommResponse, NotificationStatus notificationStatus) {
        prepareForAll(intCommResponse, notificationStatus);
        intCommResponse.setCommCost(new BigDecimal(notificationStatus.getSegmentPrice()));
    }

    private void prepareForAll(IntCommResponse intCommResponse, NotificationStatus notificationStatus) {
        intCommResponse.setContactId(Long.getLong(notificationStatus.getId()));
        intCommResponse.setIntStatus(IntCommStatus.NEW.name());
        intCommResponse.setIntUpdateDttm(Timestamp.from(Instant.now()));
        intCommResponse.setMessageType(notificationStatus.getType());
        intCommResponse.setResponseDttm(notificationStatus.getDoneDate());
        intCommResponse.setResponseNm(NotificationResponseStatus.getByCode(notificationStatus.getStatus()).name());
    }
}
