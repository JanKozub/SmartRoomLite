package org.jk.smlite.model.device;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

public class DeviceState {
    private final Duration timeout;
    private final Device device;
    private volatile String[] data;
    private volatile Instant lastUpdated = Instant.EPOCH;

    public DeviceState(Device device, Duration timeout) {
        this.device = device;
        this.timeout = timeout;
    }

    public Device getDevice() {
        return device;
    }

    public String getData(int dataIndex) {
        try {
            return data[dataIndex];
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public boolean isConnected() {
        return Duration.between(lastUpdated, Instant.now()).compareTo(timeout) < 0;
    }

    private Instant getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        Instant lu = getLastUpdated();
        LocalDateTime dateTime = LocalDateTime.ofInstant(lu, ZoneId.systemDefault());
        int second = dateTime.getSecond();
        String secondVal = "" + second;
        if (second < 10)
            secondVal = "0" + second + ", ";

        int month = dateTime.getMonthValue();
        String monthVal = "" + month;
        if (month < 10)
            monthVal = "0" + month + ".";

        String date = dateTime.getHour() + ":" +
                dateTime.getMinute() + ":" + secondVal +
                dateTime.getDayOfMonth() + "." + monthVal +
                dateTime.getYear();

        String dataStr = Arrays.toString(data);
        if (!dataStr.equals("null"))
            dataStr = dataStr.substring(1, dataStr.length() - 1);

        return String.format("|%1$-16s|%2$10s|%3$25s|%4$-20s",
                getDevice().getDeviceType(),
                isConnected() ? "yes" : "no",
                lu == Instant.EPOCH ? "never" : date,
                dataStr);
    }

    public boolean update(String[] data) {
        this.lastUpdated = Instant.now();
        if (!Arrays.equals(this.data, data)) {
            this.data = data;
            return true;
        } else return false;
    }
}
