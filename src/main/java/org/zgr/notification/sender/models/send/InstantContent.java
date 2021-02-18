package org.zgr.notification.sender.models.send;

import lombok.Builder;
import lombok.Data;
import org.zgr.notification.sender.enums.ContentType;

@Data
@Builder
public class InstantContent {
    private ContentType type;
    private InstantData data;
}
