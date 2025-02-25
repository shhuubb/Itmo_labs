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
            ExecutionResponse commandStatus = new ExecutionResponse("start", true);
            String[] userCommand;
            do {
                if (commandStatus.isSuccess()) console.prompt();
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
                System.exit(1);
                return new ExecutionResponse("Exit", true);
            }
            case "execute_script" -> {
                return ScriptMode(userCommand[1]);
            }
            default -> {
                return commandManager.getCommands().get(userCommand[0]).execute(userCommand[1]); }
        }
    }

    private ExecutionResponse ScriptMode(String fileName) {
        String[] userCommand;
        ExecutionResponse commandStatus;
        scriptStack.add(fileName);
        if (!new File(fileName).exists()) {
            console.printError("File is not exists");
            return new ExecutionResponse("File is not exists",false);
        }
        String line;
        try (FileReader filereader = new FileReader(fileName); BufferedReader bufferedReader = new BufferedReader(filereader)) {
            do{
                line = bufferedReader.readLine();
                userCommand = (line.trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandManager.addToHistory(userCommand[0]);
                var needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    var recStart = -1;
                    var i = 0;
                    for (String script : scriptStack) {
                        i++;
                        if (userCommand[1].equals(script)) {
                            if (recStart < 0) recStart = i;
                            if (lengthRecursion < 0) {
                                console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..500)");
                                while (lengthRecursion < 0 || lengthRecursion > 500) {
                                    try { console.print("> "); lengthRecursion = Integer.parseInt(console.readln().trim()); } catch (NumberFormatException e) { console.println("длина не распознан"); }
                                }
                            }
                            if (i > recStart + lengthRecursion || i > 500)
                                needLaunch = false;
                        }
                    }
                }
                commandStatus = needLaunch ? launchCommand(userCommand) :new ExecutionResponse("scriptMode", true);
            }
            while (!line.isEmpty() || commandStatus.isSuccess() || !commandStatus.getResponse().equals("exit"));

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