package Managers;

import Client.StandardConsole;
import model.Route;

import java.io.*;

import java.util.Vector;

import static Utility.JsonParser.parseJson;
import static Utility.JsonSerialization.ToJson;

public class DumpManager {
    private final String fileName;
    private final StandardConsole console;

    public DumpManager(String fileName, StandardConsole console) {
        this.fileName = fileName;
        this.console = console;
    }

    public void dump(Vector<Route> collection) {
        try {
            OutputStream outputStream = new FileOutputStream(fileName);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write(ToJson(collection));
            outputStreamWriter.close();
        }catch (IOException e) {
            console.printError("File " + fileName + " could not be opened.");
        }
    }

    public Vector<Route> read() {
        if (fileName != null && !fileName.isEmpty()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));

                var jsonString = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        jsonString.append(line);
                    }
                }

                if (jsonString.isEmpty()) {
                    jsonString = new StringBuilder("[]");
                }
                return parseJson(jsonString.toString());


            } catch (FileNotFoundException e) {
                console.printError("File " + fileName + " could not be opened.");
            } catch (IllegalStateException | IOException e) {
                console.printError("Error in reading file " + fileName + ".");
                System.exit(0);
            }
        } else
            console.printError("file is not found");

        return new Vector<>();
    }
}
