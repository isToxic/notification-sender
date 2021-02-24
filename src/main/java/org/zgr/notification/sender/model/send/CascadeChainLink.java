package org.zgr.notification.sender.model.send;

import lombok.Builder;
import lombok.Data;
import org.zgr.notification.sender.enums.RepeatSendState;

@Data
@Builder
public class CascadeChainLink {
    private RepeatSendState state;
    private Message message;
    private CascadeChainLink nextLink;
}
