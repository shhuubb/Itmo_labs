package Managers;

import Command.CommandWithArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private ServerSocket serverSocket;;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ConnectionManager(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        logger.info("ConnectionManager initialized on port {}", port);
    }

    public void start() throws IOException {
        logger.info("Starting TCP server...");
        clientSocket = serverSocket.accept();
        logger.info("Client connected: {}", clientSocket.getRemoteSocketAddress());
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public byte[] serializeObject(Object object) throws IOException {
        logger.debug("Serializing object of type: {}", object.getClass().getName());
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(object);
            return byteOut.toByteArray();
        }
    }

    public CommandWithArgs deserialize(byte[] bytes) {
        logger.debug("Deserializing command from byte array (length: {})", bytes.length);
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(is)) {
            CommandWithArgs command = (CommandWithArgs) in.readObject();
            logger.debug("Deserialized command: {}", command);
            return command;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Deserialization error: {}", e.getMessage(), e);
            return null;
        }
    }

    public void send(byte[] arr) throws IOException {
        try {
            outputStream.writeObject(arr);
            outputStream.flush();
            logger.debug("Sent object");

        }catch (IOException e) {
            logger.error("Sending error: {}", e.getMessage());
        }
    }

    public byte[] receive() throws ClassNotFoundException {
        try {
            byte[] command =  (byte[]) inputStream.readObject();
            logger.debug("Received command: {}", command.length);
            return command;
        } catch (IOException e) {
            logger.error("Receiving error: {}", e.getMessage());
            return null;
        }
    }

}