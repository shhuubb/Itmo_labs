package Commands;


import Managers.CollectionManager;
import Utility.Command;
import Utility.ExecutionResponse;

import model.Route;
import Command.CommandWithArgs;

import java.util.ArrayList;
import java.util.Vector;
/**
 * Команда filter_contains_name name: выводит элементы, значение поля name которых содержит заданную подстроку.
 * Name Подстрока, которую нужно искать в поле name.
 * @author sh_ub
 */
public class FilterContainsName extends Command {
    private final CollectionManager collectionManager;

    public FilterContainsName(CollectionManager collectionManager) {
        super("filter_contains_name name", "вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(CommandWithArgs command) {
        String substring = command.getArgs();
        if (substring.isEmpty())
            return new ExecutionResponse("Substring is empty", false);

        ArrayList<Route> list = GettingMatches(collectionManager.getCollection(), substring);

        StringBuilder answer = new StringBuilder();

        for (var match : list)
            answer.append(match.toString()).append("\n");

        answer.append(list.size()).append(" matches found.");

        return new ExecutionResponse(answer.toString(), true);
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
