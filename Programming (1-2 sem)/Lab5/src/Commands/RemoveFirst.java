package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;

/**
 * Команда remove_first: удаляет первый элемент из коллекции.
 * @author sh_ub
 */
public class RemoveFirst extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public RemoveFirst(StandardConsole console, CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if(!arg.isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }

        var id = collectionManager.getCollection().get(0).getId();

        if(!collectionManager.remove(id)){
            console.println("Remove a Route by Id: " + id);
            return new ExecutionResponse("Element with this Id is not found", false);
        }
        return new ExecutionResponse("Successful delete!", true);
    }
}
