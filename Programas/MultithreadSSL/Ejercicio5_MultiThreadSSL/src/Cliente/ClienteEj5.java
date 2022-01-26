/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JFileChooser;

/**
 *
 * @author argen
 */
public class ClienteEj5 {
    
    static byte[] lee_archivo(String archivo) throws Exception{
            FileInputStream f = new FileInputStream(archivo);
            byte[] buffer;
            try
            {
              buffer = new byte[f.available()];
              f.read(buffer);
            }
            finally
            {
              f.close();
            }
            return buffer;
    }
    
    static void read(DataInputStream f,byte[] b, int posicion, int longitud) throws Exception{

			while(longitud > 0){
				int n = f.read(b, posicion, longitud);
				posicion += n;
				longitud -= n;
			}
	}
    
    
    public static void main(String[] args) throws Exception{
        
        
        Socket socket = null;
        for(;;){
            
            try{
                SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = cliente.createSocket("localhost", 50000);
                System.out.println("Conexion con servidor...\n");
                break;
            }
            catch(Exception e){
                Thread.sleep(100);
                e.printStackTrace();
            }
        }    
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            DataInputStream dis = new DataInputStream(socket.getInputStream());
                    
             File f;
            String archivo = "", nombre = "";
            long tam;
            
            JFileChooser jf = new JFileChooser();
            
           
                int r = jf.showOpenDialog(null);
                if(r == JFileChooser.APPROVE_OPTION){
                    f = jf.getSelectedFile();
                    archivo = f.getAbsolutePath();
                    nombre = f.getName();
                   
                }
            byte[] b = lee_archivo(archivo);
           
            dos.writeUTF(nombre);
            dos.writeInt(b.length);
            dos.write(b);
            
            int flag  = dis.readInt();
            
            if(flag == 1)
                    System.out.println("Archivo "+nombre+" Enviado con exito.");
            
            dis.close();
            dos.close();
            socket.close();
        
    }

    
}
