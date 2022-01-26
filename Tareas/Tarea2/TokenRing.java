import java.net.*;
import java.io.*;

class TokenRing{
	static String ip;
	static long token = 0;
	static int nodo;
	static boolean inicio = true;
	static DataInputStream entrada;
	static DataOutputStream salida;

	static class Worker extends Thread{
                @Override
		public void run(){
			// Algoritmo 1
			try{
				// 1.1 Declarar la variable servidor de tipo ServerSocket
				ServerSocket servidor;
				// 1.2 Asignar a la variable servidor el objeto: new ServerSocket(20000 + nodo)
				servidor = new ServerSocket(20000 + nodo);
				// 1.3 Declarar la variable conexion de tipo Socket.
				Socket conexion;
				// 1.4 Asignar a la variable conexion el objeto servidor.accept().
				conexion = servidor.accept();
				// 1.5 Asignar a la variable entrada el objeto new DataInputStream(conexion.getInputStream())
				entrada = new DataInputStream(conexion.getInputStream());
			}
			// 2. En el bloque catch desplegar el mensaje de la excepción
			catch(IOException e){
				e.printStackTrace();
			}      
		}
	}

	public static void main(String[] args) throws Exception{
		if(args.length != 2){
			System.err.println("#Nodo + nextIP");
			System.exit(1);
		}
		nodo = Integer.valueOf(args[0]);
		ip = args[1];
		// Algoritmo 2
		// 1. Declarar la variable w de tipo Worker.
		Worker w;
		// 2. Asignar a la variable w el objeto new Worker().
		w = new Worker();
		// 3. Invocar el método w.start().
		w.start();
		// 4. Declarar la variable conexion de tipo Socket. Asignar null a la variable conexion.
		Socket conexion = null;
		// 5. En un ciclo:
		while(true){
			// 5.1 En un bloque try:
			try{
				// 5.1.1 Asignar a la variable conexion el objeto Socket(ip, 20000 + (nodo + 1) % 4).
				conexion = new Socket(ip, 20000 + (nodo + 1) % 4);
				// 5.1.2 Ejecutar break para salir del ciclo.
				break;
			} 
			// 5.2 En el bloque catch:
			catch(IOException e){
				// 5.2.1 Invocar el método Thread.sleep(500).
				Thread.sleep(500);
		  	}
		}    
		// 6. Asignar a la variable salida el objeto new DataOutputStream(conexion.getOutputStream()).
		salida = new DataOutputStream(conexion.getOutputStream());
		// 7. Invocar el método w.join().
		w.join();
		// 8. En un ciclo:
		while(true){
			// 8.1 Si la variable nodo es cero:
			if(nodo == 0){
				// 8.1.1 Si la variable inicio es true:
				if(inicio){
					// 8.1.1.1 Asignar false a la variable inicio.
					inicio = false;
				}
				// 8.1.2 Si la variable inicio es false:
				else{
					// 8.1.2.1 Asignar a la variable token el resultado del método entrada.readLong().
					token = entrada.readLong();
					// 8.1.2.2 Incrementar la variable token.
					token++;
					// 8.1.2.3 Desplegar las variables nodo y token.
					System.out.println("Nodo: " + nodo);
					System.out.println("Token: " + token);
				}        
			}
			// 8.2 De otra manera:
			else {
				// 8.2.1 Asignar a la variable token el resultado del método entrada.readLong().
				token = entrada.readLong();
				// 8.2.2 Incrementar la variable token.
				token++;
				// 8.2.3 Desplegar las variables nodo y token.
				System.out.println("Nodo: " + nodo);
				System.out.println("Token: " + token);
			}
			// 8.3 Si la variable nodo es cero y la variable token es mayor o igual a 1000:
			if(nodo == 0 && token >= 1000){
				// 8.3.1 Salir del ciclo.
				break;
			}
			// 8.5 Invocar el método salida.writeLong(token).    
			salida.writeLong(token);
		}
	}
}