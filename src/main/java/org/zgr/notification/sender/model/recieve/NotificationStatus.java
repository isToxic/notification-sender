package org.zgr.notification.sender.model.recieve;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationStatus {
    private String id;
    private String mtNum;
    private int status;
    private String type;
    private String doneDate;
    private String submitDate;
    private String destAddr;
    private String sourceAddr;
    private String text;
    private String partCount;
    private String errorCode;
    private String mccMnc;
    private String trafficType;
    private String segmentPrice;
}
