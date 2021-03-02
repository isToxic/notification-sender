package org.zgr.notification.sender.model.send;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class PushData extends Data {
    private String title;
    private PushContent content;
}
