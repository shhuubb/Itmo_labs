package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.AskBreak;
import Utility.ExecutionResponse;
import model.Route;

import static Utility.ParseFileRoute.ParseRoute;
import static model.Ask.AskRoute;


public class UpdateId extends Command {
    private final StandardConsole console;
    private final CollectionManager collectionManager;

    public UpdateId(StandardConsole console, CollectionManager collectionManager) {
        super("update id {element}  ", "обновить значения элементов коллекции, id которого равны заданным");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override

    public ExecutionResponse execute(String arg) {
        try {
            String[] args = arg.split(" ");

            if (args.length > 1) console.printError("Illegal number of arguments!");


            console.println("Update a Route with id: " + args[0]);

            Route a;

            if (!(args.length == 1 || (args.length == 2 && args[1].trim().charAt(0) == '{'))) {
                console.printError("Illegal number of arguments!");
                return new ExecutionResponse("Illegal number of arguments!", false);

            } else if (args[1].trim().charAt(0) == '{') {

                try {
                    a = ParseRoute(console, args[1], collectionManager.getCurrentId());
                } catch (NullPointerException e) {
                    console.printError("Illegal argument!");
                    return new ExecutionResponse("Illegal argument!", false);
                }
            } else {
                console.println("Add a Route:");
                a = AskRoute(console, collectionManager.getCurrentId());
            }
            Long id = Long.parseLong(args[0].trim());

            if (!collectionManager.update(a)) {
                console.printError("The route with this id was not found.");
                return new ExecutionResponse("The route with this id was not found.", false);
            }

            } catch(AskBreak e){
                return new ExecutionResponse("Cancel", false);
        }
        return new ExecutionResponse("Successfully updated the route.", true);
    }
}


