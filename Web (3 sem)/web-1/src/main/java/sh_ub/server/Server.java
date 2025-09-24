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

                // Validate presence
                if (!params.containsKey("x") || !params.containsKey("y") || !params.containsKey("r")){
                    jsonFormatter.writeJsonError("Missing parameters: x, y, r are required");
                    continue;
                }

                int x;
                double y;
                double r;
                try {
                    x = Integer.parseInt(params.get("x"));
                } catch (NumberFormatException nfe) {
                    jsonFormatter.writeJsonError("x must be an integer");
                    continue;
                }
                try {
                    y = Double.parseDouble(params.get("y"));
                } catch (NumberFormatException nfe) {
                    jsonFormatter.writeJsonError("y must be a number");
                    continue;
                }
                try {
                    r = Double.parseDouble(params.get("r"));
                } catch (NumberFormatException nfe) {
                    jsonFormatter.writeJsonError("r must be a number");
                    continue;
                }

                // Domain validation (align with frontend: y in (-3,5), x in set, r in set)
                int[] allowedX = {-4,-3,-2,-1,0,1,2,3,4};
                boolean xAllowed = false;
                for (int xv : allowedX) if (xv == x) { xAllowed = true; break; }
                if (!xAllowed){
                    jsonFormatter.writeJsonError("x is out of allowed set [-4..4]");
                    continue;
                }
                if (!(y > -3 && y < 5)){
                    jsonFormatter.writeJsonError("y must be in (-3, 5)");
                    continue;
                }
                double[] allowedR = {1,1.5,2,2.5,3};
                boolean rAllowed = false;
                for (double rv : allowedR) if (Double.compare(rv, r) == 0) { rAllowed = true; break; }
                if (!rAllowed){
                    jsonFormatter.writeJsonError("r must be one of [1, 1.5, 2, 2.5, 3]");
                    continue;
                }

                Coordinates point = new Coordinates(x, y, r);

                long ended = System.nanoTime();

                String record = jsonFormatter.toJsonRecord(point, ended - started);
                updateHistory(record);

                String historyArray = jsonFormatter.readHistoryAsArray();

                jsonFormatter.writeJsonResponse("{\"history\":" + historyArray + "}");
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