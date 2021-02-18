package org.zgr.notification.sender.models.recieve;

import lombok.Builder;
import lombok.Data;
import org.zgr.notification.sender.enums.ResponseError;

@Data
@Builder
public class NotificationResponse {
    private String mtNum;
    private String id;
    private ResponseError error;
    private String extendedDescription;
}
