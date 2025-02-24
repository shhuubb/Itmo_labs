package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.AskBreak;
import Utility.ExecutionResponse;
import model.Route;

import static model.Ask.AskRoute;


public class Add extends Command{
    private final StandardConsole console;
    private final CollectionManager collectionManager;

    public Add(StandardConsole console, CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String arg) {
        try{
            if(!arg.isEmpty()){
                console.printError("Illegal number of arguments!");
                return new ExecutionResponse("Illegal number of arguments!", false);
            }
            console.println("Add a Route:");

            Route a = AskRoute(console, collectionManager.getCurrentId());

            if (a.validate()){
                collectionManager.add(a);
                return new ExecutionResponse("Object Route is successfully added!!", true);
            }
            else {
                console.println(a.toString());
                return new ExecutionResponse("Object Route is not valid.", false);
            }
        } catch (AskBreak e){
            return new ExecutionResponse("Cancel", false);
        }

    }
}
