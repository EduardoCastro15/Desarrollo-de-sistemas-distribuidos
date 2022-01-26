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
    private static final int N = ClaseRMI.N;
    
    static double[][] separa_matriz(double[][] A, int inicio){
        double[][] M = new double[N/3][N];
        for(int i = 0; i < N/3; i++)
            for(int j = 0; j < N; j++)
                M[i][j] = A[i + inicio][j];
        return M;
    }

    static void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna){
        for(int i = 0; i < N/3; i++)
            for(int j = 0; j < N/3; j++)
                C[i + renglon][j + columna] = c[i][j];
    }

    static void printMatrix(double matrix[][], int rows, int cols){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++)
                System.out.printf("%f", matrix[i][j]);
            System.out.println("");
        }
    }

    public static void main(String args[]) throws Exception{
        double A[][] = new double[N][N];
        double B[][] = new double[N][N];
        double C[][] = new double[N][N];
        double checksum = 0;
        
        // 1. Inicializar las matrices A y B.
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++){
                A[i][j] = 3 * i + j;
                B[i][j] = i - 4 * j;
                C[i][j] = 0;
            }
                
        // 2. Transponer la matriz B.
        for (int i = 0; i < N; i++)
            for (int j = 0; j < i; j++){
                double t = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = t;
            }
        
        double[][] A1 = separa_matriz(A, 0);
        double[][] A2 = separa_matriz(A, N/3);
        double[][] A3 = separa_matriz(A, 2*N/3);
        double[][] B1 = separa_matriz(B, 0);
        double[][] B2 = separa_matriz(B, N/3);
        double[][] B3 = separa_matriz(B, 2*N/3);

        //String url = "rmi://localhost/prueba"; //ip privada del servidor
        //InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);
        InterfaceRMI nodo1 = (InterfaceRMI) Naming.lookup("rmi://10.0.0.5/prueba");
        InterfaceRMI nodo2 = (InterfaceRMI) Naming.lookup("rmi://10.0.0.6/prueba");
        InterfaceRMI nodo3 = (InterfaceRMI) Naming.lookup("rmi://10.0.0.7/prueba");
        
        double[][] C1 = nodo1.multiplica_matrices(A1, B1, N);
        double[][] C2 = nodo1.multiplica_matrices(A1, B2, N);
        double[][] C3 = nodo1.multiplica_matrices(A1, B3, N);
        double[][] C4 = nodo2.multiplica_matrices(A2, B1, N);
        double[][] C5 = nodo2.multiplica_matrices(A2, B2, N);
        double[][] C6 = nodo2.multiplica_matrices(A2, B3, N);
        double[][] C7 = nodo3.multiplica_matrices(A3, B1, N);
        double[][] C8 = nodo3.multiplica_matrices(A3, B2, N);
        double[][] C9 = nodo3.multiplica_matrices(A3, B3, N);
        
        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, N/3);
        acomoda_matriz(C, C3, 0, 2*N/3);
        acomoda_matriz(C, C4, N/3, 0);
        acomoda_matriz(C, C5, N/3, N/3);
        acomoda_matriz(C, C6, N/3, 2*N/3);
        acomoda_matriz(C, C7, 2*N/3, 0);
        acomoda_matriz(C, C8, 2*N/3, N/3);
        acomoda_matriz(C, C9, 2*N/3, 2*N/3);

        if(N == 9){
            System.out.println("Matriz A:");
            printMatrix(A, N, N);
            System.out.println("Matriz B:");
            printMatrix(B, N, N);
            System.out.println("Matriz B^T:");
            printMatrix(B, N, N);
            System.out.println("Matriz C:");
            printMatrix(C, N, N);
        }
        
        //Calcular y desplegar el checksum (N = 9    -> -135108.0)
        //Calcular y desplegar el checksum (N = 3000 -> -7.0826408326101299E17)

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                checksum += C[i][j];
        System.out.println("Checksum: " + checksum);
    }    
}