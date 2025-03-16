package Connection;


import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class ConnectionModule {
    public static void main(String[] args) throws IOException {

        byte arr[] = {0,1,2,3,4,5,6,7,8,9};
        DatagramChannel dc; ByteBuffer buf;
        InetAddress host = InetAddress.getLocalHost();
        int port = 6789;
        SocketAddress addr;
        addr = new InetSocketAddress(host,port);
        dc = DatagramChannel.open();
        buf = ByteBuffer.wrap(arr);
        dc.send(buf, addr);
        buf.clear();
        addr = dc.receive(buf);
        for (byte j : arr) {
            System.out.println(j);
        }

    }

}
