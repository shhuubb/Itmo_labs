package org.example;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) throws IOException
    {
        // create a socket to connect to the server running on localhost at port number 9090
        Socket socket = new Socket("localhost", 9090);

        // Setup output stream to send data to the server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Setup input stream to receive data from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Send message to the server
        out.println("Hello from client!");

        // Receive response from the server
        String response = in.readLine();
        System.out.println("Server says: " + response);

        // Close the socket
        socket.close();
    }
}