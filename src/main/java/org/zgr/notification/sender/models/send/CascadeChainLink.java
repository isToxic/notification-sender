package org.zgr.notification.sender.models.send;

import lombok.Builder;
import lombok.Data;
import org.zgr.notification.sender.enums.RepeatsendState;

@Data
@Builder
public class CascadeChainLink {
    private RepeatsendState state;
    private Message message;
    private CascadeChainLink nextLink;
}
