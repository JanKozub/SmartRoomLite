package org.jk.smlite.model.device;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

public class DeviceState {
    private final Duration timeout;
    private final Device device;
    private volatile String[] data;
    private volatile Instant lastUpdated = Instant.EPOCH;

    public DeviceState(Device device, Duration timeout) {
        this.device = device;
        this.timeout = timeout;
    }

    public Device getDevice() {
        return device;
    }

    public String[] getData() {
        return data;
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
        return "DeviceState[type=" + getDevice().getDeviceType()
                + ", connected= " + isConnected()
                + ", lastUpdated=" + (lu == Instant.EPOCH ? "never" : LocalDateTime.ofInstant(lu, ZoneId.systemDefault()).toString())
                + ", data= " + Arrays.toString(getData());
    }

    public boolean update(String[] data) {
        this.lastUpdated = Instant.now();
        if (!Arrays.equals(this.data, data)) {
            this.data = data;
            return true;
        } else return false;
    }
}
