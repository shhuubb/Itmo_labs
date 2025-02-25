package Commands;

import Client.StandardConsole;
import Managers.CommandManager;
import Utility.ExecutionResponse;

import java.util.ArrayList;
/**
 * Команда history: выводит последние 7 команд (без их аргументов).
 * @author sh_ub
 */
public class History extends Command {
    CommandManager commandmanager = new CommandManager();
    StandardConsole console = new StandardConsole();

    public History(CommandManager commandmanager, StandardConsole console) {
        super("history", "вывести последние 7 команд (без их аргументов)");
        this.commandmanager = commandmanager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if (!arg.trim().isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        var list = commandmanager.getCommandHistory();

        console.println(GettingHistory(list));
        return new ExecutionResponse("Successful output of history", true);
    }
    public String GettingHistory(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        int leftpointer = list.size()>=7? list.size()-7 : 0 ;
        int rightpointer = list.size();
        for (; leftpointer < rightpointer; rightpointer--) {
            sb.append(list.get(rightpointer-1)).append("\n");
        }
        return sb.toString();
    }
}
