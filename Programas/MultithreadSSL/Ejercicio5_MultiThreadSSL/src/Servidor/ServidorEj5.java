/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author argen
 */
public class ServidorEj5 {
    
    static class Worker extends Thread{
        
        Socket conexion;
        
        static void read(DataInputStream f,byte[] b, int posicion, int longitud) throws Exception{

			while(longitud > 0){
				int n = f.read(b, posicion, longitud);
				posicion += n;
				longitud -= n;
			}
	}
        
        
        Worker(Socket conexion){
            this.conexion = conexion;
        }
        
        
    
        
        public void run(){
            try{
                
                System.out.println("Conexión establecida desde"+
                                    conexion.getInetAddress()+":"+conexion.getPort());
                
                DataInputStream dis = new DataInputStream(conexion.getInputStream());
                DataOutputStream dos = new DataOutputStream(conexion.getOutputStream());

                File directorio = new File(System.getProperty("user.dir")+"/ArchivosServer");
                if (!directorio.exists()) {
                    if (directorio.mkdirs()) {
                        System.out.println("Directorio creado");
                    } else {
                        System.out.println("Error al crear directorio");
                    }
                }
                
                
                String nombre = dis.readUTF();     
                nombre = "ArchivosServer/"+nombre;
                int lenght = dis.readInt();
                
                byte[] buffer = new byte[lenght];
                
                read(dis, buffer, 0, lenght);
                
                escribe_archivo(nombre,buffer);
                
                System.out.println("Archivo "+nombre+" recibido correctamente");
                dos.writeInt(1);
                 dis.close();
                 dos.close();
                 conexion.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    static void escribe_archivo(String archivo,byte[] buffer) throws Exception{
        
        FileOutputStream f = new FileOutputStream(archivo);
        try
        {
          f.write(buffer);
        }
        finally
        {
          f.close();
        }
    }
    
    public static void main(String[] args) throws Exception{
        
        SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor = socket_factory.createServerSocket(50000);
        for(;;){
             
        
            Socket conexion = socket_servidor.accept();
            Worker w = new Worker(conexion);
            w.start();
        }
    }
}
