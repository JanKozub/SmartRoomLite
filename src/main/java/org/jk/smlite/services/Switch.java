package org.jk.smlite.services;

import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface Switch {
    boolean getState();

    void setState(@Valid @RequestBody String body);
}
