package io.erisdev.vpncontrolpanelbackend.certificate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CertdRequest {
    @JsonProperty("op")        public String op;
    @JsonProperty("cn")        public String cn;
    @JsonProperty("profile")   public String profile;
    @JsonProperty("key_type")  public String keyType;
    @JsonProperty("passphrase") public String passphrase;
    @JsonProperty("csr_pem")   public String csrPem;
    @JsonProperty("serial")    public String serial;
    @JsonProperty("reason")    public String reason;

    @JsonProperty("bundle_cn")          public String bundleCn;
    @JsonProperty("bundle_remote")      public String bundleRemote;
    @JsonProperty("bundle_port")        public Integer bundlePort;
    @JsonProperty("bundle_proto")       public String bundleProto;
    @JsonProperty("bundle_include_key") public Boolean bundleIncludeKey;

    public static CertdRequest op(CertdOp op) { var r=new CertdRequest(); r.op=op.name(); return r; }
}
