package Commands;

import Client.StandardConsole;
import Utility.Console;
import Utility.ExecutionResponse;
/**
 * Команда execute_script file_name: считывает и исполняет скрипт из указанного файла.
 * В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 *  fileName - Имя файла, содержащего скрипт.
 * @author sh_ub
 */
public class ExecuteScript extends Command {
    private final StandardConsole console;

    public ExecuteScript(StandardConsole console) {
        super("execute_script <file_name>", "исполнить скрипт из указанного файла");
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if (arg.trim().isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }

        console.println("Выполнение скрипта '" + arg + "'...");
        return new ExecutionResponse("File is Load", true);
    }
}
