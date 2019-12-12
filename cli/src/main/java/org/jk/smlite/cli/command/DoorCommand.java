package org.jk.smlite.cli.command;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.IOException;

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
    public void execute(Context context) throws Exception {
        repeatIfPresent(context.getCmdLine(), () -> {
            String response = execute(context.getUrl() + "/door/getSomething");
            try {
                context.getOut().write(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
