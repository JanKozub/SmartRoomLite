package org.jk.smlite.services;

import org.jk.smlite.exceptions.DeviceNotFoundException;
import org.jk.smlite.model.Message;
import org.jk.smlite.model.device.Device;
import org.jk.smlite.model.device.DeviceState;
import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.connection.CommService;
import org.jk.smlite.services.connection.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.LocalTime;
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
    private final Timer timer;

    private Configuration configuration;

    DeviceManager(CommService commService, Configuration configuration) {
        this.commService = commService;
        this.configuration = configuration;

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

    public boolean toggleDevice(DeviceType deviceType) {
        if (DeviceType.isDeviceToggle(deviceType)) {
            log.info("TOGGLED DEVICE {}", deviceType);

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            MessageListener listener = message -> {
                if (message.getDeviceType() == deviceType) {
                    log.info("{} state: {}", deviceType, message);
                    future.complete(message.getData()[0].equals("1"));
                }
            };

            Lock lock = locks.computeIfAbsent(deviceType, k -> new ReentrantLock());
            lock.lock();
            try {
                commService.register(listener);
                try {
                    sendMessage(deviceType, "TOGGLE");
                    return future.get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException();
                } catch (ExecutionException | TimeoutException e) {
                    log.error("{} timed out", deviceType);
                    return false;
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

    public boolean toggleDoorScreen() {
        log.info("TOGGLED DOOR SCREEN");

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        MessageListener listener = message -> {
            if (message.getDeviceType() == DeviceType.DOOR) {
                log.info("Door screen state: {}", message);
                future.complete(message.getData()[1].equals("1"));
            }
        };

        Lock lock = locks.computeIfAbsent(DeviceType.DOOR, k -> new ReentrantLock());
        lock.lock();
        try {
            commService.register(listener);
            try {
                sendMessage(DeviceType.DOOR, "SCREEN");
                return future.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException();
            } catch (ExecutionException | TimeoutException e) {
                log.error("Door screen timed out");
                return false;
            } finally {
                commService.unregister(listener);
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean setBlind(DeviceType deviceType, String position) {
        if (DeviceType.isDeviceBlind(deviceType)) {
            log.info("SETTING {} TO {}", deviceType, position);

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            MessageListener listener = message -> {
                if (message.getDeviceType() == deviceType) {
                    log.info("{} state: {}", deviceType, message);
                    future.complete(message.getData()[0].equals(position));
                }
            };

            Lock lock = locks.computeIfAbsent(deviceType, k -> new ReentrantLock());
            lock.lock();
            try {
                commService.register(listener);
                try {
                    sendMessage(deviceType, position);
                    return future.get(75, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException();
                } catch (ExecutionException | TimeoutException e) {
                    log.error("{} timed out", deviceType);
                    return false;
                } finally {
                    commService.unregister(listener);
                }
            } finally {
                lock.unlock();
            }
        } else {
            log.error("{} IS NOT A BLIND", deviceType);
            return false;
        }
    }

    public boolean toggleNightMode() {
        log.info("TOGGLED NIGHT MODE");
        boolean currentState = getNightModeState();
        executeNightMode(currentState);
        return currentState;
    }

    private void sendMessage(DeviceType deviceType, String msg) {
        commService.sendMessage(deviceType.getPubTopic(), msg);
    }

    public DeviceState getState(DeviceType deviceType) {
        log.info("GETTING STATE OF {}", deviceType);
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
                setBlind(DeviceType.BLIND1, "1");
            if (configuration.readProperty("clock.morning_toggle").equals("true"))
                if (isDeviceEnabled(DeviceType.CLOCK)) toggleDevice(DeviceType.CLOCK);
            if (configuration.readProperty("door.lock_on_nightMode").equals("true"))
                if (isDeviceEnabled(DeviceType.DOOR)) toggleDevice(DeviceType.DOOR);
            if (configuration.readProperty("door.morning_screen_toggle").equals("true"))
                if (Objects.requireNonNull(getDeviceState(DeviceType.DOOR)).getData()[1].equals("1"))
                    toggleDoorScreen();
        } else {
            if (configuration.readProperty("blinds.morning_toggle").equals("true"))
                setBlind(DeviceType.BLIND1, "5");
            if (configuration.readProperty("clock.morning_toggle").equals("true"))
                if (!isDeviceEnabled(DeviceType.CLOCK)) toggleDevice(DeviceType.CLOCK);
            if (configuration.readProperty("door.lock_on_nightMode").equals("true"))
                if (!isDeviceEnabled(DeviceType.DOOR)) toggleDevice(DeviceType.DOOR);
            if (configuration.readProperty("door.morning_screen_toggle").equals("true"))
                if (Objects.requireNonNull(getDeviceState(DeviceType.DOOR)).getData()[1].equals("0"))
                    toggleDoorScreen();
        }
    }
}
