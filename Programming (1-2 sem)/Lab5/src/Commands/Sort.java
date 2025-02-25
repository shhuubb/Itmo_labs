package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;
/**
 * Команда sort: сортирует коллекцию в естественном порядке.
 * @author sh_ub
 */
public class Sort extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public Sort(StandardConsole console, CollectionManager collectionManager) {
        super("sort", "отсортировать коллекцию в естественном порядке");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if(!arg.isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }

        collectionManager.sort();
        return new ExecutionResponse("Successfully sorted collection", true);
    }
}
