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
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI {
    public ClaseRMI() throws RemoteException {
        super();
    }
    public String mayusculas(String s) throws RemoteException {
        return s.toUpperCase();
    }
    public int suma(int a, int b) throws RemoteException {
        return a+b;
    }
    public long checksum(int[][] m) throws RemoteException {
        long s= 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                s += m[i][j];
            }
        }
        return s;
    }
}