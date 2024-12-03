package com.bufigol.modelo.auxiliares;

import java.util.Objects;

public class AvailableUpdates {
    private StableVersion stable;

    public AvailableUpdates(StableVersion stable) {
        this.stable = stable;
    }

    public AvailableUpdates() {
        this.stable = new StableVersion();
    }

    public StableVersion getStable() {
        return stable;
    }

    public void setStable(StableVersion stable) {
        this.stable = stable;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AvailableUpdates that = (AvailableUpdates) o;
        return Objects.equals(getStable(), that.getStable());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getStable());
    }

    @Override
    public String toString() {
        return "AvailableUpdates{" + "stable=" + stable +
                '}';
    }
}
