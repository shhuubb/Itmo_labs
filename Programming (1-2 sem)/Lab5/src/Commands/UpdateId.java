package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.AskBreak;
import Utility.ExecutionResponse;
import model.Route;

import static model.Ask.AskRoute;


public class UpdateId extends Command{
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

            if (args.length < 1) console.printError("Illegal number of arguments!");


            for (String s : args) {
                console.println("Update a Route with id: " + s);
                Route a = AskRoute(console, Long.parseLong(s));
                if (s.equals("exit")) {
                    throw new AskBreak();
                }

                if (!collectionManager.update(a)) {
                    console.printError("The route with this id was not found.");
                    return new ExecutionResponse("The route with this id was not found.", false);
                }
            }
            return new ExecutionResponse("Successfully updated", true);

        } catch (AskBreak e){
            return new ExecutionResponse("Cancel", false);
        }
    }
}

