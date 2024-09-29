package model;

import java.io.Serializable;

public class Client implements Serializable {
    String host;
    int port;
    String userHandle;

    public Client() {
    }

    public Client(String host, int port, String userHandle) {
        this.host = host;
        this.port = port;
        this.userHandle = userHandle;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public static final class Builder {
        private final Client client;

        public Builder() {
            client = new Client();
        }

        public static Client.Builder aClient() {
            return new Client.Builder();
        }

        public Client.Builder withHost(String host) {
            client.setHost(host);
            return this;
        }

        public Client.Builder withPort(int port) {
            client.setPort(port);
            return this;
        }

        public Client.Builder withUserHandle(String userHandle) {
            client.setUserHandle(userHandle);
            return this;
        }

        public Client build() {
            return client;
        }
    }
}
