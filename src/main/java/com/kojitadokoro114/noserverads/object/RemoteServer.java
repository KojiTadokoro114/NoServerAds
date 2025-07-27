package com.kojitadokoro114.noserverads.object;

public class RemoteServer {

    public final String host;
    public final int port;

    public RemoteServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static RemoteServer parse(String line) {
        String[] parts = line.split(":");
        return new RemoteServer(parts[0], Integer.parseInt(parts[1]));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof RemoteServer)) return false;
        RemoteServer other = (RemoteServer) obj;
        return host.equals(other.host) && port == other.port;
    }

    @Override
    public int hashCode() {
        return host.hashCode() + port;
    }
}
