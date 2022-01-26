import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

//java Ricart 0 localhost:50000 localhost:50001 localhost:50002 

enum Estado{
	Normal, EsperandoRecurso, AdquirioRecurso;
}

class Ricart{

	static String [] hosts;
	static int [] puertos;
	static int num_nodos;
	static int nodo;

	static long reloj_logico;
	static Object lock = new Object();

	static LinkedList<Integer> cola = new LinkedList<Integer>();

	static int num_ok_recibidos;
	static long tiempo_logico_enviado;

	static Estado estado = Estado.Normal;


	static class Reloj extends Thread{

		
		Reloj(){

		}

		public void run(){
			for(;;){

				try{

					synchronized(lock){
						System.out.println("Reloj logico: " + reloj_logico);

						if(nodo == 0) reloj_logico += 4;
						else if(nodo == 1) reloj_logico += 5;
						else if(nodo == 2) reloj_logico += 6;

					}
					

					Thread.sleep(1000);


				}catch(Exception e){
					e.printStackTrace();
				}
			
				
			}
		}
	}



	static class Worker extends Thread{
		Socket conexion;
		Worker(Socket conexion){
			this.conexion = conexion;
		}

		public void run(){
			try{
				System.out.println("Inicio el thread worker");

				//Recepción del mensaje
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());			
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());

				String comando = entrada.readUTF();
				System.out.println("Comando: " + comando);

				if(!comando.equals("HELO")){

					if(comando.equals("RQS")){
						int id_recurso = entrada.readInt();
						int nodo_recibido = entrada.readInt();
						long tiempo_recibido = entrada.readLong();

						if(estado == Estado.Normal){
							envia_ok(reloj_logico, hosts[nodo_recibido], puertos[nodo_recibido]);
						}else if(estado == Estado.EsperandoRecurso){
							if(tiempo_recibido < reloj_logico){
								envia_ok(reloj_logico, hosts[nodo_recibido], puertos[nodo_recibido]);
							}else if(tiempo_recibido > reloj_logico){
								cola.add(nodo_recibido);	
							}else if(tiempo_recibido == reloj_logico){
								if(nodo_recibido < nodo){
									envia_ok(reloj_logico, hosts[nodo_recibido], puertos[nodo_recibido]);
								}else{
									cola.add(nodo_recibido);
								}
							}
						}else if(estado == Estado.AdquirioRecurso){

							cola.add(nodo_recibido);
						}


					}else if(comando.equals("OK")){
						long tiempo_recibido = entrada.readLong();
						num_ok_recibidos++;
						if(num_ok_recibidos == (num_nodos-1)){
							System.out.println("Adquirio el recurso");
							estado = Estado.AdquirioRecurso;
						}
					}

				}else{

					long tiempo_recibido = entrada.readLong();

					if(tiempo_recibido > 0){
						synchronized(lock){
							if(tiempo_recibido >= reloj_logico){
								reloj_logico = tiempo_recibido+1;	
							}
						}
					}
				}


				salida.close();
				entrada.close();
				conexion.close();				

			}catch(Exception e){
					e.printStackTrace();
			}
			
			
		}
	}

	static class Servidor extends Thread{
		ServerSocket server;
		Servidor(ServerSocket server){
			this.server = server;
		}

		public void run(){

			try{
				for(;;){
					Socket conexion = this.server.accept();

					Worker w = new Worker(conexion);
					w.start();
				}
			}catch(Exception e){
					e.printStackTrace();
			}
			

		}
	}

	static void envia_helo(String host, int puerto)throws Exception{

		Socket conexion = null;

		for(;;)
            try{
                conexion = new Socket(host, puerto);
                break;
            }catch(Exception e){
                System.out.println("Esperando servidor...");
                Thread.sleep(100);
            }

        try{
        	DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());	
        	salida.writeUTF("HELO");
        	salida.writeLong(reloj_logico);
        }finally{
        	conexion.close();
        }


	}

	static void envia_peticion(int id_recurso, int nodo, long tiempo_logico, String host, int puerto) throws Exception
	{
		Socket conexion = new Socket(host, puerto);

		try{
			DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
			salida.writeUTF("RQS");
			salida.writeInt(id_recurso);
			salida.writeInt(nodo);
			salida.writeLong(tiempo_logico);
		}finally{
			conexion.close();
		}
	}


	static void envia_ok(long tiempo_logico, String host, int puerto) throws Exception
	{
		Socket conexion = new Socket(host, puerto);

		try{
			DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
			salida.writeUTF("OK");
			salida.writeLong(tiempo_logico);
		}finally{
			conexion.close();
		}
	}

	static void bloquea() throws Exception
	{
		System.out.println("Bloquea");
		estado = Estado.EsperandoRecurso;

		num_ok_recibidos = 0;
		tiempo_logico_enviado = reloj_logico;

		for(int i = 0; i < num_nodos; i++){
			if(i != nodo){
				envia_peticion(1, nodo, reloj_logico, hosts[i], puertos[i]);
			}

		}
	}

	static void desbloquea() throws Exception
	{
		System.out.println("Desbloquea");
		estado = Estado.Normal;

		while(cola.size() > 0){
			int nodo = cola.removeFirst();
			envia_ok(reloj_logico, hosts[nodo], puertos[nodo]);
		}
	}


	public static void main(String[] args) throws Exception{

		nodo = Integer.valueOf(args[0]);
		num_nodos = args.length - 1;

		hosts = new String[Integer.valueOf(num_nodos)];
		puertos = new int[Integer.valueOf(num_nodos)];

		//System.out.println(num_nodos);

		for(int i = 0; i < num_nodos; i++){
			hosts[i] = args[i+1].split(":")[0];
			puertos[i] = Integer.valueOf(args[i+1].split(":")[1]);
		}

		//System.out.println(puertos[nodo]);
		ServerSocket server = new ServerSocket(puertos[nodo]);
		Servidor servidor = new Servidor(server);
		servidor.start();

		for(int i = 0; i < num_nodos; i++){
			if(i != nodo){
				envia_helo(hosts[i], puertos[i]);
			}
		}


		Reloj r = new Reloj();
		r.start();

		Thread.sleep(1000);
		bloquea();
		while (estado != Estado.AdquirioRecurso) Thread.sleep(100);
		Thread.sleep(3000);
		desbloquea();

		//servidor.join();

	}

}


