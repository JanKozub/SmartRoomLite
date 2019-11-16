package org.jk.smlite.services;

import org.jk.smlite.exceptions.DeviceNotRecognizedException;
import org.jk.smlite.services.device.DeviceType;

import java.time.Instant;
import java.time.LocalDateTime;

public class Message {
    private final Instant time;
    private final String topic;
    private final int state;
    private final boolean isEnabled;
    private final String message;
    private final String returnMessage;
    private DeviceType type;

    public Message(Instant time, String topic, String message) throws DeviceNotRecognizedException {
        this.time = time;
        this.topic = topic;
        this.message = message;

        if (message.contains("clock")) {
            this.type = DeviceType.CLOCK;

            String msg = "";
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            int minute = now.getMinute();
            if (hour < 10)
                msg = "0";
            msg = msg + hour;
            if (minute < 10)
                msg = msg + "0";
            msg = msg + minute;
            this.returnMessage = msg;
        } else {
            this.returnMessage = "ACTIVE";
            if (message.contains("relay"))
                this.type = DeviceType.LIGHT;
            else if (message.contains("door"))
                this.type = DeviceType.DOOR;
            else if (message.contains("blind1"))
                this.type = DeviceType.BLIND1;
            else if (message.contains("blind2"))
                this.type = DeviceType.BLIND2;
            else {
                throw new DeviceNotRecognizedException("DEVICE NOT FOUND");
            }
        }

        this.state = Integer.parseInt(message.substring(type.getSubTopic().length()));
        this.isEnabled = state == 1;
    }

    public Instant getTime() {
        return time;
    }

    public String getTopic() {
        return topic;
    }

    public DeviceType getType() {
        return type;
    }

    public int getState() {
        return state;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getMessage() {
        return message;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "time=" + time +
                ", topic='" + topic + '\'' +
                ", state=" + state +
                ", message='" + message + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", type=" + type +
                '}';
    }
}
