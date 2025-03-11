package Utility;

import model.Coordinates;
import model.Location;
import model.Route;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Vector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        String regex = "\\{\"id\":(.+),\"name\":\"(.+)\",\"coordinates\":(\\{.+}),\"creationDate\":(\\{.+}),\"from\":(\\{.+}),\"to\":(\\{.+}),\"distance\":(.+)}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(routeString);

        if (matcher.find()) {
            String id = matcher.group(1);// Группа 1: id
            String name = matcher.group(2); // Группа 2: name
            String coordinates = matcher.group(3); // Группа 3: coordinates
            String creationDate = matcher.group(4); // Группа 4: creationDate
            String from = matcher.group(5); // Группа 5: from
            String to = matcher.group(6); // Группа 6: to
            String distance = matcher.group(7); // Группа 7: distance
            return new Route(Long.parseLong(id), name, parseCoordinates(coordinates), parseCreationDate(creationDate), parseLocation(from), parseLocation(to), Integer.parseInt(distance));
        } else {
            return null;
        }


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
