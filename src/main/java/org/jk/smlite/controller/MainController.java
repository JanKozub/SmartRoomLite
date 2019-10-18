package org.jk.smlite.controller;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/test")
public class MainController {

    @PostMapping("/state")
    public void getState(@Valid @RequestBody String state) {
        System.out.println(state);
    }
}
