package restx.shell.commands;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import jline.console.ConsoleReader;
import restx.shell.ShellCommand;
import restx.shell.ShellCommandMatch;
import restx.shell.StdShellCommand;

import java.io.IOException;
import java.util.Set;

/**
 * User: xavierhanin
 * Date: 4/9/13
 * Time: 10:11 PM
 */
public class HelpCommand extends StdShellCommand {

    private final Set<ShellCommand> commands;

    public HelpCommand(Set<ShellCommand> commands) {
        super(ImmutableList.of("help", "man"), "provides list of commands or a command manual");
        this.commands = commands;
    }

    @Override
    public boolean run(ConsoleReader reader, ShellCommandMatch match) throws IOException {
        ImmutableList<String> args = ImmutableList.copyOf(Splitter.on(' ').omitEmptyStrings().split(match.getLine()));

        if (args.size() > 1) {
            man(reader, args.get(1));
        } else {
            for (ShellCommand command : commands) {
                command.help(reader);
            }
            reader.println("");
            reader.println("use `help <command>` with any of these commands to get a detailed man on the command");
        }
        return false;
    }

    private void man(ConsoleReader reader, String command) throws IOException {
        for (ShellCommand shellCommand : commands) {
            if (shellCommand.getAliases().contains(command)) {
                shellCommand.man(reader);
                return;
            }
        }

        if (getAliases().contains(command)) {
            man(reader);
        } else {
            reader.println("command not found: `" + command + "`. use `help` to get the list of available commands.");
        }
    }
}