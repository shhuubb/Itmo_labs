package Commands;

import Utility.Command;
import Utility.ExecutionResponse;
import Utility.StandardConsole;

/**
 * Команда save: сохраняет коллекцию в файл.
 * @author sh_ub
 */
public class Exit extends Command {
    StandardConsole console;
    public Exit(StandardConsole console) {
        super("exit", "выйти из приложения");
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(Object arg) {
        if (arg != null){
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        console.println("Bye-Bye!!!");
        System.exit(0);
        return new ExecutionResponse("Collection successfully saved!", true);
    }
}
