import Client.StandardConsole;
import Managers.CollectionManager;
import Managers.DumpManager;
import Utility.AskBreak;

import model.Coordinates;
import model.Location;
import model.Route;

public class Main {

    public static void main(String[] args) throws AskBreak {
        CollectionManager collectionManager = new CollectionManager(new DumpManager("Json.json", new StandardConsole()));
        collectionManager.add(new Route(1L, "Route1", new Coordinates(2.5, 3.23f), new Location(132L, 12421421L, 234.23, "home"), new Location(12L, 11421L, 24.23, "Work"), 100));

        collectionManager.saveCollection();
    }
}
