package org.jk.smlite.cli.command;

import org.jk.smlite.cli.model.AbstractCommand;
import org.jk.smlite.cli.model.Context;

public class SwitchCommand extends AbstractCommand {

    public SwitchCommand() {
        super("toggle");
    }

    @Override
    public void execute(Context context) {
        executeSwitch(context);
    }
}
