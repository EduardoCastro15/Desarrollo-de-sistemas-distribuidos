/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;

/**
 *
 * @author georg
 */
public class ClienteMulticast {
    
    static byte[] recibeMensaje(MulticastSocket socket, int longitudMensaje) throws IOException{
        byte[] buffer = new byte[longitudMensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
    @SuppressWarnings( "deprecation" )
    public static void main(String[] args) throws Exception{
        /*InetAddress ipGrupo = InetAddress.getByName("230.0.0.0");
        MulticastSocket socket = new MulticastSocket(50000);
        socket.joinGroup(ipGrupo);
        byte[] a = recibeMensaje(socket, 4);
        System.out.println(new String(a, "UTF-8"));
        byte[] buffer = recibeMensaje(socket, 5*8);
        ByteBuffer b = ByteBuffer.wrap(buffer);
        for(int i=0; i < 5; i++)
            System.out.println(b.getDouble());
        socket.leaveGroup(ipGrupo);
        socket.close();*/
        System.setProperty("java.net.preferIPv4Stack", "true");
        MulticastSocket socket = new MulticastSocket(50000);
        InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("230.0.0.0"),50000);
        NetworkInterface netInter = NetworkInterface.getByName("em1");
        socket.joinGroup(grupo,netInter);

        byte[] a = recibeMensaje(socket,4);
        System.out.println(new String(a,"UTF-8"));

        byte[] buffer = recibeMensaje(socket,5*8);
        ByteBuffer b = ByteBuffer.wrap(buffer);
        for (int i = 0; i < 5; i++) System.out.println(b.getDouble());

        socket.leaveGroup(grupo,netInter);
        socket.close();
    }
}
