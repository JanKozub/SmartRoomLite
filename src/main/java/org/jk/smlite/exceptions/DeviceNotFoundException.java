package org.jk.smlite.exceptions;

public class DeviceNotFoundException extends Exception {
    public DeviceNotFoundException() {
        super("Device is not connected");
    }
}
