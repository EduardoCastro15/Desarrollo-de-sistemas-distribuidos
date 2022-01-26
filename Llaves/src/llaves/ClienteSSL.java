/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package llaves;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author georg
 */
public class ClienteSSL {
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
        Socket conexion = cliente.createSocket("localhost", 50000); //endpoint "desarrollosistemas.sytes.net"
        
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        
        salida.writeDouble(1234567890.0987654321);
        
        salida.close();
        entrada.close();
        conexion.close();
    }
    
}
