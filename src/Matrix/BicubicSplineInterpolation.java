package Matrix;

import Interface.Solvable;

public class BicubicSplineInterpolation implements Solvable{
    private Matrix matrixX = new Matrix(16, 16);
    private Matrix matrixF;
    private Matrix matrixA = new Matrix(16, 1);
    private double a;
    private double b;

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void init(Matrix m) {
        this.matrixF = m;
        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int j = 0; j < 4; j++){
                    for (int i = 0; i < 4; i++) {
                        matrixX.matrix[row][col] = Math.pow(x, i) * Math.pow(y, j);
                        matrixX.matrix[row + 4][col] = (x == 0) ? 0 : i * Math.pow(x, i - 1) * Math.pow(y, j);
                        matrixX.matrix[row + 8][col] = (y == 0) ? 0 : j * Math.pow(x, i) * Math.pow(y, j - 1);
                        matrixX.matrix[row + 12][col] = (y == 0 || x == 0) ? 0 : i * j * Math.pow(x, i - 1) * Math.pow(y, j - 1);
                        col++;
                    }
                }
                row++;
            }
        }
    }

    public void solve() {
        Matrix inverseX = this.matrixX.getInverse();
        matrixA = inverseX.multiplyBy(matrixF);


        double result = 0;
        int rowA = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                result += (matrixA.matrix[rowA++][0] * Math.pow(this.a, i) * Math.pow(this.b, j));
            }
        }

        System.out.printf("f(%.4f, %.4f) = %.4f\n", this.a, this.b, result);
    }
}