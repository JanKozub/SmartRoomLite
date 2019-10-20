package org.jk.smlite.services.device;

import org.jk.smlite.services.Message;
import org.jk.smlite.services.connection.CommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Component
public class DeviceManager {
    private static final Logger log = LoggerFactory.getLogger(DeviceManager.class);

    private final Map<DeviceType, DeviceState> deviceStates = new HashMap<>();
    private final CommService commService;

    DeviceManager(CommService commService) {
        this.commService = commService;

        deviceStates.put(DeviceType.CLOCK, new DeviceState(DeviceType.CLOCK, Duration.ofSeconds(15)));
        deviceStates.put(DeviceType.LIGHT, new DeviceState(DeviceType.LIGHT, Duration.ofSeconds(15)));
        deviceStates.put(DeviceType.DOOR, new DeviceState(DeviceType.DOOR, Duration.ofSeconds(15)));

        deviceStates.keySet().stream().map(DeviceType::getSubTopic).forEach(commService::connect);
        commService.register(this::updateState);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                validateStates();
            }
        }, 0, 5000);
    }

    public boolean toggleDevice(DeviceType deviceType) {
        log.info("TOGGLED DEVICE {}", deviceType);
        commService.sendMessage(deviceType.getPubTopic(), "TOGGLE");
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {

        }
        return getState(DeviceType.LIGHT).isEnabled();
    }

    public void sendMessage(DeviceType deviceType, String msg) {
        commService.sendMessage(deviceType.getPubTopic(), msg);
    }

    public DeviceState getState(DeviceType deviceType) {
        log.info("GETTING STATE OF {}", deviceType);
        return deviceStates.get(deviceType);
    }

    private void updateState(Message message) {
        boolean state = message.isState();
        DeviceType type = message.getType();
        sendMessage(type, message.getReturnMessage());

        log.debug("Updating state of {} to {}", type, state);

        DeviceState deviceState = deviceStates.get(type);
        if (deviceState != null) {
            if (deviceState.update(state)) {
                log.info("State changed for {} to {}. Notifying listeners", type, state);
            } else {
                log.debug("State not changed for {}.", type);
            }
        } else {
            log.warn("Unsupported device type {}", type);
        }
    }

    private void validateStates() {
        String message = deviceStates.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n", "\nDevice statuses:\n", ""));

        log.info(message);

        LocalDateTime time = LocalDateTime.now();
        if (time.getHour() == 5 && time.getMinute() == 0 && time.getSecond() > 0 && time.getSecond() <= 5) {
            if (!deviceStates.get(DeviceType.CLOCK).isEnabled()) {
                sendMessage(DeviceType.CLOCK, "TOGGLE");
            }
        }
    }
}
