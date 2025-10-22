package MatrixProcessing;

import java.util.Scanner;
import java.util.Locale;

// --- Головний клас ---
public class MatrixProcessing {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);

        while (true) {
            printMenu();
            System.out.print("Your choice: > ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> MatrixOperations.addMatrices(sc);
                case 2 -> MatrixOperations.multiplyMatrixByConstant(sc);
                case 3 -> MatrixOperations.multiplyMatrices(sc);
                case 4 -> MatrixOperations.transposeMatrix(sc);
                case 5 -> Determinant.calculateDeterminant(sc);
                case 6 -> Determinant.inverseMatrix(sc);
                case 0 -> {
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. Add matrices");
        System.out.println("2. Multiply matrix by a constant");
        System.out.println("3. Multiply matrices");
        System.out.println("4. Transpose matrix");
        System.out.println("5. Calculate a determinant");
        System.out.println("6. Inverse matrix");
        System.out.println("0. Exit");
    }
}

// --- Клас для вводу/виводу матриць ---
class MatrixIO {
    public static double[][] readMatrix(Scanner sc, String sizePrompt, String matrixPrompt) {
        System.out.print(sizePrompt);
        int n = sc.nextInt();
        int m = sc.nextInt();
        System.out.println(matrixPrompt);

        double[][] matrix = new double[n][m];
        for (int i = 0; i < n; i++) {
            System.out.print("> ");
            for (int j = 0; j < m; j++)
                matrix[i][j] = sc.nextDouble();
        }
        return matrix;
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (int j = 0; j < row.length; j++) {
                if (Math.abs(row[j] - Math.round(row[j])) < 1e-9)
                    System.out.print(Math.round(row[j]));
                else
                    System.out.printf("%.2f", row[j]);
                if (j < row.length - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }
}

// --- Клас для основних операцій над матрицями ---
class MatrixOperations {

    public static void addMatrices(Scanner sc) {
        double[][] A = MatrixIO.readMatrix(sc, "Enter size of first matrix: > ", "Enter first matrix:");
        double[][] B = MatrixIO.readMatrix(sc, "Enter size of second matrix: > ", "Enter second matrix:");

        if (A.length != B.length || A[0].length != B[0].length) {
            System.out.println("The operation cannot be performed.");
            return;
        }

        int n = A.length, m = A[0].length;
        double[][] C = new double[n][m];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                C[i][j] = A[i][j] + B[i][j];

        System.out.println("The result is:");
        MatrixIO.printMatrix(C);
    }

    public static void multiplyMatrixByConstant(Scanner sc) {
        double[][] matrix = MatrixIO.readMatrix(sc, "Enter size of matrix: > ", "Enter matrix:");
        System.out.print("Enter constant: > ");
        double constant = sc.nextDouble();

        int n = matrix.length, m = matrix[0].length;
        double[][] result = new double[n][m];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                result[i][j] = matrix[i][j] * constant;

        System.out.println("The result is:");
        MatrixIO.printMatrix(result);
    }

    public static void multiplyMatrices(Scanner sc) {
        double[][] A = MatrixIO.readMatrix(sc, "Enter size of first matrix: > ", "Enter first matrix:");
        double[][] B = MatrixIO.readMatrix(sc, "Enter size of second matrix: > ", "Enter second matrix:");

        int n1 = A.length, m1 = A[0].length;
        int n2 = B.length, m2 = B[0].length;

        if (m1 != n2) {
            System.out.println("The operation cannot be performed.");
            return;
        }

        double[][] result = new double[n1][m2];

        for (int i = 0; i < n1; i++)
            for (int j = 0; j < m2; j++)
                for (int k = 0; k < m1; k++)
                    result[i][j] += A[i][k] * B[k][j];

        System.out.println("The result is:");
        MatrixIO.printMatrix(result);
    }

    public static void transposeMatrix(Scanner sc) {
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.print("Your choice: > ");
        int type = sc.nextInt();

        double[][] matrix = MatrixIO.readMatrix(sc, "Enter matrix size: > ", "Enter matrix:");

        double[][] result = switch (type) {
            case 1 -> transposeMainDiagonal(matrix);
            case 2 -> transposeSideDiagonal(matrix);
            case 3 -> transposeVertical(matrix);
            case 4 -> transposeHorizontal(matrix);
            default -> {
                System.out.println("Invalid transpose type.");
                yield null;
            }
        };

        if (result != null) {
            System.out.println("The result is:");
            MatrixIO.printMatrix(result);
        }
    }

    private static double[][] transposeMainDiagonal(double[][] m) {
        int n = m.length, k = m[0].length;
        double[][] t = new double[k][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < k; j++)
                t[j][i] = m[i][j];
        return t;
    }

    private static double[][] transposeSideDiagonal(double[][] m) {
        int n = m.length, k = m[0].length;
        double[][] t = new double[k][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < k; j++)
                t[k - 1 - j][n - 1 - i] = m[i][j];
        return t;
    }

    private static double[][] transposeVertical(double[][] m) {
        int n = m.length, k = m[0].length;
        double[][] t = new double[n][k];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < k; j++)
                t[i][k - 1 - j] = m[i][j];
        return t;
    }

    @SuppressWarnings("ManualArrayCopy")
    private static double[][] transposeHorizontal(double[][] m) {
        int n = m.length, k = m[0].length;
        double[][] t = new double[n][k];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < k; j++)
                t[n - 1 - i][j] = m[i][j];
        return t;
    }
}

// --- Клас для детермінанта та обчислення оберненої матриці ---
class Determinant {

    public static void calculateDeterminant(Scanner sc) {
        double[][] matrix = MatrixIO.readMatrix(sc, "Enter matrix size: > ", "Enter matrix:");
        if (matrix.length != matrix[0].length) {
            System.out.println("The operation cannot be performed.");
            return;
        }

        double det = determinant(matrix);
        System.out.println("The result is:");
        if (det == Math.round(det))
            System.out.println(Math.round(det));
        else
            System.out.println(det);
    }

    public static void inverseMatrix(Scanner sc) {
        double[][] matrix = MatrixIO.readMatrix(sc, "Enter matrix size: > ", "Enter matrix:");
        int n = matrix.length;
        if (n != matrix[0].length) {
            System.out.println("This matrix doesn't have an inverse.");
            return;
        }

        double det = determinant(matrix);
        if (det == 0) {
            System.out.println("This matrix doesn't have an inverse.");
            return;
        }

        double[][] inverse = invertMatrix(matrix);
        if (inverse == null) {
            System.out.println("This matrix doesn't have an inverse.");
            return;
        }

        System.out.println("The result is:");
        MatrixIO.printMatrix(inverse);
    }

    public static double determinant(double[][] m) {
        int n = m.length;
        if (n == 1) return m[0][0];
        if (n == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0];

        double det = 0;
        for (int col = 0; col < n; col++) {
            double[][] sub = new double[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                int subCol = 0;
                for (int j = 0; j < n; j++) {
                    if (j == col) continue;
                    sub[i - 1][subCol++] = m[i][j];
                }
            }
            det += Math.pow(-1, col) * m[0][col] * determinant(sub);
        }
        return det;
    }

    private static double[][] invertMatrix(double[][] m) {
        int n = m.length;
        double[][] a = new double[n][n];
        double[][] inv = new double[n][n];

        for (int i = 0; i < n; i++) {
            a[i] = m[i].clone();
            inv[i][i] = 1.0;
        }

        for (int i = 0; i < n; i++) {
            if (Math.abs(a[i][i]) < 1e-9) {
                boolean found = false;
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(a[j][i]) > 1e-9) {
                        double[] tmp = a[i]; a[i] = a[j]; a[j] = tmp;
                        double[] tmp2 = inv[i]; inv[i] = inv[j]; inv[j] = tmp2;
                        found = true;
                        break;
                    }
                }
                if (!found) return null;
            }

            double diag = a[i][i];
            for (int j = 0; j < n; j++) {
                a[i][j] /= diag;
                inv[i][j] /= diag;
            }

            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                double factor = a[k][i];
                for (int j = 0; j < n; j++) {
                    a[k][j] -= factor * a[i][j];
                    inv[k][j] -= factor * inv[i][j];
                }
            }
        }
        return inv;
    }
}
