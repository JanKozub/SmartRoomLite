package org.jk.smlite.services.device;

import org.jk.smlite.model.DataType;
import org.jk.smlite.model.Device;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DeviceState {
    private final Duration timeout;
    private final Device device;
    private volatile boolean state;
    private volatile int value;
    private volatile Instant lastUpdated = Instant.EPOCH;

    public DeviceState(Device device, Duration timeout) {
        this.device = device;
        this.timeout = timeout;
    }

    public Device getDevice() {
        return device;
    }

    public boolean isEnabled() {
        return state;
    }

    public int getValue() {
        return value;
    }

    private boolean isConnected() {
        return Duration.between(lastUpdated, Instant.now()).compareTo(timeout) < 0;
    }

    private Instant getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        Instant lu = getLastUpdated();
        String output = "DeviceState[type=" + getDevice().getDeviceType()
                + ", connected= " + isConnected()
                + ", lastUpdated=" + (lu == Instant.EPOCH ? "never" : LocalDateTime.ofInstant(lu, ZoneId.systemDefault()).toString());
        if (getDevice().getDeviceType().getDataType() == DataType.BOOLEAN)
            output = output + ", state= " + isEnabled() + "]";
        else
            output = output + ", state= " + getValue() + "]";
        return output;
    }

    boolean update(int state) {
        this.lastUpdated = Instant.now();
        if (getDevice().getDeviceType().getDataType() == DataType.BOOLEAN) {
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
