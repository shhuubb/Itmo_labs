package Commands;

import Managers.CollectionManager;
import Utility.Command;
import Utility.ExecutionResponse;

/**
 * Команда show: выводит все элементы коллекции в строковом представлении.
 * @author sh_ub
 */
public class Show extends Command {

    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(Object arg) {

        if (arg != null)
            return new ExecutionResponse("Illegal number of arguments!", false);

        return new ExecutionResponse(collectionManager.toString(), true);
    }
}
