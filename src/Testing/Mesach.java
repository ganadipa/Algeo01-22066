package Testing;

import Class.Matrix;
import Class.Solvable.BicubicSplineInterpolation;
import Class.Solvable.Solvable;

public class Mesach {

    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        matrix.readSquareMatrix();

        matrix.displayMatrix();

        System.out.println(matrix.getDeterminant(Matrix.DeterminantMethod.RowReduction));

    }
}
