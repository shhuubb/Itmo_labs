package sh_ub.server;

import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import java.util.Map;
import java.util.HashMap;

public class Server {
    private static JsonFormatter jsonFormatter = new JsonFormatter(Paths.get("history.json"));

    public static void main(String[] args) {
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {

            try {
                String queryString = System.getProperty("QUERY_STRING");
                if (queryString.contains("history")) {
                    jsonFormatter.writeJsonResponse("{\"history\":" + jsonFormatter.readHistoryAsArray() + "}");
                    continue;
                }

                long started = System.nanoTime();

                Map<String, String> params = parseQuery(queryString);

                Coordinates point = new Coordinates(Integer.parseInt(params.get("x")), Double.parseDouble(params.get("y")), Double.parseDouble(params.get("r")));
                if (!point.isValidCoordinates()) {
                    throw new isValidException("Invalid coordinates");
                }

                long ended = System.nanoTime();

                String record = jsonFormatter.toJsonRecord(point, ended - started);
                updateHistory(record);

                String historyArray = jsonFormatter.readHistoryAsArray();

                jsonFormatter.writeJsonResponse("{\"history\":" + historyArray + "}");
            } catch (isValidException exception){
                jsonFormatter.writeJsonResponse("{\"error\":\"coordinates are invalid!\"}");
            } catch (Exception e) {
                jsonFormatter.writeJsonResponse("{\"error\":\"" + e.getMessage() + "\"}");
            }
        }
    }
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        String[] parts = query.split("&");
        for (String part : parts) {
            map.put(part.split("=")[0], part.split("=")[1]);
        }
        return map;
    }

    private static void updateHistory(String jsonLine) throws IOException {
        Path p = jsonFormatter.getFile();
        String current = Files.exists(p) ? Files.readString(p, StandardCharsets.UTF_8).trim() : "";
        if (current.isEmpty()) current = "[]";
        String updated;
        if (current.equals("[]")) {
            updated = "[" + jsonLine + "]";
        } else {
            if (current.endsWith("]")) current = current.substring(0, current.length()-1);
            updated = current + "," + jsonLine + "]";
        }
        Files.writeString(p, updated, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}