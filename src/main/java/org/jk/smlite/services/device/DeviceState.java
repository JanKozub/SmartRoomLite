package org.jk.smlite.services.device;

import java.time.Duration;
import java.time.Instant;

public class DeviceState {
    private final Duration timeout;
    private final DeviceType type;
    private volatile boolean state;
    private volatile Instant lastUpdated = Instant.EPOCH;

    DeviceState(DeviceType type, Duration timeout) {
        this.type = type;
        this.timeout = timeout;
    }

    public DeviceType getType() {
        return type;
    }

    public boolean isEnabled() {
        return state;
    }

    public boolean isConnected() {
        return Duration.between(lastUpdated, Instant.now())
                .compareTo(timeout) < 0;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        Instant lu = getLastUpdated();
        return "DeviceState[type=" + getType() + ", lastUpdated=" + (lu == Instant.EPOCH ? "never" : lu.toString()) + ", state=" + isEnabled() + "]";
    }

    void update() {
        this.lastUpdated = Instant.now();
    }

    boolean update(boolean state) {
        this.lastUpdated = Instant.now();
        if (this.state != state) {
            this.state = state;
            return true;
        } else
            return false;
    }
}
