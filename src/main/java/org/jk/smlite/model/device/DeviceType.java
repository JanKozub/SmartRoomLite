package org.jk.smlite.model.device;

import java.util.EnumSet;

public enum DeviceType {
    LIGHT("light", "lightSub"),
    DOOR("door", "doorSub"),
    CLOCK("clock", "clockSub"),
    BLIND1("blind1", "blind1Sub"),
    BLIND2("blind2", "blind2Sub"),
    THERMOMETER("thermometer", "thermometerSub"),
    MICROPHONE("microphone", "microphoneSub"),
    GATE("", "");


    private static final EnumSet<DeviceType> toggleableDevices = EnumSet.of(LIGHT, CLOCK, DOOR, THERMOMETER);
    private static final EnumSet<DeviceType> blinds = EnumSet.of(BLIND1, BLIND2);

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
        return toggleableDevices.contains(deviceType);
    }

    public static boolean isDeviceBlind(DeviceType deviceType) {
        return blinds.contains(deviceType);
    }
}
