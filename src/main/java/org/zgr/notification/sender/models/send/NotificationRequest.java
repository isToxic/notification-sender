package org.zgr.notification.sender.models.send;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
    private String login;
    private String password;
    private String extraParam;
    private boolean useTimeDiff;
    private String id;
    private boolean shortenLinks;
    private int registeredDelivery;
    private ScheduleInfo scheduleInfo;
    private String destAddr;
    private Message message;
    private CascadeChainLink cascadeChainLink;
}


