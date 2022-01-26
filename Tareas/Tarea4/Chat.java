import java.io.*; 
import java.net.*;

public class Chat{ 
	static class Worker extends Thread{
		@Override
		public void run(){
			// En un ciclo infinito se recibirán los mensajes enviados al group 
			// 230.0.0.0 a través del puerto 40000 y se desplegarán en la pantalla.
			do{
				try{
					InetAddress group = InetAddress.getByName("230.0.0.0");
					MulticastSocket puerto = new MulticastSocket(40000);
					puerto.joinGroup(group);
					byte[] mesage = recibe_mensaje_multicast(puerto, 100);
					System.out.println(new String(mesage, "IBM850"));
					puerto.leaveGroup(group);
					puerto.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}while(true);
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception{
		Worker w = new Worker();
		w.start();
		String name = args[0];
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));        
		// En un ciclo infinito se leerá los mensajes del teclado y se enviarán al
		// group 230.0.0.0 a través del puerto 40000.
		do{
			String mesage = buffer.readLine();
			String salida = name + ":" + mesage;
			envia_mensaje_multicast(salida.getBytes(), "230.0.0.0", 40000); 
		}while(true);
	}
	static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException{
		try{
			DatagramSocket socket = new DatagramSocket();
			InetAddress group = InetAddress.getByName(ip);  
			DatagramPacket package = new DatagramPacket(buffer, buffer.length, group, puerto);
			socket.send(package);
			socket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException{
		byte[] buffer = new byte[longitud_mensaje];
		DatagramPacket package = new DatagramPacket(buffer, buffer.length);
		socket.receive(package);
		return buffer;
	}
}