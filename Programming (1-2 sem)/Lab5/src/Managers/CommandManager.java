package Managers;

import Commands.Command;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommandManager {
    private static HashMap<String, Command> commands = new LinkedHashMap<>();

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }
}
