package org.jk.smlite.cli.command;

import org.jk.smlite.cli.model.AbstractCommand;
import org.jk.smlite.cli.model.Context;

public class GetCommand extends AbstractCommand {

    public GetCommand() {
        super("get");
    }

    @Override
    public void execute(Context context) {
        executeGet(context);
    }

}