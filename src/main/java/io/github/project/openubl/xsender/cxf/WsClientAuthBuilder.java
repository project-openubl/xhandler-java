package io.github.project.openubl.xsender.cxf;

public final class WsClientAuthBuilder {
    private String address;
    private String username;
    private String password;

    private WsClientAuthBuilder() {
    }

    public static WsClientAuthBuilder aWsClientAuth() {
        return new WsClientAuthBuilder();
    }

    public WsClientAuthBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public WsClientAuthBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public WsClientAuthBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public WsClientAuth build() {
        WsClientAuth wsClientAuth = new WsClientAuth();
        wsClientAuth.setAddress(address);
        wsClientAuth.setUsername(username);
        wsClientAuth.setPassword(password);
        return wsClientAuth;
    }
}
