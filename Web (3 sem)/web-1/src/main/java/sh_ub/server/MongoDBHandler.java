package sh_ub.server;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

public class MongoDBHandler {
    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    public MongoDBHandler(String mongoUri, String databaseName, String collectionName) {
        try {
            this.mongoClient = MongoClients.create(mongoUri);
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            this.collection = database.getCollection(collectionName);
        } catch (Exception e) {
            throw new RuntimeException("MongoDB connection failed", e);
        }
    }

    public String readHistoryAsArray() {
        try {
            List<Document> documents = new ArrayList<>();
            collection.find().into(documents);
            if (documents.isEmpty()) {
                return "[]";
            }
            StringBuilder jsonArray = new StringBuilder("[");
            boolean first = true;
            for (Document doc : documents) {
                if (!first) {
                    jsonArray.append(",");
                }
                doc.remove("_id");
                jsonArray.append(doc.toJson());
                first = false;
            }
            jsonArray.append("]");
            return jsonArray.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read history", e);
        }
    }

    public void saveRecord(Coordinates point, long executionTime) {
        try {
            Document document = new Document()
                    .append("creationTime", point.getTimeString())
                    .append("x", point.getX())
                    .append("y", point.getY())
                    .append("r", point.getR())
                    .append("IsHit", point.isHit())
                    .append("executionTime", executionTime);
            collection.insertOne(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save record", e);
        }
    }

    public void writeJsonResponse(String json) {
        String headers = "Status: 200 OK\r\nContent-Type: application/json\r\n\r\n";
        System.out.print(headers);
        System.out.print(json);
        System.out.flush();
    }
}