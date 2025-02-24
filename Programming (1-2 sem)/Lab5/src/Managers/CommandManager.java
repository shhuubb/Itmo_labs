package Managers;

import Commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommandManager {

    private static HashMap<String, Command> commands = new LinkedHashMap<>();
    private static ArrayList<String> commandHistory = new ArrayList<>();

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public void addToHistory(String command) {
        commandHistory.add(command);
    }

    public ArrayList<String> getCommandHistory() {
        return commandHistory;
    }
}
