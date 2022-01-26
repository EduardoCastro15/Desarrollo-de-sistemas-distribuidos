import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

class pi{
    static Object obj = new Object();
    static float pi = 0;
    
    static class Worker extends Thread{
        Socket conexion;

        Worker(Socket conexion){
            this.conexion = conexion;
        }
        @Override
        public void run(){
            // Algoritmo 1
            try {
                // 1. Crear streams de entrada y salida.
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                // 2. Declarar la variable "suma" de tipo float.
                float suma = 0;
                // 3. Recibir en la variable "suma" la suma calculada por el cliente.
                suma = entrada.readFloat();
                System.out.println("Recibi la suma calculada por el cliente: " + suma);
                // 4. En un bloque synchronized utilizar el objeto "obj":
                synchronized(obj){
                    // 4.1 Asignar a la variable "pi" la expresion: suma + pi
                    pi = suma + pi;
                }
                // 5. Cerrar los streams de entrada y salida.
                entrada.close();
                salida.close();
                // 6. Cerrar la conexion "conexion".
                conexion.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
  
    public static void main(String[] args) throws Exception {
        if (args.length != 1){
            System.err.println("Uso:");
            System.err.println("java PI <nodo>");
            System.exit(0);
        }
        int nodo = Integer.valueOf(args[0]);
        if (nodo == 0){
            // Algoritmo 2
    
            // 1. Declarar una variable "servidor" de tipo ServerSocket.
            // 2. Crear un socket servidor utilizando el puerto 40000 y asignarlo a la variable "servidor".
            ServerSocket servidor = new ServerSocket(40000);
            // 3. Declarar un vector "v" de tipo Worker con 4 elementos.
            Worker[] v = new Worker[4];
            // 4. Declarar una variable entera "i" y asignarle cero.
            int i = 0;
            // 5. En un ciclo
            // 5.1 Si la variable "i" es igual a 4, entonces salir del ciclo.
            while (i != 4){
                // 5.2 Declarar una variable "conexion" de tipo Socket.
                Socket conexion;        
                // 5.3 Invocar el metodo servidor.accept() y asignar el resultado a la variable "conexion".
                conexion = servidor.accept();
                // 5.4 Crear una instancia de la clase Worker, pasando como parametro la variable "conexion". 
                // Asignar la instancia al elemento v[i].
                v[i] = new Worker(conexion);
                // 5.5 Invocar el metodo v[i].start()
                v[i].start();
                // 5.6 Incrementar la variable "i".
                i++;
                // 5.7 Ir al paso 5.1
            }
            // 6. Declarar una variable "i" entera y asignarle cero.
            i = 0;
            // 7. En un ciclo:
            // 7.1 Si la variable "i" es igual a 4, entonces salir del ciclo.
            while (i != 4){
                // 7.2 Invocar el metodo v[i].join()
                v[i].join();
                // 7.3 Incrementar la variable "i".
                i++;
                // 7.4 Ir al paso 11.1
            }
            servidor.close();
            // 8. Desplegar el valor de la variable "pi".
            System.out.println("El valor de pi es: " + pi);
        
        }else{
            // Algoritmo 3
            
            // 1. Declarar la variable "conexion" de tipo Socket y asignarle null.
            Socket conexion = null;
            // 2. Realizar la conexion con el servidor implementando re-intento. Asignar el socket a la variable "conexion".
            for(;;)
                try{
                    conexion = new Socket("localhost",40000);
                    break;
                }catch (Exception e){
                    Thread.sleep(100);
                }
            // 3. Crear los streams de entrada y salida.
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            // 4. Declarar la variable "suma" de tipo float y asignarle cero.
            float suma = 0;
            // 5. Declarar una variable "i" de tipo entero y asignarle cero.
            int i = 0;
            // 6. En un ciclo:
            // 6.1 Si la variable "i" es igual a 1000000, entonces salir del ciclo.
            while (i != 1000000){
                // 6.2 Asignar a la variable "suma" la expresion:  4.0/(8*i+2*(nodo-1)+3)+suma
                suma += 4.0 / ( 8 * i + 2 * (nodo - 2) + 3);
                // 6.3 Incrementar la variable "i".
                i++;
                // 6.4 Ir al paso 6.1
            }
    
            // 7. Asignar a la variable "suma" la expresion:  nodo%2==0?suma:-suma
            suma=  (nodo%2 == 0)? - suma:suma;
            //suma = nodo % 2 == 0 ? suma: (-1) * suma;
            // 8. Enviar al servidor el valor de la variable "suma".
            salida.writeFloat(suma);
            // 9. Cerrar los streams de entrada y salida.
            entrada.close();
            salida.close();
            // 10. Cerrar la conexion "conexion".        
            conexion.close();
        }
    }
}