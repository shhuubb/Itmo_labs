package Managers;

import Command.CommandWithArgs;
import Utility.ExecutionResponse;

import java.io.*;
import java.net.*;

public class ConnectionManager {
    private DatagramSocket ds;
    private final int port;
    private InetAddress lastClientAddress;
    private int lastClientPort;

    public ConnectionManager(int port) throws SocketException {
        this.port = port;
        this.ds = new DatagramSocket(port);
    }

    public byte[] serializeObject(Object object) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(object);
            return byteOut.toByteArray();
        }
    }

    public CommandWithArgs deserialize(byte[] bytes) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(is)) {
            return (CommandWithArgs) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization error: " + e.getMessage());
            return null;
        }
    }

    public void send(byte[] arr) throws IOException {
        if (ds == null || ds.isClosed()) {
            throw new IOException("Socket is not initialized");
        }
        if (lastClientAddress == null) {
            throw new IOException("No client address available");
        }
        System.out.println(lastClientPort);

        DatagramPacket packet = new DatagramPacket(arr, arr.length, lastClientAddress, lastClientPort);
        ds.send(packet);
    }

    public byte[] receive() throws IOException {
        byte[] arr = new byte[65535];
        DatagramPacket packet = new DatagramPacket(arr, arr.length);
        ds.receive(packet);

        this.lastClientAddress = packet.getAddress();
        this.lastClientPort = packet.getPort();

        return arr;
    }

    public static void main(String[] args) {
        try {
            ConnectionManager cm = new ConnectionManager(1234);

            while (true) {
                byte[] data = cm.receive();
                CommandWithArgs cmd = cm.deserialize(data);

                if (cmd != null) {
                    System.out.println("Received command: " + cmd.getCommand());

                    ExecutionResponse response = new ExecutionResponse("Processed: " + cmd.getCommand(), true);
                    cm.send(cm.serializeObject(response));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}