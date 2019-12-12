package org.jk.smlite.cli.command;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public abstract class AbstractCommand implements Command {
    protected static final Option REPEAT = Option.builder("r").longOpt("repeat").hasArg().build();

    private final String name;
    private final String description;

    protected AbstractCommand(String name) {
        this(name, name + " command");
    }

    protected AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Options getOptions() {
        return new Options();
    }

    @Override
    public void execute(Context context) throws  Exception {
        throw new UnsupportedOperationException("Command not supported");
    }

    protected String execute(String url) {
        try {
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            InputStream input = connection.getInputStream();
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void repeatIfPresent(CommandLine cl, Runnable r) {
        if(cl.hasOption(REPEAT.getOpt())) {
            int cnt = Integer.parseInt(cl.getOptionValue(REPEAT.getOpt()));
            for(int i = 0; i < cnt; i++)
                r.run();
        } else
            r.run();
    }
}
