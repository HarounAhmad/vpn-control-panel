package io.erisdev.vpncontrolpanelbackend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_log")
@Builder
public class AuditLog {
    @Id
    @GeneratedValue
    private UUID id;

    private AuditLogAction action;

    private String entityType;
    private String entityId;
    private String details;
    private String performedBy;
    private Instant timestamp;

    @Lob
    private String oldValue;

    @Lob
    private String newValue;


}
