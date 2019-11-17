package org.jk.smlite.controller.blinds;

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

    @GetMapping("/getPosition")
    public int getBlind1Position() {
        return deviceManager.getState(DeviceType.BLIND1).getValue();
    }

    @PostMapping("/setPosition")
    public boolean setBlind1Position(@Valid @RequestBody String value) {
        return deviceManager.setBlind(DeviceType.BLIND1, value) != -1;
    }
}
