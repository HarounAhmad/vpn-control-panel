package io.erisdev.vpncontrolpanelbackend.certificate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public final class CertdResponse {
    @JsonProperty("err") public String err;

    @JsonProperty("serial")    public String serial;
    @JsonProperty("not_after") public String notAfter;

    @JsonProperty("cert_pem")          public String certPem;
    @JsonProperty("key_pem_encrypted") public String keyPemEncrypted;

    @JsonProperty("crl_pem") public String crlPem;

    @JsonProperty("zip_b64") public String zipB64;

    @JsonProperty("issued") public List<Issued> issued;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Issued {
        @JsonProperty("serial")    public String serial;
        @JsonProperty("cn")        public String cn;
        @JsonProperty("profile")   public String profile;
        @JsonProperty("not_after") public String notAfter;
        @JsonProperty("sha256")    public String sha256;
    }

    public boolean ok() { return err == null || err.isBlank(); }
}
