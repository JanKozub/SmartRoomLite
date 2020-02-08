package org.jk.smlite.services.device;

import org.jk.smlite.model.device.DeviceState;
import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;

public class NightModeHandler {

    private static final Logger log = LoggerFactory.getLogger(NightModeHandler.class);

    private final Configuration configuration;
    private final DeviceManager deviceManager;
    private final DeviceCommander deviceCommander;

    NightModeHandler(Configuration configuration, DeviceManager deviceManager, DeviceCommander deviceCommander) {
        this.configuration = configuration;
        this.deviceManager = deviceManager;
        this.deviceCommander = deviceCommander;
    }

    public boolean toggleNightMode() {
        log.info("TOGGLED NIGHT MODE");
        boolean setNightMode = !getNightModeState();
        configuration.setProperty("nightMode.toggled", Boolean.toString(setNightMode));

        if (isSet("blinds.morning_toggle"))
            deviceCommander.setBlind(DeviceType.BLIND1, getBlindMode(setNightMode));

        if (isSet("clock.morning_toggle"))
            if (isNotInCorrectMode(setNightMode, DeviceType.CLOCK)) deviceCommander.toggleDevice(DeviceType.CLOCK);

        if (isSet("door.lock_on_nightMode"))
            if (isNotInCorrectMode(setNightMode, DeviceType.DOOR)) deviceCommander.toggleDevice(DeviceType.DOOR);

        if (isSet("thermometer.morning_screen_toggle"))
            if (isNotInCorrectMode(setNightMode, DeviceType.THERMOMETER))
                deviceCommander.toggleDevice(DeviceType.THERMOMETER);

        if (isSet("door.morning_screen_toggle"))
            if (isNotInCorrectMode(setNightMode, DeviceType.DOOR)) deviceCommander.toggleDoorScreen();

        return setNightMode;
    }

    public long getNightModeToggleDuration() {
        LocalTime time = LocalTime.now();
        LocalTime clockToggleTime = LocalTime.parse(configuration.readProperty("nightMode.toggle_hour"));
        long duration = Duration.between(clockToggleTime, time).toMillis();
        return Math.abs(duration);
    }

    public boolean getNightModeState() {
        return isSet("nightMode.toggled");
    }

    private boolean isDeviceEnabled(DeviceType deviceType, int index) {
        DeviceState deviceState = deviceManager.getDeviceState(deviceType);
        if (deviceState != null) {
            return deviceState.getData(index).equals("1");
        } else return false;
    }

    private String getBlindMode(boolean setNightMode) {
        return setNightMode ? "1" : "5";
    }

    private boolean isNotInCorrectMode(boolean setNightMode, DeviceType deviceType) {
        return setNightMode == isDeviceEnabled(deviceType, deviceType == DeviceType.DOOR ? 1 : 0);
    }

    private boolean isSet(String s) {
        return configuration.readProperty(s).equals("true");
    }
}
