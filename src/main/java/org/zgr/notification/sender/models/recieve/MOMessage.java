package org.zgr.notification.sender.models.recieve;

import lombok.Data;

@Data
public class MOMessage {
    private String transactionId;
    private String destAddr;
    private String sourceAddr;
    private String type;
    private String message;
    private int partCount;
    private String receivedDate;
}
