
class Matriz{
	static Object lock = new Object();
	static int N = 10;
	static int i = 0, j = 0, k = 0;
	static int[][] A;
	static int[][] B;
	static int[][] C;

	static int[][] separa_matriz(int[][] A, int inicio){
		int[][] M = new int[N/2][N];
		for(int i = 0; i < N/2; i++)
			for(int j = 0; j < N; j++)
				M[i][j] = A[i + inicio][j];
		return M;
	}

	static int[][] multiplica_matrices(int[][] A, int[][] B){
		int[][] C = new int[N/2][N/2];
		for(int i = 0; i < N/2; i++)
			for(int j = 0; j < N/2; j++)
				for(int k = 0; k < N; k++)
					C[i][j] += A[i][k] * B[j][k];
		return C;
	}

	static void acomoda_matriz(int[][] C, int[][] c, int renglon, int columna){
		for(int i = 0; i < N/2; i++)
			for(int j = 0; j < N/2; j++)
				C[i + renglon][j + columna] = c[i][j];
	}

	public static void main(String[] args) throws Exception {
		N = 6;
		//Crear las matrices A, B, C
		A = new int[N][N];
		B = new int[N][N];
		C = new int[N][N];

		int checksum = 0;
		// 1. Inicializar las matrices A y B.
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++){
				A[i][j] = 2 * i - j;
				B[i][j] = i + 2 * j;
				C[i][j] = 0;
			}
		// 2. Transponer la matriz B.
		for (int i = 0; i < N; i++)
			for (int j = 0; j < i; j++){
				int t = B[i][j];
				B[i][j] = B[j][i];
				B[j][i] = t;
			}
		
		int[][] A1 = separa_matriz(A, 0);
		int[][] A2 = separa_matriz(A, N/2);
		int[][] B1 = separa_matriz(B, 0);
		int[][] B2 = separa_matriz(B, N/2);
		
		int[][] C1 = multiplica_matrices(A1, B1);
		int[][] C2 = multiplica_matrices(A1, B2);
		int[][] C3 = multiplica_matrices(A2, B1);
		int[][] C4 = multiplica_matrices(A2, B2);
		
		acomoda_matriz(C, C1, 0, 0);
		acomoda_matriz(C, C2, 0, N/2);
		acomoda_matriz(C, C3, N/2, 0);
		acomoda_matriz(C, C4, N/2, N/2);
		
		//Calcular y desplegar el checksum (3420)

		for (i = 0; i < N; i++)
			for (j = 0; j < N; j++)
				checksum += C[i][j];
		System.out.println("Checksum: " + checksum + " ");
	}




}
