package org.jk.smlite.services;

import org.jk.smlite.services.device.DeviceType;

import java.time.Instant;
import java.time.LocalDateTime;

public class Message {
    private final Instant time;
    private final String topic;
    private final boolean state;
    private final String message;
    private final String returnMessage;
    private DeviceType type;

    public Message(Instant time, String topic, String message) {
        this.time = time;
        this.topic = topic;
        this.message = message;

        this.state = message.contains("1");

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
            else if (message.contains("windoweast"))
                this.type = DeviceType.WINDOW_EAST;
            else if (message.contains("windowssouth"))
                this.type = DeviceType.WINDOW_SOUTH;
        }
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

    public boolean isState() {
        return state;
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
