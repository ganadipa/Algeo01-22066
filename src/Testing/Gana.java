package Testing;

import Matrix.*;
import Utils.Utils;

public class Gana {
    public static void main(String[] args) {
        // Matrix m1 = new Matrix(5, 5);
        // m1.readSquareMatrix();
        // m1.displayMatrix("augmented");
        // Matrix m2 = new Matrix(3, 3);
        // try {
        //     m2.readMatrixFromFile();
        //     m2.displayMatrix("augmented");
        //     if (m2.isEchelon()) System.out.println("Check isEchelon passed!");
        //     if (m2.isReducedEchelon()) System.out.println("Check isReducedEchelon passed!");

        // } catch (Exception e)
        // {
        //     System.out.println(e.getMessage());
        // }
        SPL spl = new SPL(2);
        spl.solveRow(0);
    }
}
