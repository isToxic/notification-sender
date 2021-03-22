package com.zgr.notification.sender.model.send;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomPayload {
    private String deeplink;
}
