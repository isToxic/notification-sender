/*
 * This file is generated by jOOQ.
 */
package com.zgr.notification.sender.db.jooq.tables;


import com.zgr.notification.sender.db.jooq.NotificationSender;
import com.zgr.notification.sender.db.jooq.tables.records.IntDeactivateListRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.14.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IntDeactivateList extends TableImpl<IntDeactivateListRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>notification_sender.int_deactivate_list</code>
     */
    public static final IntDeactivateList INT_DEACTIVATE_LIST = new IntDeactivateList();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<IntDeactivateListRecord> getRecordType() {
        return IntDeactivateListRecord.class;
    }

    /**
     * The column <code>notification_sender.int_deactivate_list.contact_id</code>.
     */
    public final TableField<IntDeactivateListRecord, Long> CONTACT_ID = createField(DSL.name("contact_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>notification_sender.int_deactivate_list.int_update_dttm</code>.
     */
    public final TableField<IntDeactivateListRecord, Timestamp> INT_UPDATE_DTTM = createField(DSL.name("int_update_dttm"), SQLDataType.TIMESTAMP(6).nullable(false), this, "");

    /**
     * The column <code>notification_sender.int_deactivate_list.int_status</code>.
     */
    public final TableField<IntDeactivateListRecord, String> INT_STATUS = createField(DSL.name("int_status"), SQLDataType.VARCHAR(10).nullable(false), this, "");

    private IntDeactivateList(Name alias, Table<IntDeactivateListRecord> aliased) {
        this(alias, aliased, null);
    }

    private IntDeactivateList(Name alias, Table<IntDeactivateListRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>notification_sender.int_deactivate_list</code> table reference
     */
    public IntDeactivateList(String alias) {
        this(DSL.name(alias), INT_DEACTIVATE_LIST);
    }

    /**
     * Create an aliased <code>notification_sender.int_deactivate_list</code> table reference
     */
    public IntDeactivateList(Name alias) {
        this(alias, INT_DEACTIVATE_LIST);
    }

    /**
     * Create a <code>notification_sender.int_deactivate_list</code> table reference
     */
    public IntDeactivateList() {
        this(DSL.name("int_deactivate_list"), null);
    }

    public <O extends Record> IntDeactivateList(Table<O> child, ForeignKey<O, IntDeactivateListRecord> key) {
        super(child, key, INT_DEACTIVATE_LIST);
    }

    @Override
    public Schema getSchema() {
        return NotificationSender.NOTIFICATION_SENDER;
    }

    @Override
    public IntDeactivateList as(String alias) {
        return new IntDeactivateList(DSL.name(alias), this);
    }

    @Override
    public IntDeactivateList as(Name alias) {
        return new IntDeactivateList(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public IntDeactivateList rename(String name) {
        return new IntDeactivateList(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public IntDeactivateList rename(Name name) {
        return new IntDeactivateList(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, Timestamp, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
