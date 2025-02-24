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
                try{

                    Long id = Long.parseLong(s.trim());
                    Route a = AskRoute(console, id);

                    if (!collectionManager.update(a)) {
                        console.printError("The route with this id was not found.");
                        return new ExecutionResponse("The route with this id was not found.", false);
                    }

                } catch(NumberFormatException nfe){
                    console.printError("Illegal argument!");
                    return new ExecutionResponse( "Id isn't defined!", false);

                }


            }
            return new ExecutionResponse("Successfully updated", true);

        } catch (AskBreak e){
            return new ExecutionResponse("Cancel", false);
        }
    }
}

