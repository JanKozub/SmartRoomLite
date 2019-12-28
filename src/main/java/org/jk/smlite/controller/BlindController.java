package org.jk.smlite.controller;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.DeviceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/blind**")
@CrossOrigin(origins = "${angular.server.ip}")
public class BlindController {
    private static final Logger log = LoggerFactory.getLogger(BlindController.class);
    private final DeviceManager deviceManager;

    public BlindController(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @GetMapping("/getPosition")
    public int getBlind1Position(HttpServletRequest request) {
        String[] url = request.getRequestURI().split("/");
        String device = url[url.length - 2].toUpperCase();

        try {
            return deviceManager.getState(DeviceType.valueOf(device)).getValue();
        } catch (NullPointerException ex) {
            log.error(device + " is not connected");
            return 0;
        }
    }

    @PostMapping("/setPosition")
    public boolean setBlind1Position(@Valid @RequestBody String value) {
        return deviceManager.setBlind(DeviceType.BLIND1, value);
    }
}
