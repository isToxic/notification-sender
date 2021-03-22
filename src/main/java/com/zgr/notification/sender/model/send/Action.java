package com.zgr.notification.sender.model.send;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {
    private String title;
    private String action;
    private String options;
}
