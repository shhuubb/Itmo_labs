package sh_ub.server;

import com.fastcgi.FCGIInterface;
import java.util.Map;
import java.util.HashMap;

import static sh_ub.server.EnvConfiguration.getMongoDBHandler;


public class Server {

    private static MongoDBHandler mongoDBHandler = getMongoDBHandler();

    public static void main(String[] args) {
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            try {
                String queryString = System.getProperty("QUERY_STRING", "");
                String scriptName = System.getProperty("SCRIPT_NAME", "");

                if (queryString.contains("history")) {
                    String history = mongoDBHandler.readHistoryAsArray();
                    mongoDBHandler.writeJsonResponse("{\"history\":" + history + "}");
                    continue;
                }
                else if (scriptName.equals("/health")) {
                    mongoDBHandler.writeJsonResponse("{\"status\":\"UP\"}");
                    continue;
                }

                long started = System.nanoTime();
                Map<String, String> params = parseQuery(queryString);

                Coordinates point = new Coordinates(
                        Double.parseDouble(params.get("x")),
                        Double.parseDouble(params.get("y")),
                        Double.parseDouble(params.get("r"))
                );
                if (!point.isValidCoordinates()) {
                    throw new isValidException("Invalid coordinates");
                }

                long ended = System.nanoTime();

                mongoDBHandler.saveRecord(point, ended - started);

                String historyArray = mongoDBHandler.readHistoryAsArray();

                mongoDBHandler.writeJsonResponse("{\"history\":" + historyArray + "}");
            } catch (isValidException exception) {
                mongoDBHandler.writeJsonResponse("{\"error\":\"coordinates are invalid!\"}");
            } catch (Exception e) {
                mongoDBHandler.writeJsonResponse("{\"ERROR\":\"" + e.getMessage() + "\"}");
            }
        }
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return map;
        }
        String[] parts = query.split("&");
        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

}