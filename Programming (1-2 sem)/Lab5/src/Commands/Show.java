package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;

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
        String[] args = arg.split(" ");
        if (!args[1].isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        return new ExecutionResponse(collectionManager.toString(), true);
    }
}
