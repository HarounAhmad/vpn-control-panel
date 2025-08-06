package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.config.FirewallProperties;
import io.erisdev.vpncontrolpanelbackend.firewall.NftRule;
import io.erisdev.vpncontrolpanelbackend.firewall.NftRuleIO;
import io.erisdev.vpncontrolpanelbackend.model.AuditLog;
import io.erisdev.vpncontrolpanelbackend.model.AuditLogAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VpnClientFirewallService {

    private final NftRuleIO nftRuleIO;
    private final FirewallProperties firewallProperties;
    private final AuditLogService auditLogService;
    private final AuditContext auditContext;

    public List<NftRule> listClientRules(String clientCn) {
        return nftRuleIO.parseRules(Path.of(firewallProperties.getClientRulesFile())).stream()
                .filter(r -> r.getClientCn().equals(clientCn))
                .toList();
    }

    public NftRule addClientRule(String clientCn, String srcIp, String dstIp, String protocol, int dstPort, String username) {
        NftRule newRule = new NftRule(clientCn, srcIp, dstIp, protocol, dstPort, "");
        nftRuleIO.addClientRule(Path.of(firewallProperties.getClientRulesFile()), newRule);
        String reloadResponse = nftRuleIO.reloadFirewall();
        newRule.setResponse(reloadResponse);
        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.ADD_CLIENT_RULE)
                        .performedBy(username)
                        .entityType(NftRule.class.getSimpleName())
                        .summary("Added firewall rule for client: " + clientCn)
                        .timestamp(Instant.now())
                        .details(auditContext.clientRule(
                                clientCn,
                                srcIp,
                                dstIp,
                                protocol,
                                dstPort
                        ))
                        .build()

        );
        return newRule;
    }

    public void removeClientRule(String clientCn, String srcIp, String protocol, int dstPort, String username) {
        nftRuleIO.removeClientRule(Path.of(firewallProperties.getClientRulesFile()), clientCn, srcIp, protocol, dstPort);
        nftRuleIO.reloadFirewall();
        auditLogService.LogAction(
                AuditLog.builder()
                        .action(AuditLogAction.REMOVE_CLIENT_RULE)
                        .performedBy(username)
                        .entityType(NftRule.class.getSimpleName())
                        .summary("Removed firewall rule for client: " + clientCn)
                        .timestamp(Instant.now())
                        .details(auditContext.clientRule(
                                clientCn,
                                srcIp,
                                null,
                                protocol,
                                dstPort
                        ))
                        .build()

        );
    }

    public void replaceClientRules(String clientCn, List<NftRule> newRules) {
        nftRuleIO.writeRulesForClient(Path.of(firewallProperties.getClientRulesFile()), clientCn, newRules);
        nftRuleIO.reloadFirewall();
        auditLogService.LogAction(
                AuditLog.builder().build()

        );
    }
}