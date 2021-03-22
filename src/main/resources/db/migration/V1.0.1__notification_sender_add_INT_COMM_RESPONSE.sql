CREATE TABLE IF NOT EXISTS notification_sender."int_comm_response"
(
    CONTACT_ID      bigint                      NOT NULL,
    RESPONSE_NM     character varying(20)       NOT NULL,
    MESSAGE_TYPE    character varying(20)       NOT NULL,
    PART_COUNT      integer,
    COMM_COST       decimal(4, 2)               NOT NULL,
    ERROR_CODE      integer                     NOT NULL,
    ERROR_TEXT      character varying(200),
    RESPONSE_DTTM   timestamp without time zone NOT NULL,
    INT_UPDATE_DTTM timestamp without time zone NOT NULL,
    INT_STATUS      character varying(10)       NOT NULL
);

CREATE INDEX "ix_notification_sender.int_comm_response.CONTACT_ID"
    ON notification_sender."int_comm_response"
        USING btree (CONTACT_ID);
CREATE INDEX "ix_notification_sender.int_comm_response.INT_STATUS"
    ON notification_sender."int_comm_response"
        USING btree (INT_STATUS);
CREATE INDEX "ix_notification_sender.int_comm_response.ERROR_CODE"
    ON notification_sender."int_comm_response"
        USING btree (ERROR_CODE);