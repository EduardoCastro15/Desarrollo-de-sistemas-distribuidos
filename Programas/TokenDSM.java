import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class TokenDSM {
    static Object obj = new Object();
    static int nodo;
    static String[] host;
    static int[] puertos;
    static int num_nodos;
    static boolean requieroR=false;
    static boolean tokenActivo=false;
    static int sizeA = 10, okAll;
    static long[] M= new long[sizeA];
    static boolean[] B=new boolean[sizeA];

    static void envia_mensaje(long t,String host,int puerto) throws Exception{
        Socket conexion = null;
        for(;;){
            try{
                conexion = new Socket(host,puerto);
                break;
            }catch(Exception e){
                Thread.sleep(100);
            }
        }
        ObjectOutputStream outInfo = new ObjectOutputStream(conexion.getOutputStream());
        outInfo.writeObject(t);
        if(t==-2){
            outInfo.writeObject(M);
            outInfo.writeObject(B);
        }
        conexion.close();
    }

    static void envia_mensaje_siguiente_disponible(long token) throws Exception{
        Socket conexion = null;
        int sigNodo = nodo!=num_nodos-1 ? nodo + 1 : 0;
        envia_mensaje(token, host[sigNodo],puertos[sigNodo]);
    }

    static void envia_mensaje_todos(long t) throws Exception{
        for(int i=0;i<num_nodos;i++){
            if(i!=nodo){
                envia_mensaje(t, host[i], puertos[i]);
            } 
        }
    }

    static class Worker extends Thread{
        Socket cl;
        Worker(Socket cl){
            this.cl = cl;
        }
        public void run(){
            try{
                
                //System.out.println("Inicio el thread Worker");
                ObjectInputStream inInfo = new ObjectInputStream(cl.getInputStream());
                //DataOutputStream outInfo = new DataOutputStream(cl.getOutputStream());
                long token = (long)inInfo.readObject();
                if(token == -2){
                    long[] m = new long[sizeA];
                    boolean[] b = new boolean[sizeA];
                    m = (long[])inInfo.readObject();
                    b = (boolean[])inInfo.readObject();
                    for (int i = 0; i < b.length; i++) {
                        if(b[i])
                            M[i]=m[i];
                    }
                }
                if(token > 0){
                    //System.out.println("Token: "+token);
                    synchronized(obj){
                        tokenActivo = true;
                    }
                    Thread.sleep(10);
                    while(requieroR) Thread.sleep(1);

                    envia_mensaje_siguiente_disponible(token+1);
                    synchronized(obj){
                        tokenActivo = false;
                    }
                    
                }
                cl.close();
            }
            catch(Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static class Servidor extends Thread{
        int nodo;
        Servidor(int nodo){
            this.nodo = nodo;
        }
        public void run(){
            try{
                ServerSocket servidor = new ServerSocket(puertos[nodo]);
                System.out.println("Servidor activo");
                for(;;){
                    Socket cl = servidor.accept(); //Cliente aceptado
                    Worker wkr = new Worker(cl);
                    wkr.start();
                }
            }
            catch(Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static void lock() throws Exception{
        synchronized(obj){
            requieroR = true;
        }
        System.out.println("Recurso Bloqueado");
        for (int i = 0; i < B.length; i++) {
            B[i] = false;
        }
    }
    
    static void unlock()  throws Exception{
        envia_mensaje_todos(-2);
        synchronized(obj){
            requieroR = false;
        }
        System.out.println("Recurso Desbloqueado");
    }

    static long read(int n){
        return M[n];
    }

    static void write(int n,long v){
       M[n]=v;
       B[n]=true;
    }

    public static void main(String[] args) throws Exception{
        if (args.length < 2){
            System.err.println("java TokenDSM <nodo> <ip:puerto>");
            System.exit(1);
        }
        for (int i = 0; i < B.length; i++) {
            B[i] = false;
            M[i] = 0;
        }

        nodo = Integer.valueOf(args[0]);
        num_nodos = args.length-1;

        host = new String[num_nodos];
        puertos = new int[num_nodos];

        String[] ip_puerto;

        for(int i=0;i<num_nodos;i++){
            ip_puerto = args[i+1].split(":");
            host[i] = ip_puerto[0];
            puertos[i] = Integer.valueOf(ip_puerto[1]); 
            
        }
        Servidor server = new Servidor(nodo);
        server.start(); 
       
        envia_mensaje_todos(-1);
        System.out.println("Todos los nodos ya estan activos");

        if(nodo==0)
            envia_mensaje(1, host[1], puertos[1]);
        
        long r;
        int itr=0;
        while(itr<100){
            Thread.sleep(1);            
            if(tokenActivo){
                lock();
                r=read(0);
                r++;
                write(0,r);
                unlock();
                Thread.sleep(10);
                itr++;
            }       
        }
        
        //envia_mensaje_todos(-3); //Ya acabe iteraciones
        //while(okAll!=num_nodos-1)
        
        lock();
        r=read(0);
        unlock();
        System.out.println("M[0]="+r);
        //System.exit(0); 
    }
}
