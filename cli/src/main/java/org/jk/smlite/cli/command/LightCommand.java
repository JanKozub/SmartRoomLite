package org.jk.smlite.cli.command;

import org.jk.smlite.cli.model.AbstractCommand;
import org.jk.smlite.cli.model.Context;

public class LightCommand extends AbstractCommand {

    public LightCommand() {
        super("Light");
    }

    @Override
    public void execute(Context context) {
        executeSwitch(context);
    }
}
