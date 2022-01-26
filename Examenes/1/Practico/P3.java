import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class P3 {

    public static void main(String[] args) {

        try{
            Socket conexion = new Socket("sisdis.sytes.net", 50002);
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            int T1=1632746968;
            int T4=0;

            int T2 = entrada.readInt();
            System.out.println("T2: "+T2);
            int T3 = entrada.readInt();
            System.out.println("T3: "+T3);

            
            int Tres = (-T1-T3+T2+T4)/2;
            T4=T3+Tres;
            System.out.println("T4: "+T4);

            Thread.sleep(1000);
            conexion.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


}