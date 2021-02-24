package org.zgr.notification.sender.model.send;

import lombok.Builder;
import lombok.Data;
import org.zgr.notification.sender.enums.MessageType;

@Data
@Builder
public class Message {
    private MessageType type;
    private org.zgr.notification.sender.model.send.Data data;
}
