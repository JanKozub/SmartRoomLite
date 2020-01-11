package org.jk.smlite.model;

import org.jk.smlite.exceptions.DeviceNotRecognizedException;
import org.jk.smlite.model.device.DeviceType;

import java.time.Instant;
import java.time.LocalDateTime;

public class Message {
    private final Instant time;
    private final String topic;
    private final char[] data;
    private final String message;
    private final String returnMessage;
    private DeviceType deviceType;

    public Message(Instant time, String topic, String message) throws DeviceNotRecognizedException {
        this.time = time;
        this.topic = topic;
        this.message = message;

        String[] values = message.split("-");

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

        data = values[1].toCharArray();
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

    public static String iterateData(char[] data) {
        StringBuilder output = new StringBuilder();
        for (char datum : data) output.append(datum);
        return output.toString();
    }

    public String getMessage() {
        return message;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public char[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "time=" + time +
                ", topic='" + topic + '\'' +
                ", state=" + iterateData(data) +
                ", message='" + message + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", type=" + deviceType +
                '}';
    }
}
