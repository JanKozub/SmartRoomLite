package org.jk.smlite.cli.command;

import org.jk.smlite.cli.model.AbstractCommand;
import org.jk.smlite.cli.model.Context;

public class ClockCommand extends AbstractCommand {

    public ClockCommand() {
        super("Clock");
    }

    @Override
    public void execute(Context context) {
        executeSwitch(context);
    }
}
