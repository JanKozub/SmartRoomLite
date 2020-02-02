package org.jk.smlite.services.handlers;

import org.jk.smlite.model.device.DeviceState;
import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.Configuration;
import org.jk.smlite.services.device.DeviceCommander;
import org.jk.smlite.services.device.DeviceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class NightModeHandler {

    private static final Logger log = LoggerFactory.getLogger(NightModeHandler.class);

    private final Configuration configuration;
    private final DeviceManager deviceManager;
    private final DeviceCommander deviceCommander;

    public NightModeHandler(Configuration configuration, DeviceManager deviceManager, DeviceCommander deviceCommander) {
        this.configuration = configuration;
        this.deviceManager = deviceManager;
        this.deviceCommander = deviceCommander;
    }

    public boolean toggleNightMode() {
        log.info("TOGGLED NIGHT MODE");
        boolean currentState = getNightModeState();
        executeNightMode(currentState);
        return currentState;
    }

    public long getNightModeToggleDuration() {
        LocalTime time = LocalTime.now();
        LocalTime clockToggleTime = LocalTime.parse(configuration.readProperty("nightMode.toggle_hour"));
        long duration = Duration.between(clockToggleTime, time).toMillis();
        return Math.abs(duration);
    }

    public boolean getNightModeState() {
        return configuration.readProperty("nightMode.toggled").equals("true");
    }


    public void executeNightMode(boolean currentState) {
        configuration.setProperty("nightMode.toggled", String.valueOf(currentState));
        if (currentState) {
            if (configuration.readProperty("blinds.morning_toggle").equals("true"))
                deviceCommander.setBlind(DeviceType.BLIND1, "1");
            if (configuration.readProperty("clock.morning_toggle").equals("true"))
                if (isDeviceEnabled(DeviceType.CLOCK)) deviceCommander.toggleDevice(DeviceType.CLOCK);
            if (configuration.readProperty("door.lock_on_nightMode").equals("true"))
                if (isDeviceEnabled(DeviceType.DOOR)) deviceCommander.toggleDevice(DeviceType.DOOR);
            if (configuration.readProperty("door.morning_screen_toggle").equals("true"))
                if (Objects.requireNonNull(deviceManager.getDeviceState(DeviceType.DOOR)).getData(1).equals("1"))
                    deviceCommander.toggleDoorScreen();
        } else {
            if (configuration.readProperty("blinds.morning_toggle").equals("true"))
                deviceCommander.setBlind(DeviceType.BLIND1, "5");
            if (configuration.readProperty("clock.morning_toggle").equals("true"))
                if (!isDeviceEnabled(DeviceType.CLOCK)) deviceCommander.toggleDevice(DeviceType.CLOCK);
            if (configuration.readProperty("door.lock_on_nightMode").equals("true"))
                if (!isDeviceEnabled(DeviceType.DOOR)) deviceCommander.toggleDevice(DeviceType.DOOR);
            if (configuration.readProperty("door.morning_screen_toggle").equals("true"))
                if (Objects.requireNonNull(deviceManager.getDeviceState(DeviceType.DOOR)).getData(1).equals("0"))
                    deviceCommander.toggleDoorScreen();
        }
    }

    private boolean isDeviceEnabled(DeviceType deviceType) {
        DeviceState deviceState = deviceManager.getDeviceState(deviceType);
        if (deviceState != null) {
            return deviceState.getData(0).equals("1");
        } else return false;
    }
}
