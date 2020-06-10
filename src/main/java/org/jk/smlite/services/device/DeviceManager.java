package org.jk.smlite.services.device;

import org.jk.smlite.exceptions.DeviceNotFoundException;
import org.jk.smlite.model.Message;
import org.jk.smlite.model.device.Device;
import org.jk.smlite.model.device.DeviceState;
import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.Configuration;
import org.jk.smlite.services.connection.CommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Component
public class DeviceManager {
    private static final Logger log = LoggerFactory.getLogger(DeviceManager.class);

    private final DeviceCommander deviceCommander;
    private final NightModeHandler nightModeHandler;

    private final List<Device> devices = new ArrayList<>();
    private final Timer timer;

    private long stateValidationCounter;

    DeviceManager(CommService commService, Configuration configuration) {
        this.deviceCommander = new DeviceCommander(commService);
        this.nightModeHandler = new NightModeHandler(configuration, this, deviceCommander);

        devices.add(new Device(DeviceType.LIGHT));
        devices.add(new Device(DeviceType.CLOCK));
        devices.add(new Device(DeviceType.DOOR));
        devices.add(new Device(DeviceType.BLIND1));
        devices.add(new Device(DeviceType.BLIND2));
        devices.add(new Device(DeviceType.THERMOMETER));
        devices.add(new Device(DeviceType.MICROPHONE));
        devices.add(new Device(DeviceType.SPEAKERS));

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

    public DeviceCommander getDeviceCommander() {
        return deviceCommander;
    }

    public NightModeHandler getNightModeHandler() {
        return nightModeHandler;
    }

    private void updateState(Message message) {
        String[] data = message.getData();
        DeviceType deviceType = message.getDeviceType();
        deviceCommander.sendMessage(deviceType, message.getReturnMessage());

        log.debug("{}: Updated state to {}", deviceType, data);
        DeviceState deviceState = getDeviceState(deviceType);

        if (deviceState != null) {
            if (deviceState.update(data)) {
                if (deviceType != DeviceType.THERMOMETER) {
                    log.info("State changed for {} to {}. Notifying listeners", deviceType, data);
                } else {
                    log.debug("Temperature changed to {}", data);
                }

                deviceCommander.handleDoubleClap(deviceType, stateValidationCounter);
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
                .collect(Collectors.joining("\n",
                        "\nDevice statuses:\n|Device:         |Connected:|Last updated:            |Data\n",
                        "\n"));
        log.info(message);

        if (nightModeHandler.getNightModeToggleDuration() <= 2500)
            if (nightModeHandler.getNightModeState())
                nightModeHandler.toggleNightMode();

        stateValidationCounter = stateValidationCounter + 1;
    }

    public DeviceState getDeviceState(DeviceType deviceType) {
        log.debug("GETTING STATE OF {}", deviceType);
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

    @PreDestroy
    private void shutdown() {
        timer.cancel();
    }
}
