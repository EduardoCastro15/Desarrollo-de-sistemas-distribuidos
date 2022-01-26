
//El cliente envia del 1 al 500 utilizando la clase ByteBuffer
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

class Cliente2 {
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
        //Socket conexion = new Socket("localhost", 50000); //endpoint "desarrollosistemas.sytes.net"
        Socket conexion = null;
        for(;;){
            try{
                conexion = new Socket("localhost", 50000);
                break;
            }
            catch(Exception e){
                Thread.sleep(100);
            }
        }
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        /*
        salida.writeInt(123);
        salida.writeDouble(1234567890.0987654321);
        salida.write("Hola".getBytes());
        
        
        read(entrada, buffer, 0, 4);
        System.out.println(new String(buffer, "UTF-8"));*/
        
        ByteBuffer b = ByteBuffer.allocate(500*8); //Apartamos, mediante el metodo estatico, 40 bytes en una estructura
        //Empaquetamiento de doubles
        for(int i=1; i<=500; i++){
            b.putInt(i);
        }
        //Metodo de instancias, porque para invocarlo ocupamos un objeto
        byte[] a = b.array();
        //Enviamos el paquete
        salida.write(a);
        byte[] buffer = new byte[1];
        read(entrada, buffer, 0, 1);
        System.out.println(new String(buffer, "UTF-8"));

        //Cerramos los sockets
        salida.close();
        entrada.close();
        conexion.close();
    }
}
