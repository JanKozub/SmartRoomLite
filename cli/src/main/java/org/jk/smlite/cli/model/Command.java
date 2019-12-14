package org.jk.smlite.cli.model;

import org.apache.commons.cli.Options;

public interface Command {

    String getName();

    String getDescription();

    Options getOptions();

    void execute(Context context) throws Exception;
}
