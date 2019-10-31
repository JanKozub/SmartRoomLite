package org.jk.smlite.controller;

import org.jk.smlite.services.Switch;
import org.jk.smlite.services.device.DeviceManager;
import org.jk.smlite.services.device.DeviceType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${angular.server.ip}")
@RestController
@RequestMapping("/clock")
public class ClockController implements Switch {
    private DeviceManager deviceManager;

    public ClockController(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @Override
    @GetMapping("/getState")
    public boolean getState() {
        return deviceManager.getState(DeviceType.CLOCK).isEnabled();
    }

    @Override
    @PostMapping("/setState")
    public boolean setState(@Valid @RequestBody String body) {
        return deviceManager.toggleDevice(DeviceType.CLOCK);
    }
}
