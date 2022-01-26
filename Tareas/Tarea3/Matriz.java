import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

class Matriz{
	static Object lock = new Object();
	static int N = 10;
	static int i = 0, j = 0, k = 0;
	static long[][] A;
	static long[][] B;
	static long[][] C;
	
	static class Worker extends Thread{
		Socket conexion;
		Worker (Socket conexion){
			this.conexion = conexion;
		}
		@Override
		public void run(){
			try{
				long[][] A1 = new long[N/2][N];
				long[][] A2 = new long[N/2][N];
				long[][] B1 = new long[N/2][N]; // new long[N][N/2];
				long[][] B2 = new long[N/2][N]; // new long[N][N/2];
				DataInputStream entrada = new DataInputStream(conexion.getInputStream()); 
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
				int x = entrada.readInt();
				switch(x){
					case 1 -> {
						for (i = 0; i < (N/2); i++){
							for(j = 0; j < N; j++){
								A1 [i][j] = A [i][j];
								B1 [i][j] = B [i][j];
								// 3. Enviar la matriz A1 al nodo 1.
								salida.writeLong(A1[i][j]);
								// 4. Enviar la matriz B1 al nodo 1.
								salida.writeLong(B1[i][j]);
							}
						}
						if (N == 10){
							System.out.println("\n\n\n\n\nNodo 1 conectado...\nMatriz A1 enviada al nodo 1...");
							imprimir_matriz(A1,N/2,N);
							System.out.println("\nMatriz B1 enviada al nodo 1...");
							imprimir_matriz(B1,N/2,N);
						}
					}
					case 2 -> {
						for (i = 0; i < (N/2); i++){
							for (j = 0; j < N; j++){
								A1 [i][j] = A [i][j];
								// 5. Enviar la matriz A1 al nodo 2.
								salida.writeLong(A1[i][j]);
							}
						}
						for (i = (N/2); i < N; i++){
							for (j = 0; j < N; j++){
								B2 [i - (N/2)][j] = B [i][j];
								// 6. Enviar la matriz B2 al nodo 2.
								salida.writeLong(B2[i - (N/2)][j]);
							}
						}
						if (N == 10){
							System.out.println("\n\n\n\n\nNodo 2 conectado...\nMatriz A1 envaida al nodo 2...");
							imprimir_matriz(A1,N/2,N);
							System.out.println("\nMatriz B2 enviada al nodo 2...");
							imprimir_matriz(B2,N/2,N);
						}
					}
					case 3 -> {
						for (i = (N/2); i < N; i++){
							for (j = 0; j < N;j++){
								A2 [i - (N/2)][j] = A [i][j];
								// 7. Enviar la matriz A2 al nodo 3.
								salida.writeLong(A2[i - (N/2)][j]);
							}
						}
						for (i = 0; i < (N/2); i++){
							for(j = 0; j < N; j++){
								B1 [i][j] = B [i][j];
								// 8. Enviar la matriz B1 al nodo 3.
								salida.writeLong(B1[i][j]);
							}
						}
						if (N == 10){
							System.out.println("\n\n\n\n\nNodo 3 conectado...\nMatriz A2 enviada al nodo 3...");
							imprimir_matriz(A2,N/2,N);
							System.out.println("\nMatriz B1 enviada al nodo 3...");
							imprimir_matriz(B1,N/2,N);
						}
					}
					case 4 -> {                    
						for (i = (N/2); i < N; i++)
							for (j = 0; j < N;j++){
								A2 [i - (N/2)][j] = A [i][j];
								B2 [i - (N/2)][j] = B [i][j];
								salida.writeLong(A2[i - (N/2)][j]); // 9. Enviar la matriz A2 al nodo 4.
								salida.writeLong(B2[i - (N/2)][j]); // 10. Enviar la matriz B2 al nodo 4.
							}
						if (N == 10){
							System.out.println("\n\n\n\n\nNodo 4 conectado...\nMatriz A2 enviada al nodo 4...");
							imprimir_matriz(A2,N/2,N);
							System.out.println("\nMatriz B2 enviada al nodo 4...");
							imprimir_matriz(B2,N/2,N);
						}
					}
				}
				synchronized(lock){
					switch(x){
						// 11. Recibe la matriz C1 del nodo 1
						case 1 -> {
							long[][] C1 = new long[N/2][N/2];
							for (i = 0; i < (N/2); i++)
								for (j = 0; j < (N/2); j++){
									C1[i][j] = entrada.readLong();
									C[i][j] = C1[i][j];
								}
							if (N == 10){
								System.out.println("\nMatriz C1 recibida del nodo 1...");
								imprimir_matriz(C,N,N);
							} 
						}
						// 12. Recibe la matriz C2 del nodo 2
						case 2 -> {
							long[][] C2 = new long[N/2][N];
							for (i = 0; i < (N/2); i++)
								for (j = (N/2); j < N; j++){
									C2[i][j] = entrada.readLong();
									C[i][j] = C2[i][j];
								}
							if (N == 10){
								System.out.println("\nMatriz C2 recibida del nodo 2...");
								imprimir_matriz(C,N,N);
							}
						}
						// 13. Recibe la matriz C3 del nodo 3
						case 3 -> {
							long[][] C3 = new long[N][N/2];
							for (i = (N/2); i < N; i++)
								for (j = 0; j < (N/2); j++){
									C3[i - (N/2)][j] = entrada.readLong();
									C[i][j] = C3[i - (N/2)][j];
								}
							if (N == 10){
								System.out.println("\nMatriz C3 recibida del nodo 3...");
								imprimir_matriz(C,N,N);
							}
						}
						// 14. Recibe la matriz C4 del nodo 4
						case 4 -> {
							long[][] C4 = new long[N][N];
							for (i = (N/2); i < N; i++)
								for (j = (N/2); j < N; j++){
									C4[i - (N/2)][j - (N/2)] = entrada.readLong();
									C[i][j] = C4[i - (N/2)][j - (N/2)];
								}
							if (N == 10){
								System.out.println("\nMatriz C4 recibida del nodo 4...");
								imprimir_matriz(C,N,N);
							}
						}
					}
				}
				System.out.print("\nConexion finalizada con el nodo: " + x);
				entrada.close();
				salida.close();
				conexion.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws Exception {
		if (args.length != 2){
			System.err.println("java Matriz <nodo>");
			System.exit(0);
		}
		int nodo = Integer.valueOf(args[0]);
		String ip = args[1];
		// Nodo 0
		if (nodo == 0){
			A = new long[N][N];
			B = new long[N][N];
			C = new long[N][N];
			// 1. Inicializar las matrices A y B.
			for (i = 0; i < N; i++)
				for (j = 0; j < N; j++){
					A[i][j] = i + 3 * j;
					B[i][j] = i - 3 * j;
					C[i][j] = 0;
				}
			// 2. Transponer la matriz B.
			for (i = 0; i < N; i++)
				for (j = 0; j < i; j++){
					long t = B[i][j];
					B[i][j] = B[j][i];
					B[j][i] = t;
				}
			if (N == 10){
				System.out.println("\nMatriz A...");
				imprimir_matriz(A,N,N);
				System.out.println("\nMatriz B 'Transpuesta'...");
				imprimir_matriz(B,N,N);
			}
			System.out.println("\nEsperando por conexiones de los nodos...");
			ServerSocket servidor = new ServerSocket(5000);
			Worker[] w = new Worker[4];
			
			int s = 0;
			while (s != 4){
				Socket conexion = servidor.accept();
				w[s] = new Worker(conexion);
				w[s].start();
				s++;
			}
			int z = 0;
			while (z != 4){
				w[z].join();
				z++;
			}
			System.out.println("\nFin de las conexiones con los nodos... ");
			servidor.close();
			// 15. Calcular el checksum de la matriz C.
			long checksum = 0;
			for (i = 0; i < N; i++)
				for (j = 0; j < N; j++)
					checksum += C[i][j];
			// 16. Desplegar el checksum de la matriz C.
			System.out.println("Checksum: " + checksum + " ");
			// 17. Si N=10 entonces desplegar la matriz A, B, C
			if (N == 10){
				System.out.println("\nMatriz A...");
				imprimir_matriz(A,N,N);
				System.out.println("\nMatriz B...");
				imprimir_matriz(B,N,N);
				System.out.println("\nMatriz C = A x B...");
				for (i = 0; i < N; i++){
					for (j = 0; j < N; j++)
						System.out.print(C[i][j] + "\t");
					System.out.println("");
				}
				System.out.println("");                
			}
		}else{
			long[][] rA = new long[N/2][N];
			long[][] rB = new long[N/2][N];
			long[][] pC = new long[N/2][N/2];
			switch(nodo){
				// Nodo 1
				case 1 -> {
					Socket conexion = new Socket(ip, 50000);
					DataInputStream entrada = new DataInputStream(conexion.getInputStream());
					DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
					salida.writeInt(nodo);		 
					for (i = 0; i < (N/2); i++){
						for (j = 0; j < N; j++){
							// 1. Recibir del nodo 0 la matriz A1.
							rA[i][j] = entrada.readLong(); 
							 // 2. Recibir del nodo 0 la matriz B1.
							rB[i][j] = entrada.readLong(); 
						}
					}
					if (N == 10){
						System.out.println("\nMatriz A1 recibida del nodo 0...");
						imprimir_matriz(rA,N/2,N);
						System.out.println("\nMatriz B1 recibida del nodo 0...");
						imprimir_matriz(rB,N/2,N);
					}
					// 3. Realizar el producto C1=A1xB1 (renglon por renglon).
					for (i = 0; i < (N/2); i++)
						for (j = 0; j < (N/2); j++)
							for (k = 0; k < N; k++)
								pC[i][j] += rA[i][k] * rB[j][k];
					// 4. Enviar la matriz C1 al nodo 0.
					for (i = 0; i < (N / 2); i++){
						for (j = 0; j < (N / 2); j++){  
							salida.writeLong(pC[i][j]);
						}
					}
					if (N == 10){
						System.out.println("\nMatriz C1 enviada al nodo 0...");
						imprimir_matriz(pC,N/2,N/2);
					}
					entrada.close();
					salida.close();
					conexion.close();
				}
				// Nodo 2
				case 2 -> {
					Socket conexion = new Socket(ip, 50000);
					DataInputStream entrada = new DataInputStream(conexion.getInputStream());
					DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
					salida.writeInt(nodo);
					for (i = 0; i < (N/2); i++)
						for (j = 0; j < N; j++)
							// 1. Recibir del nodo 0 la matriz A1.
							rA[i][j] = entrada.readLong();
					for (i = (N/2); i < N; i++)
						for (j = 0; j < N; j++)
							// 2. Recibir del nodo 0 la matriz B2.
							rB[i-(N/2)][j] = entrada.readLong();
					if (N == 10){
						System.out.println("\nMatriz A1 recibida del nodo 0...");
						imprimir_matriz(rA,N/2,N);
						System.out.println("\nMatriz B2 recibida del nodo 0...");
						imprimir_matriz(rB,N/2,N);
					}
					// 3. Realizar el producto C2=A1xB2.
					for (i = 0; i < (N/2); i++)
						for (j = 0; j < (N/2); j++)
							for (k = 0; k < N; k++)
								pC[i][j] += rA[i][k] * rB[j][k];
					// 4. Enviar la matriz C2 al nodo 0.
					for (i = 0; i < (N / 2); i++)
						for (j = 0; j < (N / 2); j++)
							salida.writeLong(pC[i][j]);
					if (N == 10){ 
						System.out.println("\nMatriz C2 enviada al nodo 0...");
						imprimir_matriz(pC,N/2,N/2);
					}
					entrada.close();
					salida.close();
					conexion.close();
				}
				// Nodo 3
				case 3 -> {
					Socket conexion = new Socket(ip, 50000);
					DataInputStream entrada = new DataInputStream(conexion.getInputStream());
					DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
					salida.writeInt(nodo);
					for (i = (N/2); i < N; i++)
						for (j = 0; j < N; j++)
							// 1. Recibir del nodo 0 la matriz A2.
							rA[i-(N/2)][j] = entrada.readLong();
					for (i = 0; i < (N/2); i++)
						for (j = 0; j < N; j++){
							// rA[i-(N/2)][j] = entrada.readLong();
							// 2. Recibir del nodo 0 la matriz B1.
							rB[i][j] = entrada.readLong();
						}
					if (N == 10){
						System.out.println("\nMatriz A2 recibida del nodo 0...");
						imprimir_matriz(rA,N/2,N);
						System.out.println("\nMatriz B1 recibida del nodo 0...");
						imprimir_matriz(rB,N/2,N);
					}
					// 3. Realizar el producto C3=A2xB1.
					for (i = 0; i < (N/2); i++)
						for (j = 0; j < (N/2); j++)
							for (k = 0; k < N; k++)
								pC[i][j] += rA[i][k] * rB[j][k];
					// 4. Enviar la matriz C3 al nodo 0.
					for (i = 0; i < (N/2); i++)
						for (j = 0; j < (N/2); j++)
							salida.writeLong(pC[i][j]);
					if (N == 10){
						System.out.println("\nMatriz C3 enviada al nodo 0...");
						imprimir_matriz(pC,N/2,N/2);
					}
					entrada.close();
					salida.close();
					conexion.close();
				}
				//Nodo 4
				case 4 -> {
					Socket conexion = new Socket(ip, 50000);
					DataInputStream entrada = new DataInputStream(conexion.getInputStream());
					DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
					salida.writeInt(nodo);
					for (i = (N/2); i < N; i++)
						for (j = 0; j < N; j++){
							// 1. Recibir del nodo 0 la matriz A2.
							rA[i-(N/2)][j] = entrada.readLong();
							// 2. Recibir del nodo 0 la matriz B2.
							rB[i-(N/2)][j] = entrada.readLong();
						}
					if (N == 10){
						System.out.println("\nMatriz A2 recibida del nodo 0...");
						imprimir_matriz(rA,N/2,N);
						System.out.println("\nMatriz B2 recibida del nodo 0...");
						imprimir_matriz(rB,N/2,N);
					}
					// 3. Realizar el producto C4=A2xB2.
					for ( i = 0; i < (N/2); i++)
						for (j = 0; j < (N/2); j++)
							for (k = 0; k < N; k++)
								pC[i][j] += rA[i][k] * rB[j][k];
					// 4. Enviar la matriz C4 al nodo 0.
					for( i = 0; i < (N/2); i++)
						for( j = 0; j < (N/2); j++)
							salida.writeLong(pC[i][j]);
					if (N == 10){
						System.out.println("\nMatriz C4 enviada al nodo 0...");
						imprimir_matriz(pC,N/2,N/2);
					}
					entrada.close();
					salida.close();
					conexion.close();
				}
			}
		}
	}
	// Metodo para imprimir la matriz
	private static void imprimir_matriz(long[][] matriz, long filas, long columnas){
		for (i = 0; i< filas; i++){
			for (j = 0; j < columnas; j++)
				System.out.print(matriz[i][j] + "\t");
			System.out.println("");
		}
	}
}
