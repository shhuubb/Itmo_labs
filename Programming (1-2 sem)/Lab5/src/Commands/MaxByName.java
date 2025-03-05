package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;
import model.Route;

import java.util.Vector;

/**
 * Команда max_by_name: выводит любой объект из коллекции, значение поля name которого является максимальным.
 * @author sh_ub
 */

public class MaxByName extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public MaxByName(StandardConsole console, CollectionManager collectionManager) {
        super("max_by_name", "Вывести любой объект из коллекции, значение поля name которого является максимальным");
        this.collectionManager = collectionManager;
        this.console = console;
    }
    /**
     * Метод для получения максимального имени
     * @param collection коллекция
     * @author sh_ub
     */
    public String GettingMaxName(Vector<Route> collection){
        return collection.stream()
                .map(Route::getName)
                .max(String::compareTo)
                .orElseThrow(() -> new IllegalStateException("Collection is Empty"));
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if(!arg.isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        console.println(GettingMaxName(collectionManager.getCollection()));
        return new ExecutionResponse("Successful Print of max name", true);
    }


}
