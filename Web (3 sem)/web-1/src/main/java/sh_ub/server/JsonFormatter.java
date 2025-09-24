package sh_ub.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Locale;

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
        return String.format(Locale.US, """
        {
            "creationTime": "%s",
            "x": %d,
            "y": %.10f,
            "r": %.1f,
            "IsHit": %b,
            "executionTime": %d
        }
        """, point.getTime(), point.getX(), point.getY(), point.getR(), point.isHit(), time);
    }

    public void writeJsonResponse(String json){
        String headers = "Content-Type: application/json\r\n\r\n";
        System.out.print(headers);
        System.out.print(json);
        System.out.flush();
    }

	public void writeJsonError(String message){
		String headers = "Status: 400 Bad Request\r\nContent-Type: application/json\r\n\r\n";
		System.out.print(headers);
		System.out.print("{\"error\":\"" + message.replace("\"", "\\\"") + "\"}");
		System.out.flush();
	}
}
