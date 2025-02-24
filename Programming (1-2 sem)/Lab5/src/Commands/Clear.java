package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;

public class Clear extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;


    public Clear(CollectionManager collectionManager, StandardConsole console) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if (!arg.isEmpty()){
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        collectionManager.clear();
        return new ExecutionResponse("Successfully cleared collection!", true);
    }
}
