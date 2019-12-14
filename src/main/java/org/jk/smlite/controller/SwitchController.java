package org.jk.smlite.controller;

import org.jk.smlite.services.device.DeviceManager;
import org.jk.smlite.services.device.DeviceType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/switch")
public class SwitchController {
    private DeviceManager deviceManager;

    public SwitchController(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @GetMapping("/getState/**")
    public boolean getState(HttpServletRequest request) {
        String urlArray[] = request.getRequestURI().split("/");
        String device = urlArray[urlArray.length - 1];

        if (device.equals("light")) {
            return deviceManager.getState(DeviceType.LIGHT).isEnabled();
        }
        if (device.equals("clock")) {
            return deviceManager.getState(DeviceType.CLOCK).isEnabled();
        }
        if (device.equals("door")) {
            return deviceManager.getState(DeviceType.CLOCK).isEnabled();
        }
        return false;
    }

    @PostMapping("/setState")
    public boolean setState(@Valid @RequestBody String body) {
        if (body.equals("light")) {
            return deviceManager.toggleDevice(DeviceType.LIGHT);
        } else {
            if (body.equals("clock")) {
                return deviceManager.toggleDevice(DeviceType.CLOCK);
            } else {
                if (body.equals("door")) {
                    return deviceManager.toggleDevice(DeviceType.DOOR);
                }
            }
        } //TODO PARSE
        return false;
    }
}
