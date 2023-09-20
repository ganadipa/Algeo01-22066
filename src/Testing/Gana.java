package Testing;

import Matrix.*;
import Utils.Utils;

public class Gana {
    public static void main(String[] args) {
        // Matrix m1 = new Matrix(5, 5);
        // m1.readSquareMatrix();
        // m1.displayMatrix("augmented");
        Matrix m2 = new Matrix(3, 3);
        try {
            // m2.readMatrixFromFile();
            // m2.displayMatrix("augmented");
            // if (m2.isEchelon()) System.out.println("Check isEchelon passed!");
            // if (m2.isReducedEchelon()) System.out.println("Check isReducedEchelon passed!");
            // SPL spl = new SPL(m2.row, m2.col);
            // System.out.println("passed");
            // spl.fromMatrix(m2);
            // System.out.println("passed");
            // spl.solve();
            // System.out.println("passed");
            // spl.showSolution();
            m2.readMatrixFromFile();
            m2.displayMatrix("augmented");
            m2.toRowEchelon(null);
            m2.displayMatrix("augmented");
            m2.normalizeMatrix();
            m2.displayMatrix("augmented");



        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
