package Utility;

import Managers.CommandManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Runner {
    private Console console;
    private final CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    public Runner(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }

    public void interactiveMode() {
        try {
            ExecutionResponse commandStatus;
            String[] userCommand;

            do {
                console.prompt();
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandManager.addToHistory(userCommand[0]);
                commandStatus = launchCommand(userCommand);
            } while (commandStatus.isSuccess() || !commandStatus.getResponse().equals("exit"));

        } catch (NoSuchElementException exception) {
            console.printError("Input is not defined");
        } catch (IllegalStateException exception) {
            console.printError("Error");
        }
    }

    private ExecutionResponse launchCommand(String[] userCommand) {
        if (userCommand[0].isEmpty()) return new ExecutionResponse("Empty command", false);
        var command = commandManager.getCommands().get(userCommand[0]);

        if (command == null) {
            console.printError("Command '" + userCommand[0] + "' not found. Use 'help' for more information.");
            return new ExecutionResponse("Command is not found", false);
        }

        switch (userCommand[0]) {
            case "exit" -> {
                return commandManager.getCommands().get("exit").execute(userCommand[1]);

            }
            case "execute_script" -> {
                return commandManager.getCommands().get("execute_script").execute(userCommand[1]);
            }
            default -> {
                return commandManager.getCommands().get(userCommand[0]).execute(userCommand[1]); }
        }
    }

    private ExecutionResponse ScriptMode(String fileName) {
        String[] userCommand;
        ExecutionResponse commandStatus = new ExecutionResponse("ScriptMode", true);
        scriptStack.add(fileName);
        if (!new File(fileName).exists()) {
            console.printError("File is not exists");
            return new ExecutionResponse("File is not exists",false);
        }

        try (FileReader filereader = new FileReader(fileName); BufferedReader bufferedReader = new BufferedReader(filereader)) {
            do{
                userCommand = (bufferedReader.readLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandManager.addToHistory(userCommand[0]);
                switch (userCommand[0]) {
                    case "execute_script" -> {

                    }
                    default -> launchCommand(userCommand);
                }
            }
            while (commandStatus.isSuccess() || !commandStatus.getResponse().equals("exit"));

        }catch (FileNotFoundException exception) {
            console.printError("File is not found");
        }catch (NoSuchElementException exception) {
            console.printError("File is empty");
        }catch (IllegalStateException exception) {
            console.printError("Error");
            System.exit(0);
        } catch (IOException e) {
            console.printError("Error while reading a file");
        } finally {
            scriptStack.removeLast();
        }
        return new ExecutionResponse("Error", false);
    }
}