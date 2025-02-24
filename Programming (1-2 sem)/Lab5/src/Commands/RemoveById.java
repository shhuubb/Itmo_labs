package Commands;

import Client.StandardConsole;
import Managers.CollectionManager;
import Utility.AskBreak;
import Utility.ExecutionResponse;



public class RemoveById extends Command{
    private final StandardConsole console;
    private final CollectionManager collectionManager;

    public RemoveById(StandardConsole console, CollectionManager collectionManager) {
        super("remove_by_id id ", "удалить элемент из коллекции по его id");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public ExecutionResponse execute(String arg) {
        try{
            if(arg.split(" ").length != 1){
                console.printError("Illegal number of arguments!");
            }
            console.println("Remove a Route by Id: ");

            Long id = Long.parseLong(arg);

            if(arg.equals("exit")){
                throw new AskBreak();
            }

            if(collectionManager.remove(id)){
                return new ExecutionResponse("Successfully removed!", true);
            } else{
                console.printError("The route with this id was not found.");
                return new ExecutionResponse("The route with this id was not found.", true);
            }
        } catch (AskBreak e){
            return new ExecutionResponse("Cancel", false);
        }
    }
}
