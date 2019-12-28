package org.jk.smlite.controller;

import org.jk.smlite.services.device.DeviceManager;
import org.jk.smlite.services.device.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/getState/**")
    public boolean getState(HttpServletRequest request) {
        String[] urlArray = request.getRequestURI().split("/");
        String device = urlArray[urlArray.length - 1].toUpperCase();

        return deviceManager.getState(DeviceType.valueOf(device)).isEnabled();
    }

    @PostMapping("/setState")
    public boolean setState(@Valid @RequestBody String body) {
        try {
            return deviceManager.toggleDevice(DeviceType.valueOf(body.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            log.error("Post mapping for device {} failed", body);
            return false;
        }
    }
}
