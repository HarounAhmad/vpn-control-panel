package io.erisdev.vpncontrolpanelbackend.service;

import io.erisdev.vpncontrolpanelbackend.firewall.NftRule;
import io.erisdev.vpncontrolpanelbackend.firewall.NftRuleIO;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class VpnClientFirewallService {

    private static final Path RULE_FILE = Path.of("/home/haroun/nftables/per-client-rules.nft");

    public List<NftRule> listClientRules(String clientCn) {
        return NftRuleIO.parseRules(RULE_FILE).stream()
                .filter(r -> r.getClientCn().equals(clientCn))
                .toList();
    }

    public void addClientRule(String clientCn, String srcIp, String dstIp, String protocol, int dstPort) {
        NftRule newRule = new NftRule(clientCn, srcIp, dstIp, protocol, dstPort);
        NftRuleIO.addClientRule(RULE_FILE, newRule);
        NftRuleIO.reloadFirewall();
    }

    public void removeClientRule(String clientCn, String srcIp, String protocol, int dstPort) {
        NftRuleIO.removeClientRule(RULE_FILE, clientCn, srcIp, protocol, dstPort);
        NftRuleIO.reloadFirewall();
    }

    public void replaceClientRules(String clientCn, List<NftRule> newRules) {
        NftRuleIO.writeRulesForClient(RULE_FILE, clientCn, newRules);
        NftRuleIO.reloadFirewall();
    }
}