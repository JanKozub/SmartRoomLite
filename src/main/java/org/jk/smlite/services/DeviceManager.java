package org.jk.smlite.services;

import org.jk.smlite.exceptions.DeviceNotFoundException;
import org.jk.smlite.model.Message;
import org.jk.smlite.model.device.Device;
import org.jk.smlite.model.device.DeviceState;
import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.connection.CommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeviceManager {
    private static final Logger log = LoggerFactory.getLogger(DeviceManager.class);

    private final CommService commService;
    public final DeviceController deviceController;

    private List<Device> devices = new ArrayList<>();
    private final Timer timer;

    private Configuration configuration;

    DeviceManager(CommService commService, Configuration configuration) {
        this.commService = commService;
        this.configuration = configuration;
        this.deviceController = new DeviceController(commService, this);

        devices.add(new Device(DeviceType.LIGHT));
        devices.add(new Device(DeviceType.CLOCK));
        devices.add(new Device(DeviceType.DOOR));
        devices.add(new Device(DeviceType.BLIND1));
        devices.add(new Device(DeviceType.THERMOMETER));

        devices.stream().map(device -> device.getDeviceType().getSubTopic()).forEach(commService::connect);
        commService.register(this::updateState);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                validateStates();
            }
        }, 0, 5000);
    }

    @PreDestroy
    private void shutdown() {
        timer.cancel();
    }


    public DeviceState getState(DeviceType deviceType) {
        log.debug("GETTING STATE OF {}", deviceType);
        return getDeviceState(deviceType);
    }

    private void updateState(Message message) {
        String[] data = message.getData();
        DeviceType deviceType = message.getDeviceType();
        sendMessage(deviceType, message.getReturnMessage());

        log.debug("{}: Updated state to {}", deviceType, data);
        DeviceState deviceState = getDeviceState(deviceType);

        if (deviceState != null) {
            if (deviceState.update(data)) {
                log.info("State changed for {} to {}. Notifying listeners", deviceType, data);
            } else {
                log.debug("State not changed for {}.", deviceType);
            }
        } else {
            log.warn("Unsupported device type {}", deviceType);
        }
    }

    private void validateStates() {
        String message = devices.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n", "\nDevice statuses:\n", ""));
        log.info(message);

        if (getNightModeToggleDuration() <= 2500) {
            if (getNightModeState()) {
                executeNightMode(getNightModeState());
            }
        }
    }

    private DeviceState getDeviceState(DeviceType deviceType) {
        try {
            return devices.stream()
                    .filter(device -> device.getDeviceType() == deviceType)
                    .findAny().orElseThrow(DeviceNotFoundException::new)
                    .getDeviceState();
        } catch (DeviceNotFoundException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    private boolean isDeviceEnabled(DeviceType deviceType) {
        DeviceState deviceState = getDeviceState(deviceType);
        if (deviceState != null) {
            return deviceState.getData()[0].equals("1");
        } else return false;
    }

    public String getData(DeviceType deviceType, int dataIndex) {
        try {
            return getState(deviceType).getData()[dataIndex];
        } catch (NullPointerException ex) {
            return null;
        }
    }


    //NIGHT MODE ------------------------------------------------------------------------------
    public boolean toggleNightMode() {
        log.info("TOGGLED NIGHT MODE");
        boolean currentState = getNightModeState();
        executeNightMode(currentState);
        return currentState;
    }

    private long getNightModeToggleDuration() {
        LocalTime time = LocalTime.now();
        LocalTime clockToggleTime = LocalTime.parse(configuration.readProperty("nightMode.toggle_hour"));
        long duration = Duration.between(clockToggleTime, time).toMillis();
        return Math.abs(duration);
    }

    public boolean getNightModeState() {
        return configuration.readProperty("nightMode.toggled").equals("true");
    }


    private void executeNightMode(boolean currentState) {
        configuration.setProperty("nightMode.toggled", String.valueOf(currentState));
        if (currentState) {
            if (configuration.readProperty("blinds.morning_toggle").equals("true"))
                deviceController.setBlind(DeviceType.BLIND1, "1");
            if (configuration.readProperty("clock.morning_toggle").equals("true"))
                if (isDeviceEnabled(DeviceType.CLOCK)) deviceController.toggleDevice(DeviceType.CLOCK);
            if (configuration.readProperty("door.lock_on_nightMode").equals("true"))
                if (isDeviceEnabled(DeviceType.DOOR)) deviceController.toggleDevice(DeviceType.DOOR);
            if (configuration.readProperty("door.morning_screen_toggle").equals("true"))
                if (Objects.requireNonNull(getDeviceState(DeviceType.DOOR)).getData()[1].equals("1"))
                    deviceController.toggleDoorScreen();
        } else {
            if (configuration.readProperty("blinds.morning_toggle").equals("true"))
                deviceController.setBlind(DeviceType.BLIND1, "5");
            if (configuration.readProperty("clock.morning_toggle").equals("true"))
                if (!isDeviceEnabled(DeviceType.CLOCK)) deviceController.toggleDevice(DeviceType.CLOCK);
            if (configuration.readProperty("door.lock_on_nightMode").equals("true"))
                if (!isDeviceEnabled(DeviceType.DOOR)) deviceController.toggleDevice(DeviceType.DOOR);
            if (configuration.readProperty("door.morning_screen_toggle").equals("true"))
                if (Objects.requireNonNull(getDeviceState(DeviceType.DOOR)).getData()[1].equals("0"))
                    deviceController.toggleDoorScreen();
        }
    }

    protected void sendMessage(DeviceType deviceType, String msg) {
        commService.sendMessage(deviceType.getPubTopic(), msg);
    }
}
