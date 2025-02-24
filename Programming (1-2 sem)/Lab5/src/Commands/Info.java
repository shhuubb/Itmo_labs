package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;

import java.time.LocalDateTime;


public class Info extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public Info(StandardConsole console, CollectionManager collectionManager) {
        super("info", "Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if(!arg.isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }

        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();


        var r = """
                Information about collection: Type, lastInitTime, lastSaveTime and count of elements:
                Type: %s,
                LastInitTime: %s
                LastSaveTime: %s
                CountOfElements: %d
            """.formatted(collectionManager.getCollection().getClass(),
                lastInitTime==null ? "The collection has not been updated in this session yet.": lastInitTime,
                lastSaveTime==null ? "The collection has not been saved in this session yet.": lastSaveTime,
                collectionManager.getCollection().size());

        console.print(r);
        return new ExecutionResponse("Successful getting info!", true);
    }
}
