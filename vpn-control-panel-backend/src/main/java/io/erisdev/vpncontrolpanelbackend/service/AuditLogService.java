package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.model.AuditLog;
import io.erisdev.vpncontrolpanelbackend.repository.AuditLogRepository;
import io.erisdev.vpncontrolpanelbackend.rest.dto.AuditLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return AuditLogDto.builder().build();

    }
}
