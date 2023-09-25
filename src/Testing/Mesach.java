package Testing;

import Matrix.BicubicSplineInterpolation;
import Matrix.Matrix;

public class Mesach {

    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        matrix.readSquareMatrix();

        matrix.displayMatrix();

        System.out.println(matrix.getDeterminant(Matrix.DeterminantMethod.RowReduction));

    }
}
