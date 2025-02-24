package Utility;

import java.util.Scanner;

public interface Console {
    void print(Object obj);
    void println(Object obj);
    String readln();
    boolean isCanReadln();
    void printError(Object obj);
    void prompt();
    String getPrompt();
    void selectFileScanner(Scanner obj);
    void selectConsoleScanner();
}
