package org.jk.smlite.controller;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.Configuration;
import org.jk.smlite.services.DeviceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties**")
@CrossOrigin(origins = "${angular.server.ip}")
public class PropertiesController {
    private static final Logger log = LoggerFactory.getLogger(BlindController.class);
    private final DeviceManager deviceManager;
    private final Configuration configuration;

    public PropertiesController(DeviceManager deviceManager, Configuration configuration) {
        this.deviceManager = deviceManager;
        this.configuration = configuration;
    }

    @GetMapping("/getProperties/switches")
    public String getSwitchesProperties() {
        return "{\n" +
                "\"temp\": " + deviceManager.getData(DeviceType.THERMOMETER, 1) + ", \n" +
                "\"hum\": " + deviceManager.getData(DeviceType.THERMOMETER, 2) + ", \n" +
                "\"light\": " + deviceManager.getData(DeviceType.LIGHT, 0) + ", \n" +
                "\"clock\": " + deviceManager.getData(DeviceType.CLOCK, 0) + ", \n" +
                "\"lock\": " + deviceManager.getData(DeviceType.DOOR, 0) + ", \n" +
                "\"doorScreen\": " + deviceManager.getData(DeviceType.DOOR, 1) + ", \n" +
                "\"nightMode\": " + configuration.readProperty("nightMode.toggled") +
                "}";
    }

    @GetMapping("/getProperties/control")
    public String getControlProperties() {
        return "{\n" +
                "\"blind1\": " + deviceManager.getData(DeviceType.BLIND1, 0) +
                "}";
    }

    @GetMapping("/getProperties/settings")
    public String getSettingsProperties() {
        return "{\n" +
                "\"nightModeToggleHour\": \"" + configuration.readProperty("nightMode.toggle_hour") + "\", \n" +
                "\"setBlindsUp\": " + configuration.readProperty("blinds.morning_toggle") + ", \n" +
                "\"toggleClock\": " + configuration.readProperty("clock.morning_toggle") + ", \n" +
                "\"lockDoor\": " + configuration.readProperty("door.lock_on_nightMode") + ", \n" +
                "\"toggleDoorScreen\": " + configuration.readProperty("door.morning_screen_toggle") +
                "}";
    }
}
