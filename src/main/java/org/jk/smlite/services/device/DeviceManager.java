package org.jk.smlite.services.device;

import org.jk.smlite.exceptions.DeviceNotFoundException;
import org.jk.smlite.model.DataType;
import org.jk.smlite.model.Device;
import org.jk.smlite.model.Message;
import org.jk.smlite.services.connection.CommService;
import org.jk.smlite.services.connection.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class DeviceManager {
    private static final Logger log = LoggerFactory.getLogger(DeviceManager.class);

    private final CommService commService;

    private final Map<DeviceType, Lock> locks = new ConcurrentHashMap<>();

    private List<Device> devices = new ArrayList<>();

    DeviceManager(CommService commService) {
        this.commService = commService;

        devices.add(new Device(DeviceType.CLOCK));
        devices.add(new Device(DeviceType.LIGHT));
        devices.add(new Device(DeviceType.DOOR));
        devices.add(new Device(DeviceType.BLIND1));

        devices.stream().map(device -> device.getDeviceType().getSubTopic()).forEach(commService::connect);
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
        if (deviceType.getDataType() == DataType.BOOLEAN) {
            log.info("TOGGLED DEVICE {}", deviceType);

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            MessageListener listener = message -> {
                if (message.getDeviceType() == deviceType) {
                    log.info("Light state: {}", message);
                    future.complete(message.isEnabled());
                }
            };

            Lock lock = locks.computeIfAbsent(deviceType, k -> new ReentrantLock());
            lock.lock();
            try {
                commService.register(listener);
                try {
                    commService.sendMessage(deviceType.getPubTopic(), "TOGGLE");
                    return future.get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException();
                } catch (ExecutionException | TimeoutException e) {
                    throw new RuntimeException(e);
                } finally {
                    commService.unregister(listener);
                }
            } finally {
                lock.unlock();
            }
        } else {
            log.error("{} IS NOT A BOOLEAN TYPE DEVICE", deviceType);
            return false;
        }
    }

    public int setBlind(DeviceType deviceType, String position) {
        if (deviceType.getDataType() == DataType.INTEGER) {
            log.info("SETTING {} TO {}", deviceType, position);
            if (deviceType == DeviceType.BLIND1) {
                MessageListener listener = new MessageListener() {
                    @Override
                    public void messageArrived(Message message) {
                        if (message.getDeviceType() == deviceType) {
                            log.info("Blind position: {}", message.getState());
                            commService.unregister(this);
                        }
                    }
                };
                commService.register(listener);
                commService.sendMessage(deviceType.getPubTopic(), position);

                return Integer.parseInt(position);
            } else {
                if (deviceType == DeviceType.BLIND2) {
                    //TODO 2ND BLIND
                }
                log.error("{} MADE A UNHANDLED REQUEST", deviceType);
                return -1;
            }
        } else {
            log.error("{} IS NOT A INTEGER TYPE DEVICE", deviceType);
            return -1;
        }
    }

    private void sendMessage(DeviceType deviceType, String msg) {
        commService.sendMessage(deviceType.getPubTopic(), msg);
    }

    public DeviceState getState(DeviceType deviceType) {
        log.info("GETTING STATE OF {}", deviceType);
        return getDeviceState(deviceType);
    }

    private void updateState(Message message) {
        int state = message.getState();
        DeviceType deviceType = message.getDeviceType();
        sendMessage(deviceType, message.getReturnMessage());

        log.debug("Updating state of {} to {}", deviceType, state);
        DeviceState deviceState = getDeviceState(deviceType);

        if (deviceState != null) {
            if (deviceState.update(state)) {
                log.info("State changed for {} to {}. Notifying listeners", deviceType, state);
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

        LocalDateTime time = LocalDateTime.now();

        if (time.getHour() == 5 && time.getMinute() == 0 && time.getSecond() > 0 && time.getSecond() <= 5) {
            DeviceState deviceState = getDeviceState(DeviceType.CLOCK);
            if (deviceState != null) {
                if (!deviceState.isEnabled()) {
                    sendMessage(DeviceType.CLOCK, "TOGGLE");
                }
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
}
