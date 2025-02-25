package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.AskBreak;
import Utility.ExecutionResponse;
import model.Route;

import static Utility.ParseFileRoute.ParseRoute;
import static model.Ask.AskRoute;

/**
 * Команда add {element}: добавляет новый элемент в коллекцию.
 * {element} - Элемент, который нужно добавить в коллекцию.
 * @author sh_ub
 */
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
        Route a;
        try{
            if(!arg.isEmpty() && !(arg.trim().charAt(0) == '{')){
                console.printError("Illegal number of arguments!");
                return new ExecutionResponse("Illegal number of arguments!", false);
            }
            else if (arg.isEmpty()){
                console.println("Add a Route:");
                a = AskRoute(console, collectionManager.getCurrentId());
            }
            else {
                try{
                    a = ParseRoute(console, arg, collectionManager.getCurrentId());
                }catch (NullPointerException e){
                    console.printError("Illegal argument!");
                    return new ExecutionResponse("Illegal argument!", false);
                }
            }

            if (a != null) {
                if (a.validate()){
                    collectionManager.add(a);
                    return new ExecutionResponse("Object Route is successfully added!!", true);
                }
                else {
                    console.printError("Illegal argument!");
                    return new ExecutionResponse("Object Route is not valid.", false);
                }
            }
            else {
                console.printError("Illegal argument!");
                return new ExecutionResponse("Illegal argument!", false);
            }



        } catch (AskBreak e){
            return new ExecutionResponse("Cancel", false);
        }

    }
}
