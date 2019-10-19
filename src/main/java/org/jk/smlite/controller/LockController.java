package org.jk.smlite.controller;

import org.jk.smlite.services.Switch;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${angular.server.ip}")
@RestController
@RequestMapping("/lock")
public class LockController implements Switch {
    private boolean state = true;

    @Override
    @GetMapping("/state")
    public boolean getState() {
        return state;
    }

    @Override
    @PostMapping("")
    public void setState(@Valid @RequestBody String body) {
        if (body.equals("CHANGE")) {
            state = !state;
        }
    }
}
