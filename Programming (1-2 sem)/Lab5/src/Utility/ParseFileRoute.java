package Utility;

import Client.StandardConsole;
import model.Coordinates;
import model.Location;
import model.Route;


import java.time.ZonedDateTime;
import java.util.NoSuchElementException;


public class ParseFileRoute {

    public static Route ParseRoute(StandardConsole console, String line, Long id) throws AskBreak{
        line = line.trim();
        String[] list = line.substring(1, line.length()-1).split(",");

        try{
            String name = AskName(console, list[0].trim());
            Coordinates coordinates = AskCoordinates(console, list[1], list[2]);
            Location from = AskLocation(console, list[3], list[4].trim(), list[5].trim(), list[6].trim());
            Location to = AskLocation(console, list[7], list[8], list[9], list[10]);
            int distance = AskDistance(console, list[11]);
            return new Route(id, name, coordinates, ZonedDateTime.now(), from, to, distance);
        } catch (NoSuchElementException | ArrayIndexOutOfBoundsException e){
            return null;
        }


    }
    private static String AskName(StandardConsole console, String name) throws AskBreak{
        try{
            if (name.equals("exit")) throw new AskBreak();
            if (name.isEmpty()) return null;

        }  catch(NoSuchElementException | IllegalStateException e){
            console.printError("Reading error");
            return null;
        }
        return name;
    }

    private static Coordinates AskCoordinates(StandardConsole console, String lineX, String lineY) throws AskBreak{
        try{
            lineX = lineX.trim();
            lineY = lineY.trim();
            double x = 0;
            if (lineX.equals("exit")) throw new AskBreak();
            if (!lineX.isEmpty()) {
                try {
                    x = Double.parseDouble(lineX);
                    if (x < -605){
                        console.printError("Coordinate x must be greater -605.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    console.printError("Coordinate x must be number.");
                    return null;
                }
            }

            float y = 0;
                if (lineY.equals("exit")) throw new AskBreak();
                if (!lineY.isEmpty()){
                    try {
                        y = Float.parseFloat(lineY);
                        if (y>243) {
                            console.printError("Coordinate y must be less 244.");
                            return null;
                        }
                    }
                    catch (NumberFormatException e) {
                        console.printError("Coordinate y must be number.");
                        return null;
                    }
                }
            return new Coordinates(x, y);
        }
        catch(NoSuchElementException | IllegalStateException e){
            console.printError("Reading error");
            return null;
        }
    }
    private static Location AskLocation(StandardConsole console, String LocationName, String coordinateX, String coordinateY, String coordinateZ) throws AskBreak{
        try{
            coordinateX = coordinateX.trim();
            coordinateY = coordinateY.trim();
            coordinateZ = coordinateZ.trim();
            String name = "";
            if (LocationName.equals("exit")) throw new AskBreak();
            if (!LocationName.isEmpty()){
                if (LocationName.length() <=318) {
                    name = LocationName;
                }
                else{
                    console.printError("Location name length must be less then 318 symbols.");
                    return null;
                }
            }

            long x = 0;

            if (coordinateX.equals("exit")) throw new AskBreak();
            if (!coordinateX.isEmpty()){
                try {
                    x = Long.parseLong(coordinateX);
                }
                catch (NumberFormatException e) {
                    console.println(coordinateX);
                    console.printError("Coordinate x of Location must be integer number.");
                    return null;
                }
            }


            long y = 0;

            if (coordinateY.equals("exit")) throw new AskBreak();
            if (!coordinateY.isEmpty()){
                try {
                    y = Long.parseLong(coordinateY);
                }
                catch (NumberFormatException e) {
                    console.printError("Coordinate y of Location must be integer number. ");
                    return null;
                }
            }


            double z = 0;

            if (coordinateZ.equals("exit")) throw new AskBreak();
            if (!coordinateZ.isEmpty()){
                try {
                    z = Double.parseDouble(coordinateZ);
                }
                catch (NumberFormatException e) {
                    console.printError("Coordinate z of Location must be number.");
                    return null;
                }
            }
            return new Location(x, y, z, name);
        }catch(NoSuchElementException | IllegalStateException e){
            console.printError("Reading error");
            return null;
        }
    }

    private static Integer AskDistance(StandardConsole console, String distanceLine) throws AskBreak{
        try{
            int distance = 0;

            if (distanceLine.equals("exit")) throw new AskBreak();
            if (!distanceLine.isEmpty()){
                    try{
                        distance = Integer.parseInt(distanceLine.trim());
                        if(distance <= 1) console.printError("Distance must be greater than 1.");

                    }
                    catch(NumberFormatException e){
                        console.printError("Distance must be number.");
                        return 0;
                    }
                }
            return distance;

        }catch(NullPointerException |NoSuchElementException | IllegalStateException e){
            console.printError("Reading error");
            return 0;
        }
    }

}
