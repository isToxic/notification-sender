package org.zgr.notification.sender.model.send;

import lombok.experimental.SuperBuilder;

@SuperBuilder
@lombok.Data
public class Data {
    private String text;
    private String serviceNumber;
    private int ttl;
    private String ttlUnit;
}
