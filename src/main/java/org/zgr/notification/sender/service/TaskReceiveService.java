package org.zgr.notification.sender.service;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zgr.notification.sender.db.jooq.tables.records.IntCommQueryRecord;
import org.zgr.notification.sender.db.jooq.tables.records.IntDeactivateListRecord;
import org.zgr.notification.sender.enums.IntCommStatus;
import org.zgr.notification.sender.enums.IntDeactivateListStatus;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.zgr.notification.sender.db.jooq.Tables.INT_DEACTIVATE_LIST;
import static org.zgr.notification.sender.db.jooq.tables.IntCommQuery.INT_COMM_QUERY;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskReceiveService {

    private final DSLContext dsl;

    @Value("${notification.receive.wait-before-send-minutes}")
    long waitBeforeSend;

    long taskLimit = 10;

    int deactivateErrorCode = 34;
    String deactivateErrorDescription = "changed";

    public void receiveNotificationTasks() {

        dsl.transactionResult(t -> {
            // Вычитываем список задач на отправку уведомлений с временем создания > waitBeforeSend и со статусом NEW
            Result<IntCommQueryRecord> newNotificationTasks = t.dsl().selectFrom(INT_COMM_QUERY)
                    .where(INT_COMM_QUERY.INT_STATUS.eq(IntCommStatus.NEW.name())
                            .and(INT_COMM_QUERY.INT_CREATE_DTTM.greaterOrEqual(
                                    Timestamp.from(Instant.now().plus(waitBeforeSend, MINUTES)))))
                    .limit(taskLimit)
                    .fetch();

            // Проставляем в БД статус PROCESSING для обрабатываемых задач
            t.dsl().update(INT_COMM_QUERY)
                    .set(INT_COMM_QUERY.INT_STATUS, IntCommStatus.PROCESSING.name())
                    .set(INT_COMM_QUERY.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                    .where(INT_COMM_QUERY.CONTACT_ID.in(
                            newNotificationTasks.stream()
                                    .map(IntCommQueryRecord::getContactId)
                                    .collect(Collectors.toList())))
                    .execute();

            // Вычитываем список деактиваций со статусом NEW
            Result<IntDeactivateListRecord> newDeactivateTasks = t.dsl().selectFrom(INT_DEACTIVATE_LIST)
                    .where(INT_DEACTIVATE_LIST.INT_STATUS.eq(IntDeactivateListStatus.NEW.name()))
                    .fetch();

            // Отфильтровываем список задач на дективацию
            List<IntCommQueryRecord> tasksToDeactivate = newNotificationTasks.parallelStream()
                    .filter(intCommQuery ->
                            newDeactivateTasks.stream()
                                    .map(IntDeactivateListRecord::getContactId)
                                    .collect(Collectors.toList())
                                    .contains(intCommQuery.getContactId()))
                    .collect(Collectors.toList());

            if (tasksToDeactivate.size() != 0) {
                // проставляем для задач на дективацию статус ERROR, код ошибки и описание
                t.dsl().update(INT_COMM_QUERY)
                        .set(INT_COMM_QUERY.INT_STATUS, IntCommStatus.ERROR.name())
                        .set(INT_COMM_QUERY.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                        .set(INT_COMM_QUERY.INT_ERROR_CODE, deactivateErrorCode)
                        .set(INT_COMM_QUERY.INT_ERROR_TEXT, deactivateErrorDescription)
                        .where(INT_COMM_QUERY.CONTACT_ID.in(
                                tasksToDeactivate.stream()
                                        .map(IntCommQueryRecord::getContactId)
                                        .collect(Collectors.toList())))
                .execute();

                t.dsl().update(INT_DEACTIVATE_LIST)
                        .set(INT_DEACTIVATE_LIST.INT_UPDATE_DTTM, Timestamp.from(Instant.now()))
                        .set(INT_DEACTIVATE_LIST.INT_STATUS, IntDeactivateListStatus.DONE.name())
                        .where(INT_DEACTIVATE_LIST.CONTACT_ID.in(
                                tasksToDeactivate.stream()
                                        .map(IntCommQueryRecord::getContactId)
                                        .collect(Collectors.toList())))
                .execute();
                // Убираем из списка задач - деактивированные
                newNotificationTasks.removeAll(tasksToDeactivate);
            }
            return newNotificationTasks;
        });
    }
}
