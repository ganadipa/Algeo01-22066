package Testing;

import Matrix.Matrix;

public class Mesach {

    public static void main(String[] args) {
        Matrix matrix = new Matrix(3, 5);
        matrix.readInterpolasi();

        matrix.displayMatrix("augmented");
    }
}
