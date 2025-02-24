import Client.StandardConsole;
import Commands.*;
import Managers.CollectionManager;
import Managers.CommandManager;
import Managers.DumpManager;
import Utility.AskBreak;

import model.Coordinates;
import model.Location;
import model.Route;

public class Main {

    public static void main(String[] args) throws AskBreak {
        StandardConsole console = new StandardConsole();
        CollectionManager collectionManager = new CollectionManager(new DumpManager("Json.json",console));
        CommandManager commandManager = new CommandManager();
        Help help = new Help(console, commandManager);
        Add add = new Add(console, collectionManager);
        commandManager.register("Add", new Add(console, collectionManager));
        commandManager.register("RemoveById", new RemoveById(console, collectionManager));
        commandManager.register("Save", new Save(console, collectionManager));
        commandManager.register("Filter", new FilterContainsName(console, collectionManager));
        FilterContainsName filter = new FilterContainsName(console, collectionManager);
        add.execute("");
        add.execute("");
        filter.execute("dick");
    }
}
