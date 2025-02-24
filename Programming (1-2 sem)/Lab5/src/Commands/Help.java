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

            if (!arg.trim().isEmpty()) return new ExecutionResponse("Illegal number of arguments!",false);

            console.print(commandmanager.getCommands().values().stream().map(command -> console.printTable(command.getName(), command.getDescription())).collect(Collectors.joining("\n")));
            return new ExecutionResponse("", true);

    }
}

