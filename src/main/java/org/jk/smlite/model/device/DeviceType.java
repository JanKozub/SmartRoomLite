package org.jk.smlite.model.device;

import java.util.ArrayList;

public enum DeviceType {
    LIGHT("light", "lightSub"),
    DOOR("door", "doorSub"),
    CLOCK("clock", "clockSub"),
    BLIND1("blind1", "blind1Sub"),
    BLIND2("blind2", "blind2Sub"),
    GATE("", "");

    private final String subTopic;
    private final String pubTopic;

    DeviceType(String subTopic, String pubTopic) {
        this.subTopic = subTopic;
        this.pubTopic = pubTopic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public String getPubTopic() {
        return pubTopic;
    }


    public static boolean isDeviceToggle(DeviceType deviceType) {
        return toggleDevices().contains(deviceType);
    }

    public static boolean isDeviceBlind(DeviceType deviceType) {
        return blinds().contains(deviceType);
    }


    private static ArrayList<DeviceType> toggleDevices() {
        ArrayList<DeviceType> devices = new ArrayList<>();
        devices.add(LIGHT);
        devices.add(DOOR);
        devices.add(CLOCK);
        return devices;
    }

    private static ArrayList<DeviceType> blinds() {
        ArrayList<DeviceType> devices = new ArrayList<>();
        devices.add(BLIND1);
        devices.add(BLIND2);
        return devices;
    }
}
