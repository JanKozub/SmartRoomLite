package org.jk.smlite.services.device;

import org.jk.smlite.services.Message;
import org.jk.smlite.services.connection.CommService;
import org.jk.smlite.services.connection.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class DeviceManager {
    private static final Logger log = LoggerFactory.getLogger(DeviceManager.class);

    private final Map<DeviceType, DeviceState> deviceStates = new HashMap<>();
    private final CommService commService;

    private final Map<DeviceType, Lock> locks = new ConcurrentHashMap<>();

    DeviceManager(CommService commService) {
        this.commService = commService;

        deviceStates.put(DeviceType.CLOCK, new DeviceState(DeviceType.CLOCK, Duration.ofSeconds(15)));
        deviceStates.put(DeviceType.LIGHT, new DeviceState(DeviceType.LIGHT, Duration.ofSeconds(15)));
        deviceStates.put(DeviceType.DOOR, new DeviceState(DeviceType.DOOR, Duration.ofSeconds(15)));
        deviceStates.put(DeviceType.BLIND1, new DeviceState(DeviceType.BLIND1, Duration.ofSeconds(15)));

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
        if (deviceType.getDataType() == DataType.BOOLEAN) {
            log.info("TOGGLED DEVICE {}", deviceType);

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            MessageListener listener = message -> {
                if (message.getType() == deviceType) {
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
            log.info("SETTING {} for {}", deviceType, position);
            if (deviceType == DeviceType.BLIND1) {
                CompletableFuture<Integer> future = new CompletableFuture<>();
                MessageListener listener = message -> {
                    if (message.getType() == deviceType) {
                        log.info("Blind state: {}", message);
                        future.complete(message.getState());
                    }
                };

                Lock lock = locks.computeIfAbsent(deviceType, k -> new ReentrantLock());
                lock.lock();
                try {
                    commService.register(listener);
                    try {
                        commService.sendMessage(deviceType.getPubTopic(), position);
                        return future.get(500, TimeUnit.MILLISECONDS);
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
                if (deviceType == DeviceType.BLIND2) {
                    //TODO 2ND BLIND
                }
                log.error("{} MADE A UNHANDLED REQUEST", deviceType);
                return -1;
            }
        } else {
            log.error("{} IS NOT A BOOLEAN TYPE DEVICE", deviceType);
            return -1;
        }
    }

    public void sendMessage(DeviceType deviceType, String msg) {
        commService.sendMessage(deviceType.getPubTopic(), msg);
    }

    public DeviceState getState(DeviceType deviceType) {
        log.info("GETTING STATE OF {}", deviceType);
        return deviceStates.get(deviceType);
    }

    private void updateState(Message message) {
        int state = message.getState();
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
