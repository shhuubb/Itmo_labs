package Managers;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class    ConnectionManager {

    public static void main(String[] args) throws IOException {
        try{
            byte arr[] = new byte[10];
            int len = arr.length;
            DatagramChannel dc;
            ByteBuffer buf;
            int port = 6789;
            SocketAddress addr;
            addr = new InetSocketAddress(port);
            dc = DatagramChannel.open();
            dc.bind(addr);
            while (true){
                buf = ByteBuffer.wrap(arr);
                addr = dc.receive(buf);
                for (int j = 0; j < len; j++) {
                    arr[j] *= 2;
                }
                buf.flip();
                dc.send(buf, addr);
            }
        }
    catch (Exception e){
        System.out.println(e);}
    }
}