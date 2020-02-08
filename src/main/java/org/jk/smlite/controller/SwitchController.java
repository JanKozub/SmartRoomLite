package org.jk.smlite.controller;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.device.DeviceCommander;
import org.jk.smlite.services.device.DeviceManager;
import org.jk.smlite.services.device.NightModeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/switch")
@CrossOrigin(origins = "${angular.server.ip}")
public class SwitchController {
    private static final Logger log = LoggerFactory.getLogger(SwitchController.class);
    private final DeviceCommander deviceCommander;
    private final NightModeHandler nightModeHandler;

    public SwitchController(DeviceManager deviceManager) {
        deviceCommander = deviceManager.getDeviceCommander();
        nightModeHandler = deviceManager.getNightModeHandler();
    }

    @PostMapping("/setState")
    public boolean setState(@Valid @RequestBody String body) {
        try {
            switch (body) {
                case "night-mode":
                    return nightModeHandler.toggleNightMode();
                case "screen":
                    return deviceCommander.toggleDoorScreen();
                case "door":
                    return !deviceCommander.toggleDevice(DeviceType.DOOR);
                default:
                    return deviceCommander.toggleDevice(DeviceType.valueOf(body.toUpperCase()));
            }

        } catch (IllegalArgumentException ex) {
            log.error("Post mapping for device {} failed", body);
            return false;
        }
    }
}
