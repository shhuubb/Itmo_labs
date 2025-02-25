package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;
/**
 * Команда show: выводит все элементы коллекции в строковом представлении.
 *
 * @author sh_ub
 */
public class Show extends Command {

    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public Show(StandardConsole console, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        String[] args = arg.trim().split(" ");
        if (args.length > 1) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        console.println(collectionManager.toString());
        return new ExecutionResponse(collectionManager.toString(), true);
    }
}
