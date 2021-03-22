CREATE TABLE IF NOT EXISTS notification_sender."int_deactivate_list"
(
    CONTACT_ID      bigint                      NOT NULL,
    INT_UPDATE_DTTM timestamp without time zone NOT NULL,
    INT_STATUS      character varying(10)       NOT NULL
);

CREATE INDEX "ix_notification_sender.int_deactivate_list.CONTACT_ID"
    ON notification_sender."int_deactivate_list"
        USING btree (CONTACT_ID);
CREATE INDEX "ix_notification_sender.int_deactivate_list.INT_STATUS"
    ON notification_sender."int_deactivate_list"
        USING btree (INT_STATUS);