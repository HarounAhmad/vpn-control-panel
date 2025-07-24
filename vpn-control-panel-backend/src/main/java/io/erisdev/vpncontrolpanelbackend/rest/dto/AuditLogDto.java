package io.erisdev.vpncontrolpanelbackend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDto {
    private String id;
    private String action;
    private String entityType;
    private String entityId;
    private String details;
    private String performedBy;
    private String timestamp;
    private String oldValue;
    private String newValue;
}
