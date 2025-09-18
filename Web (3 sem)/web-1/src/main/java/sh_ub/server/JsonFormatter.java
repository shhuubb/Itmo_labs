package sh_ub.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class JsonFormatter {
    final private Path file;

    JsonFormatter(Path file) {
        this.file = file;
    }

    public Path getFile() {
        return file;
    }

    public  String readHistoryAsArray() throws IOException {
        if (!Files.exists(file)) return "[]";
        String content = Files.readString(file, StandardCharsets.UTF_8).trim();
        if (content.isEmpty()) return "[]";
        return content;
    }

    public String toJsonRecord(Coordinates point, long time) {
        return """
            {
                "creationTime": "%s",
                "x": %d,
                "y": %f,
                "r": %f,
                "IsHit": %s,
                "executionTime": "%s"
            }
            """.formatted(point.getTime(), point.getX(), point.getY(), point.getR(), point.isHit(), time);

    }

    public void writeJsonResponse(String json){
        String headers = "Content-Type: application/json\r\n\r\n";
        System.out.print(headers);
        System.out.print(json);
        System.out.flush();
    }
}
