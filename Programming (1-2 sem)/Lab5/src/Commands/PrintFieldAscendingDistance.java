package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;
import model.Route;

import java.util.Comparator;
import java.util.Vector;
import java.util.stream.Collectors;

public class PrintFieldAscendingDistance extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public PrintFieldAscendingDistance(StandardConsole console, CollectionManager collectionManager) {
        super("print_field_ascending_distance", "вывести значения поля distance всех элементов в порядке возрастания");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if(!arg.isEmpty()) {
            console.printError("Illegal number of arguments!");
            return new ExecutionResponse("Illegal number of arguments!", false);
        }
        if(collectionManager.getCollection().isEmpty()) {
            console.printError("Collection is empty!");
            return new ExecutionResponse("Collection is empty!", false);
        }
        else{
            console.println(GettingDistances(collectionManager.getCollection()));
            return new ExecutionResponse("Successful execution!", true);
        }
    }

    private String GettingDistances(Vector<Route> routes) {
        return routes.stream()
                .map(Route::getDistance)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
    }
}
