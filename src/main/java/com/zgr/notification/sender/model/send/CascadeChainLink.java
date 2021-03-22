package com.zgr.notification.sender.model.send;

import com.zgr.notification.sender.enums.RepeatSendState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CascadeChainLink {
    private RepeatSendState state;
    private Message message;
    private CascadeChainLink nextLink;
}
