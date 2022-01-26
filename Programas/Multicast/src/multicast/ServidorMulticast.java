/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 *
 * @author georg
 */
public class ServidorMulticast {

    /**
     * @param args the command line arguments
     */
    static void enviaMensaje(byte[] buffer, String ip, int puerto) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        InetAddress ipGrupo = InetAddress.getByName(ip);
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, ipGrupo, puerto);
        socket.send(paquete);
        socket.close();
    }
    @SuppressWarnings( "deprecation" )
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        System.setProperty("java.net.preferIPv4Stack", "true");
        enviaMensaje("hola".getBytes(), "230.0.0.0", 50000);
        ByteBuffer b = ByteBuffer.allocate(5*8);
        b.putDouble(1.1);
        b.putDouble(1.2);
        b.putDouble(1.3);
        b.putDouble(1.4);
        b.putDouble(1.5);
        enviaMensaje(b.array(), "230.0.0.0", 50000);
    }
    
}
