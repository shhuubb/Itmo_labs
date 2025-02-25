package Commands;

import Client.StandardConsole;
import Utility.ExecutionResponse;
/**
 * Команда exit: завершает программу (без сохранения в файл).
 *
 * @author sh_ub
 */
public class Exit extends Command {
    private final StandardConsole console;

    public Exit(StandardConsole console) {
        super("exit ", "завершить программу (без сохранения в файл)");
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if (arg.isEmpty()) return new ExecutionResponse("Illegal number of arguments!", false);

        return new ExecutionResponse("exit", true);

    }
}
