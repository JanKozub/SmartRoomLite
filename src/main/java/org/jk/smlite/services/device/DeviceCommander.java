package org.jk.smlite.services.device;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.connection.CommService;
import org.jk.smlite.services.connection.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeviceCommander {

    private static final Logger log = LoggerFactory.getLogger(DeviceCommander.class);
    private final Map<DeviceType, Lock> locks = new ConcurrentHashMap<>();

    private final CommService commService;

    public DeviceCommander(CommService commService) {
        this.commService = commService;
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

    protected void sendMessage(DeviceType deviceType, String msg) {
        commService.sendMessage(deviceType.getPubTopic(), msg);
    }
}
