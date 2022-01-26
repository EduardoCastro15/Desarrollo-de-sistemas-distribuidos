
//Medir tiempos de paquetes serializados vs paquetes individuales
package sockets;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Cliente {

    //Metodo para garantizar la llegada de toda la informacion
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{
            while(longitud > 0){
                int n = f.read(b, posicion, longitud);
                posicion += n;
                longitud -= n;
            }
    }
    
    public static void main(String[] args) throws Exception{
        //Abrimos la conexion
        Socket conexion = new Socket("localhost", 50000); //endpoint "desarrollosistemas.sytes.net"
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        
        salida.writeInt(123);
        salida.writeDouble(1234567890.0987654321);
        salida.write("Hola".getBytes());
        
        byte[] buffer = new byte[4];
        read(entrada, buffer, 0, 4);
        System.out.println(new String(buffer, "UTF-8"));
        
        ByteBuffer b = ByteBuffer.allocate(5*8); //Apartamos, mediante el metodo estatico, 40 bytes en una estructura
        //Empaquetamiento de doubles
        b.putDouble(1.1);
        b.putDouble(1.2);
        b.putDouble(1.3);
        b.putDouble(1.4);
        b.putDouble(1.5);
        //Metodo de instancias, porque para invocarlo ocupamos un objeto
        byte[] a = b.array();
        //Enviamos el paquete
        salida.write(a);
        //Cerramos los sockets
        salida.close();
        entrada.close();
        conexion.close();
    }
}
