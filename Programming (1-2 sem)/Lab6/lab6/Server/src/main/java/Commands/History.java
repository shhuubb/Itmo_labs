package Commands;

import Utility.Command;
import Utility.StandardConsole;
import Managers.CommandManager;
import Utility.ExecutionResponse;
import Command.CommandWithArgs;

import java.util.ArrayList;
/**
 * Команда history: выводит последние 7 команд (без их аргументов).
 * @author sh_ub
 */
public class History extends Command {
    CommandManager commandmanager;
    StandardConsole console;

    public History(StandardConsole console, CommandManager commandmanager) {
        super("history", "вывести последние 7 команд (без их аргументов)");
        this.console = console;
        this.commandmanager = commandmanager;
    }

    @Override
    public ExecutionResponse execute(CommandWithArgs command) {

        if (command.getArgs() != null) {
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        var list = commandmanager.getCommandHistory();

        return new ExecutionResponse(GettingHistory(list), true);
    }

    /**
     * Получение истории запросов
     * @param list список запросов
     * @return последние 7 комманд
     */
    public String GettingHistory(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        int firstIndex = list.size()>=8? list.size()-8 : 0 ;
        int lastIndex = list.size()-1;
        for (; firstIndex < lastIndex; lastIndex--) {
            sb.append(list.get(lastIndex-1)).append(lastIndex-firstIndex==1 ? "" : "\n");
        }
        return sb.toString();
    }
}
