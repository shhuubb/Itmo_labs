package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.ExecutionResponse;
import model.Route;

import java.util.ArrayList;
import java.util.Vector;

public class FilterContainsName extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public FilterContainsName(StandardConsole console, CollectionManager collectionManager) {
        super("filter_contains_name name", "вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if (arg.isEmpty()) {
            console.printError("Substring is empty");
            return new ExecutionResponse("Substring is empty", false);
        }
        var list = GettingMatches(collectionManager.getCollection(), arg);
        console.println(list.size() + " matches found.");
        for (var match : list) {
            console.print(match.toString());
        }

        return new ExecutionResponse("Matches found.", true);
    }

    private ArrayList<Route> GettingMatches(Vector<Route> list, String substring) {
        ArrayList<Route> matches = new ArrayList<>();
        for (Route route : list) {
            if (route.getName().toLowerCase().contains(substring)) {
                matches.add(route);
            }
        }
        return matches;
    }
}
