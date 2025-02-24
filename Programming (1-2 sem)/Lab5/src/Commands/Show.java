package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;

public class Show extends Command {

    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public Show(CollectionManager collectionManager, StandardConsole console) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    public ExecutionResponse execute() {}
}
