package org.jk.smlite.controller;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.device.DeviceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/switch")
@CrossOrigin(origins = "${angular.server.ip}")
public class SwitchController {
    private static final Logger log = LoggerFactory.getLogger(SwitchController.class);
    private DeviceManager deviceManager;

    public SwitchController(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @PostMapping("/setState")
    public boolean setState(@Valid @RequestBody String body) {
        try {
            switch (body) {
                case "night-mode":
                    return deviceManager.nightModeHandler.toggleNightMode();
                case "screen":
                    return deviceManager.deviceCommander.toggleDoorScreen();
                case "door":
                    return !deviceManager.deviceCommander.toggleDevice(DeviceType.DOOR);
                default:
                    return deviceManager.deviceCommander.toggleDevice(DeviceType.valueOf(body.toUpperCase()));
            }

        } catch (IllegalArgumentException ex) {
            log.error("Post mapping for device {} failed", body);
            return false;
        }
    }
}
