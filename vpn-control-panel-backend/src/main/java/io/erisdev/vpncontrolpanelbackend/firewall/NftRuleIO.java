package io.erisdev.vpncontrolpanelbackend.firewall;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NftRuleIO {

    private static final Pattern RULE_PATTERN = Pattern.compile(
            "ip saddr (?<srcIp>\\S+) ip daddr (?<dstIp>\\S+) (?<proto>tcp|udp) dport (?<port>\\d+) accept\\s+# client-(?<cn>\\S+)"
    );

    public static List<NftRule> parseRules(Path path) {
        List<NftRule> rules = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                Matcher matcher = RULE_PATTERN.matcher(line);
                if (matcher.find()) {
                    rules.add(new NftRule(
                            matcher.group("cn"),
                            matcher.group("srcIp"),
                            matcher.group("dstIp"),
                            matcher.group("proto"),
                            Integer.parseInt(matcher.group("port"))
                    ));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return rules;
    }

    public static void writeRulesForClient(Path path, String cn, List<NftRule> newRules) {
        List<NftRule> existing = parseRules(path);

        List<NftRule> merged = new ArrayList<>();
        // keep all rules except for the client
        for (NftRule r : existing) {
            if (!r.getClientCn().equals(cn)) merged.add(r);
        }
        // add new client rules, avoid duplicates
        for (NftRule newRule : newRules) {
            if (!merged.contains(newRule)) merged.add(newRule);
        }

        writeRules(path, merged);
    }

    public static void writeRules(Path path, List<NftRule> rules) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            // sort rules for deterministic output (optional)
            rules.stream()
                    .sorted(Comparator.comparing(NftRule::getClientCn)
                            .thenComparing(NftRule::getSrcIp)
                            .thenComparing(NftRule::getDstIp)
                            .thenComparing(NftRule::getProtocol)
                            .thenComparingInt(NftRule::getDstPort))
                    .forEach(rule -> {
                        try {
                            writer.write(String.format(
                                    "ip saddr %s ip daddr %s %s dport %d accept # client-%s%n",
                                    rule.getSrcIp(),
                                    rule.getDstIp(),
                                    rule.getProtocol(),
                                    rule.getDstPort(),
                                    rule.getClientCn()
                            ));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void removeClientRule(Path path, String cn, String srcIp, String proto, int dstPort) {
        List<NftRule> existing = parseRules(path);

        List<NftRule> updated = new ArrayList<>();
        for (NftRule r : existing) {
            if (r.getClientCn().equals(cn)
                    && r.getSrcIp().equals(srcIp)
                    && r.getProtocol().equals(proto)
                    && r.getDstPort() == dstPort) {
                continue;
            }
            updated.add(r);
        }

        writeRules(path, updated);
    }

    public static void addClientRule(Path path, NftRule newRule) {
        List<NftRule> existing = parseRules(path);

        boolean exists = existing.stream().anyMatch(r ->
                r.getClientCn().equals(newRule.getClientCn()) &&
                        r.getSrcIp().equals(newRule.getSrcIp()) &&
                        r.getDstIp().equals(newRule.getDstIp()) &&
                        r.getProtocol().equals(newRule.getProtocol()) &&
                        r.getDstPort() == newRule.getDstPort()
        );
        if (exists) return; // avoid duplicate

        existing.add(newRule);

        writeRules(path, existing);
    }

    public static void reloadFirewall() {
        try {
            Process p = new ProcessBuilder("nft", "-f", "/etc/nftables.conf")
                    .inheritIO()
                    .start();
            if (p.waitFor() != 0) {
                throw new RuntimeException("nft reload failed with code " + p.exitValue());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to reload nftables", e);
        }
    }
}