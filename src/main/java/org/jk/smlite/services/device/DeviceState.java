package org.jk.smlite.services.device;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DeviceState {
    private final Duration timeout;
    private final DeviceType deviceType;
    private volatile boolean state;
    private volatile int value;
    private volatile Instant lastUpdated = Instant.EPOCH;

    DeviceState(DeviceType type, Duration timeout) {
        this.deviceType = type;
        this.timeout = timeout;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public boolean isEnabled() {
        return state;
    }

    public int getValue() {
        return value;
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
        String output = "DeviceState[type=" + getDeviceType()
                + ", connected= " + isConnected()
                + ", lastUpdated=" + (lu == Instant.EPOCH ? "never" : LocalDateTime.ofInstant(lu, ZoneId.systemDefault()).toString());
        if (deviceType.getDataType() == DataType.BOOLEAN)
            output = output + ", state= " + isEnabled() + "]";
        else
            output = output + ", state= " + getValue() + "]";
        return output;
    }

    boolean update(int state) {
        this.lastUpdated = Instant.now();
        if (deviceType.getDataType() == DataType.BOOLEAN) {
            if (this.state != (state == 1)) {
                this.state = (state == 1);
                return true;
            } else
                return false;
        } else {
            if (this.value != state) {
                this.value = state;
                return true;
            } else {
                return false;
            }
        }
    }
}
