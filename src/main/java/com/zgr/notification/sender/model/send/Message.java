package com.zgr.notification.sender.model.send;

import com.zgr.notification.sender.enums.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private MessageType type;
    private com.zgr.notification.sender.model.send.Data data;
}
