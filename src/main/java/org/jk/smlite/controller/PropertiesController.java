package org.jk.smlite.controller;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.Configuration;
import org.jk.smlite.services.device.DeviceManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties**")
@CrossOrigin(origins = "${angular.server.ip}")
public class PropertiesController {
    private final DeviceManager deviceManager;
    private final Configuration configuration;

    public PropertiesController(DeviceManager deviceManager, Configuration configuration) {
        this.deviceManager = deviceManager;
        this.configuration = configuration;
    }

    @GetMapping("/getProperties/switches")
    public String getSwitchesProperties() {
        return "{\n" +
                "\"temp\": " + deviceManager.getDeviceState(DeviceType.THERMOMETER).getData(1) + ", \n" +
                "\"hum\": " + deviceManager.getDeviceState(DeviceType.THERMOMETER).getData(2) + ", \n" +
                "\"light\": " + deviceManager.getDeviceState(DeviceType.LIGHT).getData(0) + ", \n" +
                "\"clock\": " + deviceManager.getDeviceState(DeviceType.CLOCK).getData(0) + ", \n" +
                "\"lock\": " + deviceManager.getDeviceState(DeviceType.DOOR).getData(0) + ", \n" +
                "\"doorScreen\": " + deviceManager.getDeviceState(DeviceType.DOOR).getData(1) + ", \n" +
                "\"nightMode\": " + configuration.readProperty("nightMode.toggled") +
                "}";
    }

    @GetMapping("/getProperties/control")
    public String getControlProperties() {
        return "{\n" +
                "\"temp\": " + deviceManager.getDeviceState(DeviceType.THERMOMETER).getData(1) + ", \n" +
                "\"hum\": " + deviceManager.getDeviceState(DeviceType.THERMOMETER).getData(2) + ", \n" +
                "\"speakers\": " + deviceManager.getDeviceState(DeviceType.SPEAKERS).getData(0) + ", \n" +
                "\"thermometerScreen\": " + deviceManager.getDeviceState(DeviceType.THERMOMETER).getData(0) +
                "}";
    }

    @GetMapping("/getProperties/settings")
    public String getSettingsProperties() {
        return "{\n" +
                "\"temp\": " + deviceManager.getDeviceState(DeviceType.THERMOMETER).getData(1) + ", \n" +
                "\"hum\": " + deviceManager.getDeviceState(DeviceType.THERMOMETER).getData(2) + ", \n" +
                "\"nightModeToggleHour\": \"" + configuration.readProperty("nightMode.toggle_hour") + "\", \n" +
                "\"setBlindsUp\": " + configuration.readProperty("blinds.morning_toggle") + ", \n" +
                "\"toggleClock\": " + configuration.readProperty("clock.morning_toggle") + ", \n" +
                "\"lockDoor\": " + configuration.readProperty("door.lock_on_nightMode") + ", \n" +
                "\"thermometerScreen\": " + configuration.readProperty("thermometer.morning_screen_toggle") + ", \n" +
                "\"toggleDoorScreen\": " + configuration.readProperty("door.morning_screen_toggle") +
                "}";
    }
}
