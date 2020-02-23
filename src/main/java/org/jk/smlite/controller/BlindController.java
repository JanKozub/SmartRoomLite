package org.jk.smlite.controller;

import org.jk.smlite.model.device.DeviceType;
import org.jk.smlite.services.device.DeviceCommander;
import org.jk.smlite.services.device.DeviceManager;
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
    private final DeviceCommander deviceCommander;

    public BlindController(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
        this.deviceCommander = deviceManager.getDeviceCommander();
    }

    @GetMapping("/getPosition/**")
    public String getBlind1Position(HttpServletRequest request) {
        String[] url = request.getRequestURI().split("/");
        String device = url[url.length - 1].toUpperCase();

        try {
            return deviceManager.getDeviceState(DeviceType.valueOf(device)).getData(0);
        } catch (NullPointerException ex) {
            log.error(device + " is not connected");
            return "0";
        }
    }

    @PostMapping("/setPosition/**")
    public boolean setBlindPosition(@Valid @RequestBody String value, HttpServletRequest request) {
        String[] url = request.getRequestURI().split("/");
        String device = url[url.length - 1].toUpperCase();

        if (DeviceType.valueOf(device) != DeviceType.BLIND1 && DeviceType.valueOf(device) != DeviceType.BLIND2) {
            log.info("WRONG MAPPING ON /setPosition");
            return false;
        }

        return deviceCommander.setBlind(DeviceType.valueOf(device), value);
    }
}
