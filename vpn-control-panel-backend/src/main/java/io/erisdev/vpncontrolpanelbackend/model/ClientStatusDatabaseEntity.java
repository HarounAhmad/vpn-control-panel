package io.erisdev.vpncontrolpanelbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_status")
@Builder
@Data
public class ClientStatusDatabaseEntity {

    @Id
    private String cn;
    private String realIp;
    private String vpnIp;
    private long bytesIn;
    private long bytesOut;
    private long connectedSinceEpoch;
    private String connectedSince;
    private String status;

}
