package Utility;

import model.Coordinates;
import model.Location;
import model.Route;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Vector;
/**
 * Класс для десереализации данных из json
 * @author sh_ub
 */
public class JsonParser {
    public static Vector<Route> parseJson(String json) {
        Vector<Route> routes = new Vector<>();
        json = json.trim().replace("\n", "").replace("\r", "").replace(" ", "");


        json = json.substring(1, json.length() - 1);


        String[] routeStrings = json.split("\\},\\s*\\{");

        for (String routeString : routeStrings) {

            if (!routeString.startsWith("{")) {
                routeString = "{" + routeString;
            }
            if (!routeString.endsWith("}")) {
                routeString = routeString + "}";
            }

            Route route = parseRoute(routeString);
            routes.add(route);
        }

        return routes;
    }

    private static Route parseRoute(String routeString) {
         Long id = null;
         String name = null;
         Coordinates coordinates = null;
         ZonedDateTime creationDate = null;
         Location from = null;
         Location to = null;
         int distance = 1;
        routeString = routeString.substring(1, routeString.length() - 1);

        String[] fields = routeString.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":", 2);
            String key = keyValue[0].replace("\"", "");
            String value = keyValue[1].replace("\"", "");

            switch (key) {
                case "id" ->
                        id = Long.parseLong(value);
                case "name" ->
                        name = value;
                case "coordinates" ->
                        coordinates = parseCoordinates(value);
                case "creationDate" ->
                        creationDate = parseCreationDate(value);
                case "from" ->
                        from = parseLocation(value);
                case "to" ->
                        to = parseLocation(value);
                case "distance" ->
                        distance = Integer.parseInt(value);

            }
        }

        return new Route(id, name, coordinates, creationDate, from, to, distance);
    }

    private static Coordinates parseCoordinates(String coordinatesString) {
        double x = 0.;
        float y = 0.f;
        coordinatesString = coordinatesString.substring(1, coordinatesString.length() - 1); // Убираем {}

        String[] fields = coordinatesString.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":", 2);
            String key = keyValue[0].replace("\"", "");
            String value = keyValue[1].replace("\"", "");

            switch (key) {
                case "x" ->
                        x = Double.parseDouble(value);
                case "y" ->
                        y = Float.parseFloat(value);

            }
        }

        return new Coordinates(x, y);
    }

    private static ZonedDateTime parseCreationDate(String creationDateString) {

        creationDateString = creationDateString.substring(1, creationDateString.length() - 1);
        int year = 0, month =1, day = 1, hour = 0 , minute = 0;
        String[] fields = creationDateString.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":", 2);
            String key = keyValue[0].replace("\"", "");
            String value = keyValue[1].replace("\"", "");

            switch (key) {
                case "year" ->
                        year = Integer.parseInt(value);

                case "month" ->
                        month = Integer.parseInt(value);

                case "day" ->
                        day = Integer.parseInt(value);

                case "hour"->
                        hour = Integer.parseInt(value);

                case "minute"->
                        minute = Integer.parseInt(value);
            }
        }
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute);
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(localDateTime, zoneId);
    }


    private static Location parseLocation(String locationString) {
        long x = 0L;
        long y = 0L;
        double z = 0.;
        String name = "";
        locationString = locationString.substring(1, locationString.length() - 1);

        String[] fields = locationString.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":", 2);
            String key = keyValue[0].replace("\"", "");
            String value = keyValue[1].replace("\"", "");

            switch (key) {
                case "LocationName" ->
                        name = value;
                case "x" ->
                        x = Long.parseLong(value);
                case "y" ->
                        y = Long.parseLong(value);
                case "z" ->
                        z = Double.parseDouble(value);

            }
        }

        return new Location(x, y, z, name);
    }
}
