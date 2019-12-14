package org.jk.smlite.cli.command;

import org.apache.commons.cli.Options;
import org.jk.smlite.cli.model.AbstractCommand;
import org.jk.smlite.cli.model.Context;

public class DoorCommand extends AbstractCommand {
    private static final Options OPTIONS = new Options()
            .addOption(REPEAT);

    public DoorCommand() {
        super("Door");
    }

    @Override
    public Options getOptions() {
        return OPTIONS;
    }

    @Override
    public void execute(Context context) {
        executeSwitch(context);
    }
}
