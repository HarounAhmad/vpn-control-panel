package io.erisdev.vpncontrolpanelbackend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDto {
    private String action;
    private String entityType;
    private String summary;
    private String performedBy;
    private String timestamp;
    private Map<String, String> details;
}
