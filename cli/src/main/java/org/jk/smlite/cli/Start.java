package org.jk.smlite.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.jk.smlite.cli.command.*;
import org.jk.smlite.cli.model.Command;
import org.jk.smlite.cli.model.Context;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class Start {
    // https://github.com/jline/jline3
    // https://commons.apache.org/proper/commons-cli/

    public static final String MY_URL = "http://localhost:8080";

    public static void main(String[] args) throws IOException {
        new Start().run(args);
    }


    private List<Command> commands = List.of(
            new BlindCommand(),
            new DoorCommand(),
            new LightCommand(),
            new ClockCommand(),
            new SwitchCommand(),
            new GetCommand()
    );

    private Start() {
    }

    private void run(String[] args) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out))) {

            while (true) {
                out.write("smlite>");
                out.flush();
                String consoleRead = in.readLine();

                if (consoleRead.equals("exit"))
                    break;

                try {
                    validateSyntax(out, consoleRead);
                } catch (SyntaxException ex) {
                    out.write(consoleRead + ": " + ex.getMessage());
                    out.newLine();
                } catch (Exception ex) {
                    out.write("Command error: " + ex.getMessage());
                    out.newLine();
                }
                out.flush();
            }
        }
    }


    private void validateSyntax(BufferedWriter out, String command) throws Exception {
        List<String> cmdBody = List.of(command.split(" "));

        Optional<Command> tgtCmd = commands.stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(cmdBody.get(0)))
                .findAny();

        if (tgtCmd.isEmpty()) throw new SyntaxException("Command not found");

        Command c = tgtCmd.get();

        CommandLine cl = new DefaultParser().parse(c.getOptions(), cmdBody.subList(1, cmdBody.size()).toArray(new String[0]));

        tgtCmd.get().execute(new Context(out, cmdBody.get(0), cl, MY_URL));
    }
}
