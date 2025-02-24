package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;

public class Save extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;


    public Save(CollectionManager collectionManager, StandardConsole console) {
        super("save", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if (!arg.isEmpty()){
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        collectionManager.saveCollection();

        return new ExecutionResponse("Successfully saved collection!", true);
    }
}
