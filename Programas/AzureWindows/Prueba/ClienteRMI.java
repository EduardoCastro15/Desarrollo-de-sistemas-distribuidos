/*Orden de ejecución y compilación
Compilar los 4 archivos:
    javac InterfaceRMI.java
    javac ClaseRMI.java
    javac ClienteRMI.java
    javac ServidorRMI.java
Ejecutar en consola:
    rmiregistry
Ejecutar en otra consola:
    java ServidorRMI
Ejecutar en otra consola:
    java ClienteRMI
Importante compilar con la version 8 de Java*/
import java.rmi.Naming;

public class ClienteRMI {
    public static void main(String args[]) throws Exception{
        String url = "rmi://localhost/prueba"; //ip privada 
        InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);
        System.out.println(r.mayusculas("hola"));
        System.out.println("suma=" + r.suma(10, 20));
        int[][] m = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        System.out.println("checksum=" + r.checksum(m));
    }    
}