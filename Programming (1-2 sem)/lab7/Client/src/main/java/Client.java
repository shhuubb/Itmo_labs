import Utility.AskBreak;
import Utility.StandardConsole;
import utility.Runner;

import java.net.UnknownHostException;

class Client {
    public static void main(String[] args) throws AskBreak, ClassNotFoundException, InterruptedException {
        Runner runner = new Runner(new StandardConsole());
        runner.interactiveMode();
    }
}