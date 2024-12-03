package com.bufigol.modelo.auxiliares;

import java.util.Objects;

public class StableVersion {
    private String version;

    public StableVersion(String version) {
        this.version = version;
    }

    public StableVersion() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StableVersion that = (StableVersion) o;
        return Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVersion());
    }

    @Override
    public String toString() {
        return "StableVersion{" + "version='" + version + '\'' +
                '}';
    }
}
