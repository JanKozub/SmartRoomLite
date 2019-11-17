package org.jk.smlite.controller.blinds;

import org.jk.smlite.services.device.DeviceManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${angular.server.ip}")
@RestController
@RequestMapping("/blind2")
public class Blind2Controller {

    private final DeviceManager deviceManager;

    public Blind2Controller(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @GetMapping("/getPosition")
    public int getBlind2Position() {
        //TODO this
        return 0;
        //return deviceManager.getState(DeviceType.BLIND2).getValue();
    }

    @PostMapping("/setPosition")
    public boolean setBlind2Position(@Valid @RequestBody String value) {
        return true;
//        return deviceManager.setBlind(DeviceType.BLIND2, value) != -1;
    }
}
