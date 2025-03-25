package Connection;

import Command.CommandType;
import Command.CommandWithArgs;
import Utility.ExecutionResponse;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectionClient {
    private DatagramChannel dc;
    private final int port;
    private SocketAddress serverAddr;
    private SocketAddress responseAddr;

    public ConnectionClient(int port) throws UnknownHostException {
        this.port = port;
        this.serverAddr = new InetSocketAddress("localhost", 1234);
    }

    public byte[] serializeObject(Object object) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(object);
            return byteOut.toByteArray();
        }
    }

    public ExecutionResponse deserializeObject(byte[] bytes) {
        if (bytes == null) return new ExecutionResponse( "Ответ от сервера не получен, выполнение отменено!",false);
        InputStream is = new ByteArrayInputStream(bytes);
        try (ObjectInputStream in = new ObjectInputStream(is)) {
            return (ExecutionResponse) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ExecutionResponse("Error in deserialization: " + e.getMessage(), false);
        }
    }

    public void send(byte[] arr) throws IOException {
        if (dc == null || !dc.isOpen()) {
            throw new IOException("Channel is not initialized");
        }
        ByteBuffer buf = ByteBuffer.wrap(arr);
        dc.send(buf, serverAddr);
        buf.clear();
    }

    public byte[] receive() throws IOException {
        if (dc == null || !dc.isOpen()) {
            throw new IOException("Channel is not initialized");
        }
        ByteBuffer bufffer = ByteBuffer.allocate(65535);
        responseAddr = dc.receive(bufffer);
        if (responseAddr != null) {
            byte[] data = new byte[bufffer.position()];
            bufffer.flip();
            bufffer.get(data);
            return data;
        }
        return null;
    }

    public boolean start() {
        try {
            dc = DatagramChannel.open();
            dc.bind(new InetSocketAddress("localhost", port));
            dc.configureBlocking(false);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to start connection: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (dc != null) {
                dc.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing channel: " + e.getMessage());
        }
    }
}
