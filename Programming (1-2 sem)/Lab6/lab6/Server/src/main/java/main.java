
import Commands.Add;
import Commands.*;
import Managers.CollectionManager;
import Managers.CommandManager;
import Managers.DumpManager;
import Utility.StandardConsole;


import static Utility.ConnectionStarter.run;
import static Utility.Path.getJsonPath;


class main {
    public static void main(String[] args) {
        var console = new StandardConsole();
        var dumpManager = new DumpManager(getJsonPath(), console);
        final var collectionManager = new CollectionManager(dumpManager);

        var commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("add", new Add(collectionManager));
            register("info", new Info(collectionManager));
            register("show", new Show(collectionManager));
            register("remove_by_id", new RemoveById(console, collectionManager));
            register("clear", new Clear(collectionManager));
            register("remove_first", new RemoveFirst(collectionManager));
            register("sort", new Sort(console, collectionManager));
            register("max_by_name", new MaxByName(collectionManager));
            register("filter_contains_name", new FilterContainsName(console, collectionManager));
            register("print_field_ascending_distance", new PrintFieldAscendingDistance(collectionManager));
            register("update", new UpdateId(collectionManager));
            register("exit", new Exit(collectionManager));
        }};
        commandManager.register("history", new History(commandManager, console));
        run(1234, commandManager, console);
    }
}
