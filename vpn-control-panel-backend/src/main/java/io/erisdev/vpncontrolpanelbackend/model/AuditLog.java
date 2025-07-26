package io.erisdev.vpncontrolpanelbackend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
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
    private String summary;
    private String performedBy;
    private Instant timestamp;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "audit_log_details", joinColumns = @JoinColumn(name = "audit_log_id"))
    @MapKeyColumn(name = "detail_key")
    @Column(name = "detail_value")
    private Map<String, String> details;
}
