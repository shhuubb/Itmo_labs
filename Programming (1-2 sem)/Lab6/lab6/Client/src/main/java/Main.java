import Utility.AskBreak;
import Utility.StandardConsole;
import utility.Runner;

import java.net.UnknownHostException;

class Main {
    public static void main(String[] args) throws UnknownHostException, AskBreak {
        Runner runner = new Runner(new StandardConsole());
        runner.interactiveMode();
    }
}