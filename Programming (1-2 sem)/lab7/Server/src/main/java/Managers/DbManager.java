package Managers;

import model.Coordinates;
import model.Location;
import model.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static java.sql.Types.NULL;


public class DbManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/studs";
    private static final String DB_USER = "s468125";
    private static final String DB_PASSWORD = "hwljJYScGSzuaYac";
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        connect();
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT into coordinates(X, Y) values(23.5, 45.45)");
        ResultSet set = statement.executeQuery("Select * from coordinates");
        while (set.next()) {
            System.out.printf("%d | %.2f | %.2f \n",set.getLong("id"), set.getDouble("X"), set.getDouble("Y"));
        }

    }
    public static void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            logger.info("Connected to database {}!", connection.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            logger.error("Could not connect to database!");
            System.exit(1);
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Could not disconnect from database!");
        }
    }



    public static void addRoute(Route route) {
        try {
            Long coordId = addCoordinates(route.getCoordinates());
            Long fromId = addLocation(route.getFrom());
            Long toId = addLocation(route.getTo());
            Timestamp creationDate = Timestamp.from(route.getCreationDate().toInstant());

            PreparedStatement statement = connection.prepareStatement("INSERT INTO route(name, coordinates_id, creation_date, from_id, to_id, distance) values(?, ?, ?, ?, ?, ?)");
            statement.setDouble(1, route.getCoordinates().getX());
            statement.setLong(2, coordId == null ? NULL : coordId);
            statement.setTimestamp(3, creationDate);
            statement.setLong(4, fromId == null ? NULL : fromId  );
            statement.setLong(5, toId == null ? NULL : toId);
            statement.setDouble(6, route.getDistance());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Could not add Route!");
        }
    }



    public static Long addCoordinates(Coordinates coordinates) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO coordinates(X, Y) values(?, ?) ON CONFLICT (x, y) DO NOTHING RETURNING id");
            statement.setDouble(1, coordinates.getX());
            statement.setDouble(2, coordinates.getY());
            ResultSet rs = statement.executeQuery();

            if (rs.next())
                return rs.getLong("id");

            return GetCoordinatesId(coordinates);

        } catch (SQLException e) {
            logger.error("Could not add coordinates!");
            return null;
        }
    }

    public static Long addLocation(Location location) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO location(x, y, z, name) values(?, ?, ?, ?) ON CONFLICT (x, y, z, name) DO NOTHING RETURNING id");
            statement.setDouble(1, location.getX());
            statement.setDouble(2, location.getY());
            statement.setDouble(3, location.getZ());
            statement.setString(4, location.getName());
            ResultSet rs = statement.executeQuery();

            if (rs.next())
                return rs.getLong("id");

            return GetLocationId(location);

        } catch (SQLException e) {
            logger.error("Could not add Location!");
            return null;
        }
    }

    public static Long GetLocationId(Location location) {

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM location WHERE (x = ? AND y = ? AND z = ? AND name = ?)");
            statement.setDouble(1, location.getX());
            statement.setDouble(2, location.getY());
            statement.setDouble(3, location.getZ());
            statement.setString(4, location.getName());
            ResultSet rs = statement.executeQuery();

            if (rs.next())
                return rs.getLong("id");

        } catch (SQLException e) {
            logger.error("Failed to find location ID", e);
        }

        return null;
    }

    public static Long GetCoordinatesId(Coordinates coordinates) {
        String sql = "SELECT id FROM coordinates WHERE (x = ? AND y = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, coordinates.getX());
            stmt.setDouble(2, coordinates.getY());
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return rs.getLong("id");

        } catch (SQLException e) {
            logger.error("Failed to find coordinates ID", e);
        }

        return null;
    }
}
