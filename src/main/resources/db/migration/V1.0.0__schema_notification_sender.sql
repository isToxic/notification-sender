CREATE SCHEMA IF NOT EXISTS notification_sender;

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

CREATE TABLE IF NOT EXISTS notification_sender."int_deactivate_list"
(
    CONTACT_ID      bigint                      NOT NULL,
    INT_UPDATE_DTTM timestamp without time zone NOT NULL,
    INT_STATUS      character varying(10)       NOT NULL
);