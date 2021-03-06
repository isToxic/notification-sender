/*
 * This file is generated by jOOQ.
 */
package com.zgr.notification.sender.db.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class IntCommResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long       contactId;
    private String     responseNm;
    private String     messageType;
    private Integer    partCount;
    private BigDecimal commCost;
    private Integer    errorCode;
    private String     errorText;
    private Timestamp  responseDttm;
    private Timestamp  intUpdateDttm;
    private String     intStatus;

    public IntCommResponse() {}

    public IntCommResponse(IntCommResponse value) {
        this.contactId = value.contactId;
        this.responseNm = value.responseNm;
        this.messageType = value.messageType;
        this.partCount = value.partCount;
        this.commCost = value.commCost;
        this.errorCode = value.errorCode;
        this.errorText = value.errorText;
        this.responseDttm = value.responseDttm;
        this.intUpdateDttm = value.intUpdateDttm;
        this.intStatus = value.intStatus;
    }

    public IntCommResponse(
        Long       contactId,
        String     responseNm,
        String     messageType,
        Integer    partCount,
        BigDecimal commCost,
        Integer    errorCode,
        String     errorText,
        Timestamp  responseDttm,
        Timestamp  intUpdateDttm,
        String     intStatus
    ) {
        this.contactId = contactId;
        this.responseNm = responseNm;
        this.messageType = messageType;
        this.partCount = partCount;
        this.commCost = commCost;
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.responseDttm = responseDttm;
        this.intUpdateDttm = intUpdateDttm;
        this.intStatus = intStatus;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.contact_id</code>.
     */
    @NotNull
    public Long getContactId() {
        return this.contactId;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.contact_id</code>.
     */
    public IntCommResponse setContactId(Long contactId) {
        this.contactId = contactId;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.response_nm</code>.
     */
    @NotNull
    @Size(max = 20)
    public String getResponseNm() {
        return this.responseNm;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.response_nm</code>.
     */
    public IntCommResponse setResponseNm(String responseNm) {
        this.responseNm = responseNm;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.message_type</code>.
     */
    @NotNull
    @Size(max = 20)
    public String getMessageType() {
        return this.messageType;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.message_type</code>.
     */
    public IntCommResponse setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.part_count</code>.
     */
    public Integer getPartCount() {
        return this.partCount;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.part_count</code>.
     */
    public IntCommResponse setPartCount(Integer partCount) {
        this.partCount = partCount;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.comm_cost</code>.
     */
    @NotNull
    public BigDecimal getCommCost() {
        return this.commCost;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.comm_cost</code>.
     */
    public IntCommResponse setCommCost(BigDecimal commCost) {
        this.commCost = commCost;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.error_code</code>.
     */
    @NotNull
    public Integer getErrorCode() {
        return this.errorCode;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.error_code</code>.
     */
    public IntCommResponse setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.error_text</code>.
     */
    @Size(max = 200)
    public String getErrorText() {
        return this.errorText;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.error_text</code>.
     */
    public IntCommResponse setErrorText(String errorText) {
        this.errorText = errorText;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.response_dttm</code>.
     */
    @NotNull
    public Timestamp getResponseDttm() {
        return this.responseDttm;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.response_dttm</code>.
     */
    public IntCommResponse setResponseDttm(Timestamp responseDttm) {
        this.responseDttm = responseDttm;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.int_update_dttm</code>.
     */
    @NotNull
    public Timestamp getIntUpdateDttm() {
        return this.intUpdateDttm;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.int_update_dttm</code>.
     */
    public IntCommResponse setIntUpdateDttm(Timestamp intUpdateDttm) {
        this.intUpdateDttm = intUpdateDttm;
        return this;
    }

    /**
     * Getter for <code>notification_sender.int_comm_response.int_status</code>.
     */
    @NotNull
    @Size(max = 10)
    public String getIntStatus() {
        return this.intStatus;
    }

    /**
     * Setter for <code>notification_sender.int_comm_response.int_status</code>.
     */
    public IntCommResponse setIntStatus(String intStatus) {
        this.intStatus = intStatus;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IntCommResponse (");

        sb.append(contactId);
        sb.append(", ").append(responseNm);
        sb.append(", ").append(messageType);
        sb.append(", ").append(partCount);
        sb.append(", ").append(commCost);
        sb.append(", ").append(errorCode);
        sb.append(", ").append(errorText);
        sb.append(", ").append(responseDttm);
        sb.append(", ").append(intUpdateDttm);
        sb.append(", ").append(intStatus);

        sb.append(")");
        return sb.toString();
    }
}
