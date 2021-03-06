package org.jk.smlite.model.device;

import java.time.Duration;

public class Device {
    private final DeviceType deviceType;
    private final DeviceState deviceState;

    public Device(DeviceType deviceType) {
        this.deviceType = deviceType;
        this.deviceState = new DeviceState(this, Duration.ofSeconds(5));
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public DeviceState getDeviceState() {
        return deviceState;
    }

    @Override
    public String toString() {
        return deviceState.toString();
    }
}
