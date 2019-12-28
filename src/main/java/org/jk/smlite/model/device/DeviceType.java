package org.jk.smlite.model.device;

import org.jk.smlite.model.DataType;

public enum DeviceType {
    LIGHT("relay", "relaySub", DataType.BOOLEAN),
    DOOR("door", "doorSub", DataType.BOOLEAN),
    CLOCK("clock", "clockSub", DataType.BOOLEAN),
    BLIND1("blind1", "blind1Sub", DataType.INTEGER),
    BLIND2("blind2", "blind2Sub", DataType.INTEGER),
    GATE("", "", DataType.BOOLEAN);


    private final String subTopic;
    private final String pubTopic;
    private final DataType dataType;

    DeviceType(String subTopic, String pubTopic, DataType dataType) {
        this.subTopic = subTopic;
        this.pubTopic = pubTopic;
        this.dataType = dataType;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public String getPubTopic() {
        return pubTopic;
    }

    public DataType getDataType() {
        return dataType;
    }
}
