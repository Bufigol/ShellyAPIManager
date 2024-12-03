package com.bufigol.modelo.auxiliares;

import java.util.Objects;

public class CloudStatus {
    private boolean connected;

    public CloudStatus(boolean connected) {
        this.connected = connected;
    }

    public CloudStatus() {
        this.connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CloudStatus that = (CloudStatus) o;
        return isConnected() == that.isConnected();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isConnected());
    }

    @Override
    public String toString() {
        return "CloudStatus{" + "connected=" + connected +
                '}';
    }
}
