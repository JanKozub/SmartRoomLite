package org.jk.smlite.controller;

import org.jk.smlite.services.device.DeviceManager;
import org.jk.smlite.services.device.DeviceType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${angular.server.ip}")
@RestController
@RequestMapping("/blind1")
public class Blind1Controller {

    private final DeviceManager deviceManager;

    public Blind1Controller(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @GetMapping("/getState")
    public int getWin1State() {
        return deviceManager.getState(DeviceType.BLIND1).getValue();
    }

    @PostMapping("/setState")
    public int setWin1State(@Valid @RequestBody String value) {
        return deviceManager.setBlind(DeviceType.BLIND1, value);
    }
}
