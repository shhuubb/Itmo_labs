package Commands;


import Managers.CollectionManager;
import Utility.Command;
import Utility.ExecutionResponse;
import Utility.StandardConsole;
import model.Route;

import java.util.ArrayList;
import java.util.Vector;
/**
 * Команда filter_contains_name name: выводит элементы, значение поля name которых содержит заданную подстроку.
 * Name Подстрока, которую нужно искать в поле name.
 * @author sh_ub
 */
public class FilterContainsName extends Command {
    private final CollectionManager collectionManager;
    private final StandardConsole console;

    public FilterContainsName(StandardConsole console, CollectionManager collectionManager) {
        super("filter_contains_name name", "вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public ExecutionResponse execute(Object arg) {
        String args = new String(arg.toString());
        if (args.isEmpty())
            return new ExecutionResponse("Substring is empty", false);

        ArrayList<Route> list = GettingMatches(collectionManager.getCollection(), args);

        for (var match : list)
            console.println(match.toString());

        return new ExecutionResponse(list.size() + " matches found.", true);
    }

    /**
     * Фильтрация значений
     * @param list коллекция элементов
     * @param substring подстрока
     * @return ArrayList<Route>
     */
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
