/*
 * This file is generated by jOOQ.
 */
package com.zgr.notification.sender.db.jooq;


import org.jooq.Sequence;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;

import javax.annotation.Generated;


/**
 * Convenience access to all sequences in notification_sender.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.14.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>notification_sender.query_id_seq</code>
     */
    public static final Sequence<Long> QUERY_ID_SEQ = Internal.createSequence("query_id_seq", NotificationSender.NOTIFICATION_SENDER, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);
}