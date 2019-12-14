package org.jk.smlite.cli.model;

import org.apache.commons.cli.CommandLine;

import java.io.BufferedWriter;

public class Context {
    private final BufferedWriter out;
    private final String name;
    private final CommandLine cmdLine;
    private final String url;

    public Context(BufferedWriter out, String name, CommandLine cmdLine, String url) {
        this.out = out;
        this.name = name;
        this.cmdLine = cmdLine;
        this.url = url;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public String getName() {
        return name;
    }

    public CommandLine getCmdLine() {
        return cmdLine;
    }

    public String getUrl() {
        return url;
    }
}
