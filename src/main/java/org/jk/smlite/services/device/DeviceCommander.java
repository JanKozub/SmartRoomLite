package org.jk.smlite.services.device;

import org.jk.smlite.model.Message;
import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.connection.CommService;
import org.jk.smlite.services.connection.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class DeviceCommander {

    private static final Logger log = LoggerFactory.getLogger(DeviceCommander.class);
    private final Map<DeviceType, Lock> locks = new ConcurrentHashMap<>();

    private final CommService commService;

    DeviceCommander(CommService commService) {
        this.commService = commService;
    }

    public boolean toggleDevice(DeviceType deviceType) {
        if (DeviceType.isDeviceToggle(deviceType)) {
            log.info("TOGGLED DEVICE {}", deviceType);

            return changeState(deviceType, 10, "TOGGLE",
                    msg -> msg.getData()[0].equals("1"));
        } else {
            log.error("{} IS NOT A BOOLEAN TYPE DEVICE", deviceType);
            return false;
        }
    }

    public boolean toggleDoorScreen() {
        log.info("TOGGLED DOOR SCREEN");

        return changeState(DeviceType.DOOR, 10, "SCREEN",
                msg -> msg.getData()[1].equals("1"));
    }

    public boolean setBlind(DeviceType deviceType, String position) {
        if (DeviceType.isDeviceBlind(deviceType)) {
            log.info("SETTING {} TO {}", deviceType, position);

            return changeState(deviceType, 75, position,
                    msg -> msg.getData()[0].equals(position));
        } else {
            log.error("{} IS NOT A BLIND", deviceType);
            return false;
        }
    }

    void sendMessage(DeviceType deviceType, String msg) {
        commService.sendMessage(deviceType.getPubTopic(), msg);
    }


    private boolean changeState(DeviceType deviceType, int timeout, String message, Predicate<Message> predicate) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        MessageListener listener = msg -> {
            if (msg.getDeviceType() == deviceType) {
                log.info("{} state: {}", deviceType, msg);
                future.complete(predicate.test(msg));
            }
        };

        Lock lock = locks.computeIfAbsent(deviceType, k -> new ReentrantLock());
        lock.lock();
        try {
            commService.register(listener);
            try {
                sendMessage(deviceType, message);
                return future.get(timeout, TimeUnit.SECONDS);
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
    }
}
