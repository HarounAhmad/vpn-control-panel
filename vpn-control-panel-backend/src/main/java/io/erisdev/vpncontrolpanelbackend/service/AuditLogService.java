package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.model.AuditLog;
import io.erisdev.vpncontrolpanelbackend.repository.AuditLogRepository;
import io.erisdev.vpncontrolpanelbackend.rest.dto.AuditLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public List<AuditLogDto> getAuditLogs() {
        return auditLogRepository.findAll().stream().map(
                this::mapToDto
        ).toList();
    }


    public void LogAction(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }


    private AuditLogDto mapToDto(AuditLog e) {
        return AuditLogDto.builder()
                .action(e.getAction().name())
                .entityType(e.getEntityType())
                .summary(e.getSummary())
                .performedBy(e.getPerformedBy())
                .timestamp(e.getTimestamp().toString())
                .details(e.getDetails() != null ? e.getDetails() : Map.of())
                .build();

    }
}
