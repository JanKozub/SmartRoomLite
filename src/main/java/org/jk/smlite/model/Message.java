package org.jk.smlite.model;

import org.jk.smlite.exceptions.DeviceNotRecognizedException;
import org.jk.smlite.model.device.DeviceType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Message {
    private final Instant time;
    private final String topic;
    private final String[] data;
    private final String message;
    private final String returnMessage;
    private DeviceType deviceType;

    public Message(Instant time, String topic, String message) throws DeviceNotRecognizedException {
        this.time = time;
        this.topic = topic;
        this.message = message;

        String[] values = message.split(",");

        try {
            this.deviceType = DeviceType.valueOf(values[0].toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new DeviceNotRecognizedException("DEVICE NOT FOUND");
        }

        if (this.deviceType == DeviceType.CLOCK) {
            String msg = "";
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            int minute = now.getMinute();
            if (hour < 10) {
                msg = "0";
            }
            msg = msg + hour;
            if (minute < 10) {
                msg = msg + "0";
            }
            msg = msg + minute;
            this.returnMessage = msg;
        } else {
            this.returnMessage = "ACTIVE";
        }

        data = Arrays.copyOfRange(values, 1, values.length);
    }

    public Instant getTime() {
        return time;
    }

    public String getTopic() {
        return topic;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getMessage() {
        return message;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public String[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "time=" + time +
                ", topic='" + topic + '\'' +
                ", state=" + Arrays.toString(data) +
                ", message='" + message + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", type=" + deviceType +
                '}';
    }
}
