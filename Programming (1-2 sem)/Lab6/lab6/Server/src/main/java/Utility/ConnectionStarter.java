package Utility;

import Managers.CommandManager;
import Managers.ConnectionManager;

import java.io.IOException;
import Command.CommandWithArgs;
import model.Route;
import Command.CommandType;

public class ConnectionStarter {
    public static void run(int port, CommandManager commandManager, StandardConsole console) {

        try {
            ConnectionManager cm = new ConnectionManager(port);
            while (true) {
                byte[] data = cm.receive();
                CommandWithArgs cmd = cm.deserialize(data);
                Command command = commandManager.getCommands().get(cmd.getCommand().toString().toLowerCase());
                CommandType commandType = cmd.getCommand();
                ExecutionResponse response;
                if (cmd != null) {
                    if (commandType == CommandType.ADD || commandType == CommandType.UPDATE){
                        Route arg = cmd.getRoute();
                        response = command.execute(arg);
                }
                    else if (commandType == CommandType.FILTER_CONTAINS_NAME || commandType == CommandType.REMOVE_BY_ID){
                        String arg = cmd.getArgs();
                        response = command.execute(arg);
                    }else if (commandType == CommandType.EXIT){
                        response = command.execute(null);
                    }
                    else {
                        response = command.execute(null);
                    }
                    cm.send(cm.serializeObject(response));
                } else{
                    console.println("Receiving error error");
                }
            }
        } catch (IOException e) {
            console.println("Sending error");
        }
    }
}
