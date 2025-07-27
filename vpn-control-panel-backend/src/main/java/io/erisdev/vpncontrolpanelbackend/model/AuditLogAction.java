package io.erisdev.vpncontrolpanelbackend.model;

public enum AuditLogAction {
    CREATE,
    UPDATE,
    DELETE,
    REVOKE, LOGIN_SUCCESS, LOGIN_FAILURE, ADD_CLIENT_RULE, REMOVE_CLIENT_RULE, CREATE_CLIENT, DOWNLOAD
}
