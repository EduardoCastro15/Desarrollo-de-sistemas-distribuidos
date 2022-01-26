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
    public static final int N = 9;

    public ClaseRMI() throws RemoteException{
        super();
    }

    public double[][] multiplica_matrices(double[][] A, double[][] B, int N) throws RemoteException{
        double[][] C = new double[N/3][N/3];
        for(int i = 0; i < N/3; i++)
            for(int j = 0; j < N/3; j++)
                for(int k = 0; k < N; k++)
                    C[i][j] += A[i][k] * B[j][k];
        return C;
    }
}