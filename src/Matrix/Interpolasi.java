package Matrix;

import Interface.Solvable;

public class Interpolasi implements Solvable {
    Matrix matrix;
    double x;

    public void init(Matrix matrix) {
        this.matrix = matrix;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void solve() {
        this.matrix.toReducedRowEchelon();

        System.out.print("f(x) = ");
        for (int i = matrix.row - 1; i >=  0; i--) {
            if (i > 1) {
                System.out.printf("%.4fx^%d", Math.abs(this.matrix.matrix[i][matrix.col - 1]), i);
            } else if (i == 1) {
                System.out.printf("%.4fx", Math.abs(this.matrix.matrix[i][matrix.col - 1]));
            } else {
                System.out.printf("%.4f", Math.abs(this.matrix.matrix[i][matrix.col - 1]));
            }
            if (i != 0) {
                if (this.matrix.matrix[i - 1][matrix.col - 1] >= 0) {
                    System.out.print(" + ");
                } else if (this.matrix.matrix[i - 1][matrix.col - 1] < 0) {
                    System.out.print(" - ");
                }
            }
            result += (this.matrix.matrix[i][matrix.col - 1] * Math.pow(this.x, i));
        }
        System.out.println("");

        System.out.printf("f(%.4f) = %.4f", this.x, result);
    }
}
