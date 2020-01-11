package org.jk.smlite.model.device;

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

    public boolean isConnected() {
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
        if (DeviceType.isDeviceToggle(getDevice().getDeviceType()))
            output = output + ", state= " + isEnabled() + "]";
        else
            output = output + ", state= " + getValue() + "]";
        return output;
    }

    public boolean update(char[] data) {
        this.lastUpdated = Instant.now();
        if (DeviceType.isDeviceToggle(getDevice().getDeviceType())) {
            if (this.state != (data[0] == '1')) {
                this.state = (data[0] == '1');
                return true;
            } else
                return false;
        } else {
            if ((char) this.value != data[0]) {
                this.value = (int) data[0] - 48;
                return true;
            } else {
                return false;
            }
        }
    }
}
