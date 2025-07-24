package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.config.FirewallProperties;
import io.erisdev.vpncontrolpanelbackend.firewall.NftRule;
import io.erisdev.vpncontrolpanelbackend.firewall.NftRuleIO;
import io.erisdev.vpncontrolpanelbackend.model.AuditLog;
import io.erisdev.vpncontrolpanelbackend.model.AuditLogAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VpnClientFirewallService {

    private final NftRuleIO nftRuleIO;
    private final FirewallProperties firewallProperties;
    private final AuditLogService auditLogService;

    public List<NftRule> listClientRules(String clientCn) {
        return nftRuleIO.parseRules(Path.of(firewallProperties.getClientRulesFile())).stream()
                .filter(r -> r.getClientCn().equals(clientCn))
                .toList();
    }

    public NftRule addClientRule(String clientCn, String srcIp, String dstIp, String protocol, int dstPort) {
        NftRule newRule = new NftRule(clientCn, srcIp, dstIp, protocol, dstPort);
        nftRuleIO.addClientRule(Path.of(firewallProperties.getClientRulesFile()), newRule);
        nftRuleIO.reloadFirewall();
        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.CREATE)
                        .details("Added firewall rule for client: " + clientCn)
                        .entityType(NftRule.class.getSimpleName())
                        .entityId("n/a - firewall rules are not stored in the database")
                        .performedBy("system") // Assuming this is an internal operation
                        .newValue(newRule.toString())
                        .timestamp(java.time.Instant.now())
                        .oldValue(null)
                        .build()
        );
        return newRule;
    }

    public void removeClientRule(String clientCn, String srcIp, String protocol, int dstPort) {
        nftRuleIO.removeClientRule(Path.of(firewallProperties.getClientRulesFile()), clientCn, srcIp, protocol, dstPort);
        nftRuleIO.reloadFirewall();
        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.DELETE)
                        .details("Removed firewall rule for client: " + clientCn)
                        .entityType(NftRule.class.getSimpleName())
                        .entityId("n/a - firewall rules are not stored in the database")
                        .performedBy("system") // Assuming this is an internal operation
                        .newValue(null)
                        .timestamp(java.time.Instant.now())
                        .oldValue("srcIp: " + srcIp + ", protocol: " + protocol + ", dstPort: " + dstPort)
                        .build()
        );
    }

    public void replaceClientRules(String clientCn, List<NftRule> newRules) {
        nftRuleIO.writeRulesForClient(Path.of(firewallProperties.getClientRulesFile()), clientCn, newRules);
        nftRuleIO.reloadFirewall();
        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.UPDATE)
                        .details("Replaced firewall rules for client: " + clientCn)
                        .entityType(NftRule.class.getSimpleName())
                        .entityId("n/a - firewall rules are not stored in the database")
                        .performedBy("system") // Assuming this is an internal operation
                        .newValue(newRules.toString())
                        .timestamp(java.time.Instant.now())
                        .oldValue("Previous rules replaced")
                        .build()
        );
    }
}