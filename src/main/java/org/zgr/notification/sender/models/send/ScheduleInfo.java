package org.zgr.notification.sender.models.send;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleInfo {
    private String timeBegin;
    private String timeEnd;
    private String weekdaysSchedule;
    private String deadline;
}
