package Commands;

import Managers.CollectionManager;
import Utility.ExecutionResponse;
import model.Route;
import Utility.Command;
/**
 * Команда update id {element}: обновляет значение элемента коллекции, id которого равен заданному.
 * Id - Идентификатор элемента, который нужно обновить.
 * {element} - Новое значение элемента.
 * @author sh_ub
 */
public class UpdateId extends Command {
    private final CollectionManager collectionManager;

    public UpdateId(CollectionManager collectionManager) {
        super("update id {element}", "обновить значения элементов коллекции, id которого равны заданным");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(Object arg) {
        Route a = (Route) arg;
        if (!collectionManager.update(a))
            return new ExecutionResponse("The route with this id was not found.", false);
        return new ExecutionResponse("Route was successfully updated", true);
    }
}


