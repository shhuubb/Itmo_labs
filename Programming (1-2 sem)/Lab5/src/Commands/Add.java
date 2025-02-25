package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.AskBreak;
import Utility.ExecutionResponse;
import Utility.ParseFileRoute;
import model.Route;

import static Utility.ParseFileRoute.ParseRoute;
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
        Route a;
        try{
            if(!arg.isEmpty() && !(arg.trim().charAt(0) == '{')){
                console.printError("Illegal number of arguments!");
                return new ExecutionResponse("Illegal number of arguments!", false);
            }
            else if (arg.trim().charAt(0) == '{'){
                try{
                    a = ParseRoute(console, arg, collectionManager.getCurrentId());
                }catch (NullPointerException e){
                    console.printError("Illegal argument!");
                    return new ExecutionResponse("Illegal argument!", false);
                }
            }
            else{
                console.println("Add a Route:");
                a = AskRoute(console, collectionManager.getCurrentId());
            }

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
