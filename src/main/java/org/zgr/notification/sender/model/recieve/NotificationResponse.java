package org.zgr.notification.sender.model.recieve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zgr.notification.sender.enums.ResponseError;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private String mtNum;
    private String id;
    private ResponseError error;
    private String extendedDescription;

    public boolean hasBody(){
        return this.getError() == null;
    }
}
