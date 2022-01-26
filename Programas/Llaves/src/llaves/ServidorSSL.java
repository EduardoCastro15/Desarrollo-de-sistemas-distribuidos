/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package llaves;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author georg
 */
public class ServidorSSL {
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor = socket_factory.createServerSocket(50000); //endpoint "desarrollosistemas.sytes.net"
        
        Socket conexion = socket_servidor.accept();
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        
        double x = entrada.readDouble();
        System.out.println(x);
        
        salida.close();
        entrada.close();
        conexion.close();
    }
}
