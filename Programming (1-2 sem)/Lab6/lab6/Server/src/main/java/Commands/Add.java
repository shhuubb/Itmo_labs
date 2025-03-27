package Commands;

import Managers.CollectionManager;
import Utility.ExecutionResponse;
import Utility.Command;
import model.Route;

/**
 * Команда add {element}: добавляет новый элемент в коллекцию.
 * {element} - Элемент, который нужно добавить в коллекцию.
 * @author sh_ub
 */
public class Add extends Command{
    private CollectionManager collectionManager;

    public Add(CollectionManager collectionManager)   {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(Object arg) {
        Route a = (Route) arg;
        a.setId(collectionManager.getCurrentId());
        try{
            if (a.validate()){
                collectionManager.add(a);
                return new ExecutionResponse("Object Route is successfully added!!", true);
            }
            else return new ExecutionResponse("Object Route is not valid.", false);

        } catch (NullPointerException e){
            return new ExecutionResponse("Illegal arguments!", false);
        }
    }
}
