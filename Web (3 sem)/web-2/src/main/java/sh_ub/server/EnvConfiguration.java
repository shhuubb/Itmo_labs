package sh_ub.server;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfiguration {
    Dotenv dotenv = Dotenv.configure().directory("/app").filename(".env").load();

    private String dbHost = dotenv.get("DB_NAME");
    private String dbPort = dotenv.get("PORT");
    private String dbUser = dotenv.get("DB_USER");
    private String dbPassword = dotenv.get("DB_PASS");
    private String collectionName = dotenv.get("COLLECTION_NAME");
    private String authSource = dotenv.get("AUTHSOURCE");

    public static MongoDBHandler getMongoDBHandler() {
        EnvConfiguration config = new EnvConfiguration();
        String connectionString = String.format(
                "mongodb://%s:%s@mongo:%s/%s?authSource=%s",
                config.getDbUser(),
                config.getDbPassword(),
                config.getDbPort(),
                config.getDbHost(),
                config.getAuthSource()
        );

        ;
        return new MongoDBHandler(connectionString, config.dbHost, config.collectionName);
    }


    public String getDbPort() {
        return dbPort;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getAuthSource() {
        return authSource;
    }
}