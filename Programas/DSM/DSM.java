/*  Compilación: javac DSM.java
    Ejecución: java TokenDSM <nodo> <ip:puerto> <ip:puerto> <ip:puerto> <ip:puerto>
        java DSM 0 localhost:500 localhost:501 localhost:502 localhost:503
        java DSM 1 localhost:500 localhost:501 localhost:502 localhost:503
        java DSM 2 localhost:500 localhost:501 localhost:502 localhost:503
        java DSM 3 localhost:500 localhost:501 localhost:502 localhost:503
*/
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

// 1. Se deberá implementar las operaciones lock y unlock utilizando exclusión mutua 
// distribuida (utilizar el algoritmo de Ricart o el algoritmo de token-ring, 
// los cuales implementamos en actividades anteriores).

public class DSM {
    static int nodo;
    static String[] host;
    static int[] puertos;
    static int num_nodos;
    static int tam = 5;
    static boolean requieroR = false;
    static boolean tokenActivo = false;
    static Object obj = new Object();
    static long[] M = new long[tam];
    static boolean[] B = new boolean[tam];

    static void sendMessage(long t, String host, int puerto) throws Exception{
        Socket conexion = null;
        for(;;){
            try{
                conexion = new Socket(host, puerto);
                break;
            }catch(Exception e){
                Thread.sleep(100);
            }
        }
        ObjectOutputStream outInfo = new ObjectOutputStream(conexion.getOutputStream());
        outInfo.writeObject(t);
        if(t == -2){
            outInfo.writeObject(M);
            outInfo.writeObject(B);
        }
        conexion.close();
    }

    static void sendMessageNext(long token) throws Exception{
        Socket conexion = null;
        int sigNodo = nodo != num_nodos - 1 ? nodo + 1 : 0;
        sendMessage(token, host[sigNodo], puertos[sigNodo]);
    }

    static void envia_mensaje_todos(long t) throws Exception{
        for(int i = 0; i < num_nodos; i++){
            if(i != nodo){
                sendMessage(t, host[i], puertos[i]);
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
                ObjectInputStream inInfo = new ObjectInputStream(cl.getInputStream());
                long token = (long)inInfo.readObject();
                if(token == -2){
                    long[] m = new long[tam];
                    boolean[] b = new boolean[tam];
                    m = (long[])inInfo.readObject();
                    b = (boolean[])inInfo.readObject();
                    for (int i = 0; i < b.length; i++)
                        if(b[i])
                            M[i] = m[i];
                }
                if(token > 0){
                    //System.out.println(token);
                    synchronized(obj){
                        tokenActivo = true;
                    }
                    Thread.sleep(10);
                    while(requieroR) Thread.sleep(1);

                    sendMessageNext(token + 1);
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
                for(;;){
                    Socket cl = servidor.accept();
                    Worker wkr = new Worker(cl);
                    wkr.start();
                }
            }
            catch(Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // 5. En la operación lock se deberá asignar false a todos los elementos del arreglo B.
    static void lock() throws Exception{
        synchronized(obj){
            requieroR = true;
        }
        //System.out.println("lock()");
        for (int i = 0; i < B.length; i++) {
            B[i] = false;
        }
    }
    
    // 6. Cuando se ejecute unlock, antes de desbloquear, se deberá enviar los cambios
	// realizados en el arreglo M al resto de nodos.
    static void unlock()  throws Exception{
        envia_mensaje_todos(-2);
        synchronized(obj){
            requieroR = false;
        }
        //System.out.println("unlock()");
    }

    static long read(int n){
        return M[n];
    }

    static void write(int n,long v){
       M[n]=v;
       B[n]=true;
    }

    public static void main(String[] args) throws Exception{
        if(args.length < 2){
            System.err.println("java TokenDSM <nodo> <ip:puerto>");
            System.exit(1);
        }

        // 2. Cada nodo creará un arreglo M de enteros de 64 bits, el cual representará la memoria compartida.

        // 3. Cada nodo creará un arreglo B de booleanos los cuales indicarán qué elemento
        // del arreglo de enteros fue modificado dentro de un bloque lock-unlock.
        for(int i = 0; i < B.length; i++){
            B[i] = false;
            M[i] = 0;
        }

        nodo = Integer.valueOf(args[0]);
        num_nodos = args.length - 1;

        host = new String[num_nodos];
        puertos = new int[num_nodos];

        String[] ip_puerto;

        for(int i = 0; i < num_nodos; i++){
            ip_puerto = args[i + 1].split(":");
            host[i] = ip_puerto[0];
            puertos[i] = Integer.valueOf(ip_puerto[1]);
        }

        // 4. Cada nodo implementará las operaciones Read, Write, Lock y Unlock. 
		// La operación Read(n) leerá del arreglo M el elemento n, 
		// La operación Write(n,valor) escribirá el valor en el elemento n del arreglo M. 
		// Cuando se ejecute la operación Write(n,valor) se deberá asignar true al elemento n del arreglo B.
        Servidor server = new Servidor(nodo);
        server.start();

        envia_mensaje_todos(-1);

        if(nodo == 0)
            sendMessage(1, host[1], puertos[1]);
        
        long r;
        int itr = 0;
		// 7. Ejecutar el programa en cuatro nodos, en cada nodo se ejecutará:
        while(itr < 100){
        	// 7.1 Una barrera que espere que todos los nodos se encuentran en ejecución.
            Thread.sleep(1);
            if(tokenActivo){
            	// 7.2 Ejecutar las siguientes instrucciones en un ciclo de 100 iteraciones:
				// Lock()
				// r=Read(0)
				// r++
				// Write(0,r)
				// Unlock()
                lock();
                r = read(0);
                r++;
                write(0, r);
                unlock();
                System.out.println(read(0));
                Thread.sleep(10);
                itr++;
            }       
        }

        // 7.3 Al final de las iteraciones el nodo 0 ejecutará los siguiente:
			// Lock()
			// System.out.println(Read(0))
			// Unlock();
        lock();
        r = read(0);
        unlock();
        System.out.println("Resultado final es:" + read(0));
        //System.exit(0); 
    }
}
