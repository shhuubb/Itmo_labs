package Commands;
import Client.StandardConsole;

import Managers.CommandManager;

import Utility.ExecutionResponse;

import java.util.stream.Collectors;

public class Help extends Command {

    private final StandardConsole console;
    private final CommandManager commandmanager;


    public Help(StandardConsole console, CommandManager commandmanager) {
        super("help ", "вывести справку по доступным командам");
        this.console = console;
        this.commandmanager = commandmanager ;
    }
    @Override
    public ExecutionResponse execute(String arg) {
            String[] args = arg.split(" ");
            if (args.length > 0 && !args[1].isEmpty()) return new ExecutionResponse("Illegal number of arguments!",false);

            return new ExecutionResponse(commandmanager.getCommands().values().stream()
                    .map(command -> console.printTable(command.getName(), command.getDescription()))
                    .collect(Collectors.joining("\n")), true);

    }
}

