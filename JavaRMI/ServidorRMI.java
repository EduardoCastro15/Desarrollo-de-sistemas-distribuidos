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

public class ServidorRMI {
    public static void main(String[] args)throws Exception {
        String url = "rmi://localhost/prueba";
        ClaseRMI obj = new ClaseRMI();
        Naming.rebind(url, obj);
    }
}