package org.jk.smlite.controller;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.DeviceManager;
import org.jk.smlite.services.UrlHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/thermometer**")
@CrossOrigin(origins = "${angular.server.ip}")
public class ThermometerController {
    private static final Logger log = LoggerFactory.getLogger(SwitchController.class);
    private DeviceManager deviceManager;

    public ThermometerController(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @GetMapping("/getProp/**")
    public String getTemperature(HttpServletRequest request) {
        String prop = UrlHandler.getLastElementOfUrl(request);
        try {
            if (prop.equals("getTemperature")) {
                return "{ \"value\": \"" + deviceManager.getState(DeviceType.THERMOMETER).getData()[1] + "\" }";
            } else {
                return "{ \"value\": \"" + deviceManager.getState(DeviceType.THERMOMETER).getData()[2] + "\" }";
            }
        } catch (NullPointerException | IllegalArgumentException ex) {
            log.error("GetMapping failed for switch {}", prop);
            return "";
        }
    }

    @PostMapping("/toggle")
    public boolean setState(@Valid @RequestBody String body) {
        try {
            return deviceManager.toggleDevice(DeviceType.THERMOMETER);
        } catch (IllegalArgumentException ex) {
            log.error("Post mapping for device {} failed", body);
            return false;
        }
    }
}
