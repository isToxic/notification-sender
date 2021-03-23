CREATE SEQUENCE notification_sender."query_id_seq"
    INCREMENT 1
    MINVALUE 1
    START 1;

CREATE TABLE IF NOT EXISTS notification_sender."int_comm_query"
(
    CAMPAIGN_CD     character varying(20)       NOT NULL,
    CONTACT_ID      bigint                      NOT NULL,
    CRE_CARD_ID     bigint                      NOT NULL,
    OFFER_ID        int,
    START_DATE      date                        NOT NULL,
    END_DATE        date                        NOT NULL,
    START_TIME      time without time zone      NOT NULL,
    END_TIME        time without time zone      NOT NULL,
    MACROBREND      character varying(12)       NOT NULL,
    MOBILE_PHONE    character varying(20)       NOT NULL,
    MESSAGE_TEXT    character varying(250)      NOT NULL,
    MESSAGE_HEADER  character varying(100),
    DEEP_LINK       character varying(100),
    MESSAGE_URL     character varying(200),
    INT_QUERY_ID    bigint                      NOT NULL DEFAULT nextval('notification_sender."query_id_seq"'),
    INT_CREATE_DTTM timestamp without time zone NOT NULL,
    INT_UPDATE_DTTM timestamp without time zone NOT NULL,
    INT_STATUS      character varying(10)       NOT NULL,
    INT_ERROR_CODE  integer,
    INT_ERROR_TEXT  character varying(200),
    SAS_CHECK_FLG   integer,
    CONSTRAINT "Pk_notification_sender.int_comm_query" PRIMARY KEY (INT_QUERY_ID)
);

CREATE INDEX "ix_notification_sender.int_comm_query.CONTACT_ID"
    ON notification_sender."int_comm_query"
        USING btree (CONTACT_ID);
CREATE INDEX "ix_notification_sender.int_comm_query.START_DATE"
    ON notification_sender."int_comm_query"
        USING btree (START_DATE);
CREATE INDEX "ix_notification_sender.int_comm_query.END_DATE"
    ON notification_sender."int_comm_query"
        USING btree (END_DATE);
CREATE INDEX "ix_notification_sender.int_comm_query.INT_CREATE_DTTM"
    ON notification_sender."int_comm_query"
        USING btree (INT_CREATE_DTTM);
CREATE INDEX "ix_notification_sender.int_comm_query.INT_UPDATE_DTTM"
    ON notification_sender."int_comm_query"
        USING btree (INT_UPDATE_DTTM);
CREATE INDEX "ix_notification_sender.int_comm_query.INT_STATUS"
    ON notification_sender."int_comm_query"
        USING btree (INT_STATUS);