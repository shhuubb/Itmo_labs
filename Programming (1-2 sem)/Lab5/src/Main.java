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
        String input = "{Route1, 12, 243, Home, 324, 343,343.43, School, 324, 343, 343.43, 100}";
        String[] list = input.substring(1, input.length()-1).split(",");

        for (String line : list) {
            System.out.println(line);
        }
    }
}
