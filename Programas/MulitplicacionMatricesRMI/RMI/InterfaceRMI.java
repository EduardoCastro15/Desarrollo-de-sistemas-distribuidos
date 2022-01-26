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
    //public int[][] separa_matriz(int[][] A, int inicio) throws RemoteException;
    //public void acomoda_matriz(int[][] C, int[][] c, int renglon, int columna) throws RemoteException;
    public int[][] multiplica_matrices(int[][] A, int[][] B, int N) throws RemoteException;
}