package org.zgr.notification.sender.model.send;

import lombok.Builder;

@Builder
@lombok.Data
public class Data {
    private String text;
    private InstantContent instantContent;
    private boolean flash;
    private String serviceNumber;
    private int ttl;
    private int smsPriority;
}
