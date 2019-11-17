package org.jk.smlite.model;

import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface Switch {
    boolean getState();

    boolean setState(@Valid @RequestBody String body);
}
