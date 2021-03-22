package com.zgr.notification.sender.service.db.impl;

import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommResponse;
import com.zgr.notification.sender.db.jooq.tables.pojos.IntDeactivateList;
import com.zgr.notification.sender.enums.IntCommStatus;
import com.zgr.notification.sender.enums.IntDeactivateListStatus;
import com.zgr.notification.sender.enums.NotificationResponseStatus;
import com.zgr.notification.sender.model.recieve.NotificationResponse;
import com.zgr.notification.sender.service.db.DBService;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.zgr.notification.sender.db.jooq.Tables.INT_COMM_RESPONSE;
import static com.zgr.notification.sender.db.jooq.Tables.INT_DEACTIVATE_LIST;
import static com.zgr.notification.sender.db.jooq.tables.IntCommQuery.INT_COMM_QUERY;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBServiceImpl implements DBService {

    private final DSLContext dsl;

    @Value("${notification.receive.wait-before-send-minutes}")
    long waitBeforeSend;
    @Value("${notification.receive.task-limit}")
    int taskLimit;

    final int DEACTIVATE_ERROR_CODE = 34;
    final String DEACTIVATE_ERROR_DESCRIPTION = "deactivate";

    public List<IntCommQuery> receiveNotificationTasks() {
        HashSet<Long> idsInProcess = new HashSet<>();
        HashSet<Long> idsToDeactivate = new HashSet<>();
        // Вычитываем список задач на отправку уведомлений с временем создания > waitBeforeSend и со статусом NEW и датой отправки в указанных интервалах
        List<IntCommQuery> newNotificationTasks = dsl.selectFrom(INT_COMM_QUERY)
                .where(INT_COMM_QUERY.INT_STATUS.eq(IntCommStatus.NEW.name())
                        .and(INT_COMM_QUERY.INT_CREATE_DTTM.lessOrEqual(Timestamp.from(Instant.now().minus(waitBeforeSend, MINUTES))))
                        .and(INT_COMM_QUERY.START_DATE.lessOrEqual(Date.valueOf(LocalDate.now())))
                        .and(INT_COMM_QUERY.END_DATE.greaterOrEqual(Date.valueOf(LocalDate.now()))))
                .limit(taskLimit)
                .fetchInto(IntCommQuery.class);

        if (newNotificationTasks.size() != 0) {
            newNotificationTasks.forEach(task -> idsInProcess.add(task.getContactId()));
            // Проставляем в БД статус PROCESSING для обрабатываемых задач
            dsl.update(INT_COMM_QUERY)
                    .set(INT_COMM_QUERY.INT_STATUS, IntCommStatus.PROCESSING.name())
                    .set(INT_COMM_QUERY.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                    .where(INT_COMM_QUERY.CONTACT_ID.in(idsInProcess))
                    .execute();

            // Вычитываем список деактиваций со статусом NEW и id из списка обрабатываемых задач
            List<IntDeactivateList> deactivateTasks = dsl.selectFrom(INT_DEACTIVATE_LIST)
                    .where(INT_DEACTIVATE_LIST.INT_STATUS.eq(IntDeactivateListStatus.NEW.name())
                            .and(INT_DEACTIVATE_LIST.CONTACT_ID.in(idsInProcess)))
                    .fetchInto(IntDeactivateList.class);

            if (deactivateTasks.size() != 0) {
                deactivateTasks.forEach(task -> idsToDeactivate.add(task.getContactId()));
                // проставляем для задач на деактивацию статус ERROR, код ошибки и описание
                dsl.update(INT_COMM_QUERY)
                        .set(INT_COMM_QUERY.INT_STATUS, IntCommStatus.ERROR.name())
                        .set(INT_COMM_QUERY.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                        .set(INT_COMM_QUERY.INT_ERROR_CODE, DEACTIVATE_ERROR_CODE)
                        .set(INT_COMM_QUERY.INT_ERROR_TEXT, DEACTIVATE_ERROR_DESCRIPTION)
                        .where(INT_COMM_QUERY.CONTACT_ID.in(idsToDeactivate))
                        .execute();

                dsl.update(INT_DEACTIVATE_LIST)
                        .set(INT_DEACTIVATE_LIST.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                        .set(INT_DEACTIVATE_LIST.INT_STATUS, IntDeactivateListStatus.DONE.name())
                        .where(INT_DEACTIVATE_LIST.CONTACT_ID.in(idsToDeactivate))
                        .execute();
                // Убираем из списка задач - деактивированные
                newNotificationTasks.removeIf(task -> idsToDeactivate.contains(task.getContactId()));
            }
        }
        return newNotificationTasks;
    }

    @Override
    public void setStatusSent(Long contactId) {
        dsl.update(INT_COMM_QUERY)
                .set(INT_COMM_QUERY.INT_STATUS, IntCommStatus.SENT.name())
                .set(INT_COMM_QUERY.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                .set(INT_COMM_QUERY.INT_ERROR_CODE, 0)
                .where(INT_COMM_QUERY.CONTACT_ID.eq(contactId))
                .execute();
    }

    @Override
    public void setErrorStatus(Long contactId, NotificationResponse response) {
        dsl.update(INT_COMM_QUERY)
                .set(INT_COMM_QUERY.INT_STATUS, IntCommStatus.ERROR.name())
                .set(INT_COMM_QUERY.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                .set(INT_COMM_QUERY.INT_ERROR_CODE, response.getError().getCode())
                .set(INT_COMM_QUERY.INT_ERROR_TEXT, response.getError().getDescription())
                .where(INT_COMM_QUERY.CONTACT_ID.eq(contactId))
                .execute();
    }

    @Override
    public void saveNotificationStatusSMS(IntCommResponse intCommResponse) {
        dsl.insertInto(INT_COMM_RESPONSE)
                .set(INT_COMM_RESPONSE.CONTACT_ID, intCommResponse.getContactId())
                .set(INT_COMM_RESPONSE.RESPONSE_NM, intCommResponse.getResponseNm())
                .set(INT_COMM_RESPONSE.MESSAGE_TYPE, intCommResponse.getMessageType())
                .set(INT_COMM_RESPONSE.PART_COUNT, intCommResponse.getPartCount())
                .set(INT_COMM_RESPONSE.COMM_COST, intCommResponse.getCommCost())
                .set(INT_COMM_RESPONSE.ERROR_CODE, intCommResponse.getErrorCode())
                .setNull(INT_COMM_RESPONSE.ERROR_TEXT)
                .set(INT_COMM_RESPONSE.RESPONSE_DTTM, intCommResponse.getResponseDttm())
                .set(INT_COMM_RESPONSE.INT_UPDATE_DTTM, intCommResponse.getIntUpdateDttm())
                .set(INT_COMM_RESPONSE.INT_STATUS, intCommResponse.getIntStatus())
                .execute();
    }

    @Override
    public void saveNotificationStatusSMSError(IntCommResponse intCommResponse) {
        dsl.insertInto(INT_COMM_RESPONSE)
                .set(INT_COMM_RESPONSE.CONTACT_ID, intCommResponse.getContactId())
                .set(INT_COMM_RESPONSE.RESPONSE_NM, intCommResponse.getResponseNm())
                .set(INT_COMM_RESPONSE.MESSAGE_TYPE, intCommResponse.getMessageType())
                .set(INT_COMM_RESPONSE.PART_COUNT, intCommResponse.getPartCount())
                .set(INT_COMM_RESPONSE.COMM_COST, intCommResponse.getCommCost())
                .set(INT_COMM_RESPONSE.ERROR_CODE, intCommResponse.getErrorCode())
                .set(INT_COMM_RESPONSE.ERROR_TEXT, intCommResponse.getErrorText())
                .set(INT_COMM_RESPONSE.RESPONSE_DTTM, intCommResponse.getResponseDttm())
                .set(INT_COMM_RESPONSE.INT_UPDATE_DTTM, intCommResponse.getIntUpdateDttm())
                .set(INT_COMM_RESPONSE.INT_STATUS, intCommResponse.getIntStatus())
                .execute();
    }

    @Override
    public void saveNotificationStatusPUSH(IntCommResponse intCommResponse) {
        if (intCommResponse.getResponseNm().equals(NotificationResponseStatus.OPEN.name())){
            dsl.insertInto(INT_COMM_RESPONSE)
                    .set(INT_COMM_RESPONSE.CONTACT_ID, intCommResponse.getContactId())
                    .set(INT_COMM_RESPONSE.RESPONSE_NM, intCommResponse.getResponseNm())
                    .set(INT_COMM_RESPONSE.MESSAGE_TYPE, intCommResponse.getMessageType())
                    .setNull(INT_COMM_RESPONSE.PART_COUNT)
                    .set(INT_COMM_RESPONSE.COMM_COST, BigDecimal.ZERO)
                    .set(INT_COMM_RESPONSE.ERROR_CODE, intCommResponse.getErrorCode())
                    .setNull(INT_COMM_RESPONSE.ERROR_TEXT)
                    .set(INT_COMM_RESPONSE.RESPONSE_DTTM, intCommResponse.getResponseDttm())
                    .set(INT_COMM_RESPONSE.INT_UPDATE_DTTM, intCommResponse.getIntUpdateDttm())
                    .set(INT_COMM_RESPONSE.INT_STATUS, intCommResponse.getIntStatus())
                    .execute();
        } else {
            dsl.insertInto(INT_COMM_RESPONSE)
                    .set(INT_COMM_RESPONSE.CONTACT_ID, intCommResponse.getContactId())
                    .set(INT_COMM_RESPONSE.RESPONSE_NM, intCommResponse.getResponseNm())
                    .set(INT_COMM_RESPONSE.MESSAGE_TYPE, intCommResponse.getMessageType())
                    .setNull(INT_COMM_RESPONSE.PART_COUNT)
                    .set(INT_COMM_RESPONSE.COMM_COST, intCommResponse.getCommCost())
                    .set(INT_COMM_RESPONSE.ERROR_CODE, intCommResponse.getErrorCode())
                    .setNull(INT_COMM_RESPONSE.ERROR_TEXT)
                    .set(INT_COMM_RESPONSE.RESPONSE_DTTM, intCommResponse.getResponseDttm())
                    .set(INT_COMM_RESPONSE.INT_UPDATE_DTTM, intCommResponse.getIntUpdateDttm())
                    .set(INT_COMM_RESPONSE.INT_STATUS, intCommResponse.getIntStatus())
                    .execute();
        }
    }

    @Override
    public void saveNotificationStatusPUSHError(IntCommResponse intCommResponse) {
        dsl.insertInto(INT_COMM_RESPONSE)
                .set(INT_COMM_RESPONSE.CONTACT_ID, intCommResponse.getContactId())
                .set(INT_COMM_RESPONSE.RESPONSE_NM, intCommResponse.getResponseNm())
                .set(INT_COMM_RESPONSE.MESSAGE_TYPE, intCommResponse.getMessageType())
                .setNull(INT_COMM_RESPONSE.PART_COUNT)
                .set(INT_COMM_RESPONSE.COMM_COST, intCommResponse.getCommCost())
                .set(INT_COMM_RESPONSE.ERROR_CODE, intCommResponse.getErrorCode())
                .set(INT_COMM_RESPONSE.ERROR_TEXT, intCommResponse.getErrorText())
                .set(INT_COMM_RESPONSE.RESPONSE_DTTM, intCommResponse.getResponseDttm())
                .set(INT_COMM_RESPONSE.INT_UPDATE_DTTM, intCommResponse.getIntUpdateDttm())
                .set(INT_COMM_RESPONSE.INT_STATUS, intCommResponse.getIntStatus())
                .execute();
    }
}
