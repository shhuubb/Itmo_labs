package Client;

import Utility.Console;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class StandardConsole implements Console {
    private static final String P = "> ";
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    public void print(Object message) {
        System.out.print(message);
    }

    public void println(Object message) {
        System.out.println(message);
    }

    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner!=null ? fileScanner : defScanner).nextLine();
    }
    public boolean isCanReadln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner!=null ? fileScanner : defScanner).hasNextLine();
    }
    public String printTable(String RightEl, String LeftEl) {
        return String.format(" %-35s%-1s%n", RightEl, LeftEl);

    }
    public void prompt() {
        print(P);
    }

    public String getPrompt() {
        return P;
    }

    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    public void selectConsoleScanner() {
        fileScanner = null;
    }
}
