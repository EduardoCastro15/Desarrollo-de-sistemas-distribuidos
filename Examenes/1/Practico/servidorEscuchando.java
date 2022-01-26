/*Servidor que que espera conexion en el puerto 50000 y sumar los numeros que reciba*/
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;


class Servidor2{
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{
            while(longitud > 0){
                int n = f.read(b, posicion, longitud);
                posicion += n;
                longitud -= n;
            }
        }
    static class Worker extends Thread{
            Socket conexion;
            Worker(Socket conexion){
                this.conexion = conexion;
            }
            
            @Override
            public void run(){
                try{
                    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
/*
                    int n = entrada.readInt();
                    System.out.println(n);

                    double x = entrada.readDouble();
                    System.out.println(x);

                    byte[] buffer = new byte[4];
                    read(entrada, buffer, 0, 4);
                    System.out.println(new String(buffer, "UTF-8"));

                    salida.write("HOLA".getBytes());*/
                    int suma =0;
                    byte[] a = new byte[500*8];
                    read(entrada, a, 0, 500*8);

                    ByteBuffer b = ByteBuffer.wrap(a);
                    for(int i = 0; i < 500; i++){
                        suma += b.getInt();
                        System.out.println(b.getInt());
                    }
                    System.out.println(suma);
                    salida.close();
                    entrada.close();
                    conexion.close();
                }
                catch(Exception e){
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

	public static void main(String [] args) throws Exception{
            ServerSocket servidor = new ServerSocket(50000);
            for(;;){
                Socket conexion = servidor.accept();
                Worker w = new Worker(conexion);
                w.start();
            }
	}
}