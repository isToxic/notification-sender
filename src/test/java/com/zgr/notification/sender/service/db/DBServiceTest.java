package com.zgr.notification.sender.service.db;

import com.zgr.notification.sender.AppTests;
import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommQuery;
import com.zgr.notification.sender.db.jooq.tables.pojos.IntCommResponse;
import com.zgr.notification.sender.db.jooq.tables.records.IntCommQueryRecord;
import com.zgr.notification.sender.enums.IntCommStatus;
import com.zgr.notification.sender.enums.IntDeactivateListStatus;
import com.zgr.notification.sender.enums.MessageType;
import com.zgr.notification.sender.enums.NotificationResponseError;
import com.zgr.notification.sender.enums.NotificationResponseStatus;
import com.zgr.notification.sender.enums.ResponseError;
import com.zgr.notification.sender.model.recieve.NotificationResponse;
import com.zgr.notification.sender.service.http.SendNotificationService;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.zgr.notification.sender.TestUtil.getLongIn;
import static com.zgr.notification.sender.TestUtil.getRandomString;
import static com.zgr.notification.sender.db.jooq.Tables.INT_DEACTIVATE_LIST;
import static com.zgr.notification.sender.db.jooq.tables.IntCommQuery.INT_COMM_QUERY;
import static com.zgr.notification.sender.db.jooq.tables.IntCommResponse.INT_COMM_RESPONSE;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@Import(DBServiceTest.TestConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DBServiceTest extends AppTests {

    private final DSLContext dsl;
    private final DBService dbService;

    @Value("${notification.receive.wait-before-send-minutes}")
    long waitBeforeSend;
    @Value("${notification.receive.task-limit}")
    int taskLimit;

    @Test
    @Transactional
    public void receiveNotificationTasksTest() {
        List<IntCommQueryRecord> recordsToSave = new ArrayList<>();
        // ?????????????????? ???????????????????? ????????????
        IntCommQueryRecord correctRecord = new IntCommQueryRecord(
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
                Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                IntCommStatus.NEW.name(),
                null,
                null,
                null
        );
        recordsToSave.add(correctRecord);

        // ?????????????????? ???????????? ???? ???????????????? ???? NEW

        recordsToSave.add(
                new IntCommQueryRecord(
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
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        IntCommStatus.PROCESSING.name(),
                        null,
                        null,
                        null)
        );

        recordsToSave.add(
                new IntCommQueryRecord(
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
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        IntCommStatus.ERROR.name(),
                        null,
                        null,
                        null)
        );

        recordsToSave.add(
                new IntCommQueryRecord(
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
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        IntCommStatus.SENT.name(),
                        null,
                        null,
                        null)
        );

        // ?????????????????? ???????????? ?? limit - 1 ?????????? ?????????? ????????????????

        recordsToSave.add(
                new IntCommQueryRecord(
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
                        Timestamp.from(Instant.now().minus(waitBeforeSend - 1, MINUTES)),
                        Timestamp.from(Instant.now().minus(waitBeforeSend - 1, MINUTES)),
                        IntCommStatus.NEW.name(),
                        null,
                        null,
                        null)
        );

        // ?????????????????? ???????????? ?? ?????????? ???????????? ????????????

        recordsToSave.add(
                new IntCommQueryRecord(
                        getRandomString(),
                        getLongIn(0L, 999L),
                        getLongIn(0L, 999L),
                        getLongIn(0L, 999L).intValue(),
                        java.sql.Date.valueOf(LocalDate.now().plus(1, DAYS)),
                        java.sql.Date.valueOf(LocalDate.now().plus(2, DAYS)),
                        Time.valueOf(LocalTime.now()),
                        Time.valueOf(LocalTime.now().plus(3, HOURS)),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getLongIn(0L, 999L),
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        IntCommStatus.NEW.name(),
                        null,
                        null,
                        null)
        );

        // ?????????????????? ???????????? ?? ?????????? ?????????? ??????????

        recordsToSave.add(
                new IntCommQueryRecord(
                        getRandomString(),
                        getLongIn(0L, 999L),
                        getLongIn(0L, 999L),
                        getLongIn(0L, 999L).intValue(),
                        java.sql.Date.valueOf(LocalDate.now()),
                        java.sql.Date.valueOf(LocalDate.now().minus(1, DAYS)),
                        Time.valueOf(LocalTime.now()),
                        Time.valueOf(LocalTime.now().plus(3, HOURS)),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getRandomString(),
                        getLongIn(0L, 999L),
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                        IntCommStatus.NEW.name(),
                        null,
                        null,
                        null)
        );

        recordsToSave.forEach(recordToSave ->
                dsl.insertInto(INT_COMM_QUERY)
                        .set(recordToSave)
                        .execute());

        // ?????????????????? ?????????????????????? ???? ???????????????? DONE ?? ?? id ???????????????????? ????????????

        dsl.insertInto(INT_DEACTIVATE_LIST)
                .set(INT_DEACTIVATE_LIST.CONTACT_ID, correctRecord.getContactId())
                .set(INT_DEACTIVATE_LIST.INT_UPDATE_DTTM, Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)))
                .set(INT_DEACTIVATE_LIST.INT_STATUS, IntDeactivateListStatus.DONE.name())
                .execute();

        List<IntCommQuery> resultList = dbService.receiveNotificationTasks();
        Assertions.assertEquals(1, resultList.size());
        IntCommQuery result = resultList.get(0);

        Assertions.assertEquals(correctRecord.getContactId(), result.getContactId());
        Assertions.assertEquals(correctRecord.getCreCardId(), result.getCreCardId());
        Assertions.assertEquals(correctRecord.getCampaignCd(), result.getCampaignCd());
        Assertions.assertEquals(correctRecord.getDeepLink(), result.getDeepLink());
        Assertions.assertEquals(correctRecord.getEndDate(), result.getEndDate());
        Assertions.assertEquals(correctRecord.getEndTime(), result.getEndTime());
        Assertions.assertEquals(correctRecord.getStartDate(), result.getStartDate());
        Assertions.assertEquals(correctRecord.getStartTime(), result.getStartTime());
        Assertions.assertEquals(correctRecord.getIntCreateDttm(), result.getIntCreateDttm());
        Assertions.assertEquals(correctRecord.getIntQueryId(), result.getIntQueryId());
        Assertions.assertEquals(correctRecord.getOfferId(), result.getOfferId());
        Assertions.assertEquals(correctRecord.getMacrobrend(), result.getMacrobrend());
        Assertions.assertEquals(correctRecord.getMessageHeader(), result.getMessageHeader());
        Assertions.assertEquals(correctRecord.getMessageText(), result.getMessageText());
        Assertions.assertEquals(correctRecord.getMessageUrl(), result.getMessageUrl());
        Assertions.assertEquals(correctRecord.getMobilePhone(), result.getMobilePhone());
        Assertions.assertEquals(correctRecord.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(correctRecord.getIntStatus(), result.getIntStatus());
    }

    @Test
    @Transactional
    public void receiveNotificationTasksLimitTest() {
        long limitTasks = getLongIn(100L, 999L);
        List<Long> queryNums = LongStream.range(0L,limitTasks).boxed().collect(Collectors.toList());
        Stream.generate(() -> {
            Long queryId = queryNums.get(0);
            queryNums.remove(queryId);
            return new IntCommQueryRecord(
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
                    queryId,
                    Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                    Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                    IntCommStatus.NEW.name(),
                    null,
                    null,
                    null
            );
        })
                .limit(limitTasks)
                .forEach(recordToSave ->
                        dsl.insertInto(INT_COMM_QUERY)
                                .set(recordToSave)
                                .execute()
                );
        List<IntCommQuery> resultList = dbService.receiveNotificationTasks();
        Assertions.assertEquals(taskLimit, resultList.size());
    }

    @Test
    @Transactional
    public void receiveNotificationTasksDeactivateTest() {
        IntCommQueryRecord correctRecord = new IntCommQueryRecord(
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
                Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)),
                IntCommStatus.NEW.name(),
                null,
                null,
                null
        );
        dsl.insertInto(INT_COMM_QUERY)
                .set(correctRecord)
                .execute();
        dsl.insertInto(INT_DEACTIVATE_LIST)
                .set(INT_DEACTIVATE_LIST.CONTACT_ID, correctRecord.getContactId())
                .set(INT_DEACTIVATE_LIST.INT_UPDATE_DTTM, Timestamp.from(Instant.now().minus(waitBeforeSend + 1, MINUTES)))
                .set(INT_DEACTIVATE_LIST.INT_STATUS, IntDeactivateListStatus.NEW.name())
                .execute();
        List<IntCommQuery> resultList = dbService.receiveNotificationTasks();
        Assertions.assertEquals(0, resultList.size());
    }

    @Test
    @Transactional
    public void setStatusSentTest() {
        IntCommQueryRecord recordToSave = new IntCommQueryRecord(
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
        );

        dsl.insertInto(INT_COMM_QUERY)
                .set(recordToSave)
                .execute();

        dbService.setStatusSent(recordToSave.getContactId());

        List<IntCommQuery> resultList = dsl.selectFrom(INT_COMM_QUERY)
                .where(INT_COMM_QUERY.CONTACT_ID.eq(recordToSave.getContactId()))
                .fetchInto(IntCommQuery.class);

        Assertions.assertEquals(1, resultList.size());
        IntCommQuery result = resultList.get(0);

        Assertions.assertEquals(recordToSave.getContactId(), result.getContactId());
        Assertions.assertEquals(recordToSave.getCreCardId(), result.getCreCardId());
        Assertions.assertEquals(recordToSave.getCampaignCd(), result.getCampaignCd());
        Assertions.assertEquals(recordToSave.getDeepLink(), result.getDeepLink());
        Assertions.assertEquals(recordToSave.getEndDate(), result.getEndDate());
        Assertions.assertEquals(recordToSave.getEndTime(), result.getEndTime());
        Assertions.assertEquals(recordToSave.getStartDate(), result.getStartDate());
        Assertions.assertEquals(recordToSave.getStartTime(), result.getStartTime());
        Assertions.assertEquals(recordToSave.getIntCreateDttm(), result.getIntCreateDttm());
        Assertions.assertEquals(recordToSave.getIntQueryId(), result.getIntQueryId());
        Assertions.assertEquals(recordToSave.getOfferId(), result.getOfferId());
        Assertions.assertEquals(recordToSave.getMacrobrend(), result.getMacrobrend());
        Assertions.assertEquals(recordToSave.getMessageHeader(), result.getMessageHeader());
        Assertions.assertEquals(recordToSave.getMessageText(), result.getMessageText());
        Assertions.assertEquals(recordToSave.getMessageUrl(), result.getMessageUrl());
        Assertions.assertEquals(recordToSave.getMobilePhone(), result.getMobilePhone());
        Assertions.assertNotEquals(recordToSave.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(IntCommStatus.SENT.name(), result.getIntStatus());
        Assertions.assertEquals(0, result.getIntErrorCode());
        Assertions.assertNull(result.getIntErrorText());
    }

    @Test
    @Transactional
    public void setErrorStatusTest() {
        IntCommQueryRecord recordToSave = new IntCommQueryRecord(
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
        );

        dsl.insertInto(INT_COMM_QUERY)
                .set(recordToSave)
                .execute();

        NotificationResponse response = NotificationResponse.builder()
                .id(recordToSave.getContactId().toString())
                .error(ResponseError.INVALID_LOGIN)
                .mtNum(getLongIn(0L, 99999L).toString())
                .extendedDescription(getRandomString())
                .build();
        dbService.setErrorStatus(recordToSave.getContactId(), response);

        List<IntCommQuery> resultList = dsl.selectFrom(INT_COMM_QUERY)
                .where(INT_COMM_QUERY.CONTACT_ID.eq(recordToSave.getContactId()))
                .fetchInto(IntCommQuery.class);

        Assertions.assertEquals(1, resultList.size());
        IntCommQuery result = resultList.get(0);

        Assertions.assertEquals(recordToSave.getContactId(), result.getContactId());
        Assertions.assertEquals(recordToSave.getCreCardId(), result.getCreCardId());
        Assertions.assertEquals(recordToSave.getCampaignCd(), result.getCampaignCd());
        Assertions.assertEquals(recordToSave.getDeepLink(), result.getDeepLink());
        Assertions.assertEquals(recordToSave.getEndDate(), result.getEndDate());
        Assertions.assertEquals(recordToSave.getEndTime(), result.getEndTime());
        Assertions.assertEquals(recordToSave.getStartDate(), result.getStartDate());
        Assertions.assertEquals(recordToSave.getStartTime(), result.getStartTime());
        Assertions.assertEquals(recordToSave.getIntCreateDttm(), result.getIntCreateDttm());
        Assertions.assertEquals(recordToSave.getIntQueryId(), result.getIntQueryId());
        Assertions.assertEquals(recordToSave.getOfferId(), result.getOfferId());
        Assertions.assertEquals(recordToSave.getMacrobrend(), result.getMacrobrend());
        Assertions.assertEquals(recordToSave.getMessageHeader(), result.getMessageHeader());
        Assertions.assertEquals(recordToSave.getMessageText(), result.getMessageText());
        Assertions.assertEquals(recordToSave.getMessageUrl(), result.getMessageUrl());
        Assertions.assertEquals(recordToSave.getMobilePhone(), result.getMobilePhone());
        Assertions.assertNotEquals(recordToSave.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(IntCommStatus.ERROR.name(), result.getIntStatus());
        Assertions.assertEquals(response.getError().getCode(), result.getIntErrorCode());
        Assertions.assertEquals(response.getError().getDescription(), result.getIntErrorText());
    }

    @Test
    @Transactional
    public void saveNotificationStatusSMSTest() {
        IntCommResponse forSave = new IntCommResponse();
        forSave.setContactId(getLongIn(0L, 99999L));
        forSave.setIntStatus(IntCommStatus.NEW.name());
        forSave.setIntUpdateDttm(Timestamp.from(Instant.now()));
        forSave.setMessageType(MessageType.SMS.name());
        forSave.setResponseDttm(Timestamp.from(Instant.now().minus(10L, HOURS)));
        forSave.setResponseNm(NotificationResponseStatus.getByCode(2).name());
        forSave.setPartCount(getLongIn(0L, 10L).intValue());
        forSave.setCommCost(new BigDecimal("0.88").multiply(BigDecimal.valueOf(forSave.getPartCount())));
        forSave.setErrorCode(0);

        dbService.saveNotificationStatusSMS(forSave);

        List<IntCommResponse> responses = dsl.selectFrom(INT_COMM_RESPONSE)
                .where(INT_COMM_RESPONSE.CONTACT_ID.eq(forSave.getContactId()))
                .fetchInto(IntCommResponse.class);

        Assertions.assertEquals(1, responses.size());
        IntCommResponse result = responses.get(0);

        Assertions.assertEquals(forSave.getContactId(), result.getContactId());
        Assertions.assertEquals(forSave.getErrorCode(), result.getErrorCode());
        Assertions.assertEquals(forSave.getIntStatus(), result.getIntStatus());
        Assertions.assertEquals(forSave.getPartCount(), result.getPartCount());
        Assertions.assertEquals(forSave.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(forSave.getCommCost(), result.getCommCost());
        Assertions.assertEquals(forSave.getErrorText(), result.getErrorText());
        Assertions.assertEquals(forSave.getMessageType(), result.getMessageType());
        Assertions.assertEquals(forSave.getResponseDttm(), result.getResponseDttm());
        Assertions.assertEquals(forSave.getResponseNm(), result.getResponseNm());
    }

    @Test
    @Transactional
    public void saveNotificationStatusSMSErrorTest() {
        IntCommResponse forSave = new IntCommResponse();
        forSave.setContactId(getLongIn(0L, 99999L));
        forSave.setIntStatus(IntCommStatus.NEW.name());
        forSave.setIntUpdateDttm(Timestamp.from(Instant.now()));
        forSave.setMessageType(MessageType.SMS.name());
        forSave.setResponseDttm(Timestamp.from(Instant.now().minus(10L, HOURS)));
        forSave.setResponseNm(NotificationResponseStatus.getByCode(2).name());
        forSave.setPartCount(getLongIn(0L, 10L).intValue());
        forSave.setCommCost(new BigDecimal("0.88").multiply(BigDecimal.valueOf(forSave.getPartCount())));
        forSave.setErrorCode(8);
        forSave.setErrorText(NotificationResponseError.getDescriptionByCode(8));

        dbService.saveNotificationStatusSMSError(forSave);

        List<IntCommResponse> responses = dsl.selectFrom(INT_COMM_RESPONSE)
                .where(INT_COMM_RESPONSE.CONTACT_ID.eq(forSave.getContactId()))
                .fetchInto(IntCommResponse.class);

        Assertions.assertEquals(1, responses.size());
        IntCommResponse result = responses.get(0);

        Assertions.assertEquals(forSave.getContactId(), result.getContactId());
        Assertions.assertEquals(forSave.getErrorCode(), result.getErrorCode());
        Assertions.assertEquals(forSave.getIntStatus(), result.getIntStatus());
        Assertions.assertEquals(forSave.getPartCount(), result.getPartCount());
        Assertions.assertEquals(forSave.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(forSave.getCommCost(), result.getCommCost());
        Assertions.assertEquals(forSave.getErrorText(), result.getErrorText());
        Assertions.assertEquals(forSave.getMessageType(), result.getMessageType());
        Assertions.assertEquals(forSave.getResponseDttm(), result.getResponseDttm());
        Assertions.assertEquals(forSave.getResponseNm(), result.getResponseNm());
    }

    @Test
    @Transactional
    public void saveNotificationStatusPUSHTest() {
        IntCommResponse forSave = new IntCommResponse();
        forSave.setContactId(getLongIn(0L, 99999L));
        forSave.setIntStatus(IntCommStatus.NEW.name());
        forSave.setIntUpdateDttm(Timestamp.from(Instant.now()));
        forSave.setMessageType(MessageType.PUSH.name());
        forSave.setResponseDttm(Timestamp.from(Instant.now().minus(10L, HOURS)));
        forSave.setResponseNm(NotificationResponseStatus.getByCode(2).name());
        forSave.setCommCost(new BigDecimal("0.88"));
        forSave.setErrorCode(0);

        dbService.saveNotificationStatusPUSH(forSave);

        List<IntCommResponse> responses = dsl.selectFrom(INT_COMM_RESPONSE)
                .where(INT_COMM_RESPONSE.CONTACT_ID.eq(forSave.getContactId()))
                .fetchInto(IntCommResponse.class);

        Assertions.assertEquals(1, responses.size());
        IntCommResponse result = responses.get(0);

        Assertions.assertEquals(forSave.getContactId(), result.getContactId());
        Assertions.assertEquals(forSave.getErrorCode(), result.getErrorCode());
        Assertions.assertEquals(forSave.getIntStatus(), result.getIntStatus());
        Assertions.assertEquals(forSave.getPartCount(), result.getPartCount());
        Assertions.assertEquals(forSave.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(forSave.getCommCost(), result.getCommCost());
        Assertions.assertEquals(forSave.getErrorText(), result.getErrorText());
        Assertions.assertEquals(forSave.getMessageType(), result.getMessageType());
        Assertions.assertEquals(forSave.getResponseDttm(), result.getResponseDttm());
        Assertions.assertEquals(forSave.getResponseNm(), result.getResponseNm());
    }


    @Test
    @Transactional
    public void saveNotificationStatusPUSHOpenTest() {
        IntCommResponse forSave = new IntCommResponse();
        forSave.setContactId(getLongIn(0L, 99999L));
        forSave.setIntStatus(IntCommStatus.NEW.name());
        forSave.setIntUpdateDttm(Timestamp.from(Instant.now()));
        forSave.setMessageType(MessageType.SMS.name());
        forSave.setResponseDttm(Timestamp.from(Instant.now().minus(10L, HOURS)));
        forSave.setResponseNm(NotificationResponseStatus.OPEN.name());
        forSave.setCommCost(new BigDecimal("0.88"));
        forSave.setErrorCode(0);

        dbService.saveNotificationStatusPUSH(forSave);

        List<IntCommResponse> responses = dsl.selectFrom(INT_COMM_RESPONSE)
                .where(INT_COMM_RESPONSE.CONTACT_ID.eq(forSave.getContactId()))
                .fetchInto(IntCommResponse.class);

        Assertions.assertEquals(1, responses.size());
        IntCommResponse result = responses.get(0);

        Assertions.assertEquals(forSave.getContactId(), result.getContactId());
        Assertions.assertEquals(forSave.getErrorCode(), result.getErrorCode());
        Assertions.assertEquals(forSave.getIntStatus(), result.getIntStatus());
        Assertions.assertEquals(forSave.getPartCount(), result.getPartCount());
        Assertions.assertEquals(forSave.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(0, BigDecimal.ZERO.compareTo(result.getCommCost()));
        Assertions.assertEquals(forSave.getErrorText(), result.getErrorText());
        Assertions.assertEquals(forSave.getMessageType(), result.getMessageType());
        Assertions.assertEquals(forSave.getResponseDttm(), result.getResponseDttm());
        Assertions.assertEquals(forSave.getResponseNm(), result.getResponseNm());
    }

    @Test
    @Transactional
    public void saveNotificationStatusPUSHErrorTest() {
        IntCommResponse forSave = new IntCommResponse();
        forSave.setContactId(getLongIn(0L, 99999L));
        forSave.setIntStatus(IntCommStatus.NEW.name());
        forSave.setIntUpdateDttm(Timestamp.from(Instant.now()));
        forSave.setMessageType(MessageType.PUSH.name());
        forSave.setResponseDttm(Timestamp.from(Instant.now().minus(10L, HOURS)));
        forSave.setResponseNm(NotificationResponseStatus.getByCode(2).name());
        forSave.setCommCost(new BigDecimal("0.88"));
        forSave.setErrorCode(8);
        forSave.setErrorText(NotificationResponseError.getDescriptionByCode(8));

        dbService.saveNotificationStatusPUSHError(forSave);

        List<IntCommResponse> responses = dsl.selectFrom(INT_COMM_RESPONSE)
                .where(INT_COMM_RESPONSE.CONTACT_ID.eq(forSave.getContactId()))
                .fetchInto(IntCommResponse.class);

        Assertions.assertEquals(1, responses.size());
        IntCommResponse result = responses.get(0);

        Assertions.assertEquals(forSave.getContactId(), result.getContactId());
        Assertions.assertEquals(forSave.getErrorCode(), result.getErrorCode());
        Assertions.assertEquals(forSave.getIntStatus(), result.getIntStatus());
        Assertions.assertEquals(forSave.getIntUpdateDttm(), result.getIntUpdateDttm());
        Assertions.assertEquals(forSave.getCommCost(), result.getCommCost());
        Assertions.assertEquals(forSave.getErrorText(), result.getErrorText());
        Assertions.assertEquals(forSave.getMessageType(), result.getMessageType());
        Assertions.assertEquals(forSave.getResponseDttm(), result.getResponseDttm());
        Assertions.assertEquals(forSave.getResponseNm(), result.getResponseNm());
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
    }
}
