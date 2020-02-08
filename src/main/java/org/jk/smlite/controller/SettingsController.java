package org.jk.smlite.controller;

import org.jk.smlite.services.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/settings")
@CrossOrigin(origins = "${angular.server.ip}")
public class SettingsController {
    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);
    private Configuration configuration;

    public SettingsController(Configuration configuration) {
        this.configuration = configuration;
    }

    private static String getLastElementOfUrl(HttpServletRequest request) {
        String[] urlArray = request.getRequestURI().split("/");
        return urlArray[urlArray.length - 1];
    }

    @PostMapping("/setProperty/**")
    public boolean setProperty(@Valid @RequestBody String body, HttpServletRequest request) {
        String property = getLastElementOfUrl(request);
        try {
            configuration.setProperty(property, body);
            return true;
        } catch (IllegalArgumentException ex) {
            log.error("Post mapping for device {} failed", body);
            return false;
        }
    }
}
