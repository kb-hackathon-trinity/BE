package org.trinity.be.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Integer notificationNo;
    private String id;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}
