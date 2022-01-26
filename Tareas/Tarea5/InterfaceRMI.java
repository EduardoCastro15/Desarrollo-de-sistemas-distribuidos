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
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
    public double[][] multiplica_matrices(double[][] A, double[][] B, int N) throws RemoteException;
}