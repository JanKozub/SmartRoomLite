package org.jk.smlite.cli.command;

import org.apache.commons.cli.CommandLine;

import java.io.BufferedWriter;
import java.util.List;

public class Context {
    private final BufferedWriter out;
    private final CommandLine cmdLine;
    private final String url;

    public Context(BufferedWriter out, CommandLine commandLine, String url) {
        this.out = out;
        this.cmdLine = commandLine;
        this.url = url;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public CommandLine getCmdLine() {
        return cmdLine;
    }

    public String getUrl() {
        return url;
    }
}
