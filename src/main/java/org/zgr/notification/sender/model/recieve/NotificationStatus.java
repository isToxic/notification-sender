package org.zgr.notification.sender.model.recieve;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class NotificationStatus {
    private String id;
    private String mtNum;
    private int status;
    private String type;
    private Timestamp doneDate;
    private Timestamp submitDate;
    private String destAddr;
    private String sourceAddr;
    private String text;
    private String partCount;
    private String errorCode;
    private String mccMnc;
    private int trafficType;
    private String segmentPrice;
}
