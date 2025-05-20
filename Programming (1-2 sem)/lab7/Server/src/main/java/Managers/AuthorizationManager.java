package Managers;

import Authentication.User;
import Utility.ExecutionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages user authorization and registration processes, interacting with the database to validate and store user credentials.
 *
 * @author sh_ub
 */
public class AuthorizationManager {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationManager.class);
    private static final DbUsersManager dbUsersManager = new DbUsersManager();

    /**
     * Registers a new user with the provided credentials.
     *
     * @param user the user to register
     * @return an {@link ExecutionResponse} indicating success or failure of the registration
     * @author sh_ub
     */
    public static ExecutionResponse register(User user) {
        if (user == null) {
            logger.error("Attempted to register null user");
            return new ExecutionResponse("User cannot be null", false);
        }

        String login = user.getLogin();
        String password = user.getPasswordHash();
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            logger.error("Login or password is null or empty for user registration");
            return new ExecutionResponse("Login or password cannot be null or empty", false);
        }

        if (isAuthorize(login)) {
            logger.info("User {} already exists, attempting login", login);
            return login(login, password);
        }

        try {
            dbUsersManager.addUser(user);
            logger.info("User {} successfully registered", login);
            return new ExecutionResponse(String.format("User %s successfully registered", login), true);
        } catch (Exception e) {
            logger.error("Failed to register user {}: {}", login, e.getMessage());
            return new ExecutionResponse(String.format("Failed to register user %s: %s", login, e.getMessage()), false);
        }
    }

    /**
     * Authenticates a user by verifying their login and password.
     *
     * @param login    the user's login
     * @param password the user's password (in plain text)
     * @return an {@link ExecutionResponse} indicating success or failure of the login attempt
     * @author sh_ub
     */
    public static ExecutionResponse login(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            logger.error("Login or password is null or empty for login attempt");
            return new ExecutionResponse("Login or password cannot be null or empty", false);
        }

        User user = dbUsersManager.findUser(login);
        if (user == null) {
            logger.warn("User {} not found during login attempt", login);
            return new ExecutionResponse(String.format("User %s not found", login), false);
        }

        if (user.validatePassword(password)) {
            logger.info("User {} successfully logged in", login);
            return new ExecutionResponse(String.format("User %s successfully logged in", login), true);
        } else {
            logger.warn("Invalid password for user {}", login);
            return new ExecutionResponse("Invalid password for user", false);
        }
    }

    /**
     * Checks if a user with the specified login exists.
     *
     * @param login the login to check
     * @return {@code true} if the user exists, {@code false} otherwise
     * @author sh_ub
     */
    public static boolean isAuthorize(String login) {
        if (login == null || login.isEmpty()) {
            logger.error("Login is null or empty for authorization check");
            return false;
        }

        User user = dbUsersManager.findUser(login);
        boolean exists = user != null;
        if (exists) {
            logger.debug("User {} exists", login);
        } else {
            logger.debug("User {} not found", login);
        }
        return exists;
    }
}