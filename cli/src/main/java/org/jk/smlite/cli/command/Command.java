package org.jk.smlite.cli.command;

import org.apache.commons.cli.Options;
import org.jk.smlite.cli.command.Context;

import java.io.BufferedWriter;
import java.util.List;

public interface Command {

    String getName();

    String getDescription();

    Options getOptions();

    void execute(Context context) throws Exception;
}
