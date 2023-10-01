package Testing;



import java.io.IOException;

import Matrix.*;
import Matrix.SPL.SPLMethod;
import Utils.Utils;

public class Gana {
    public static void main(String[] args) {
        // // Matrix m1 = new Matrix(5, 5);
        // // m1.readSquareMatrix();
        // // m1.displayMatrix("augmented");
        // Matrix m2 = new Matrix(3, 3);
        // try {
        //     m2.readMatrixFromFile();
        //     // m2.displayMatrix();
        //     // if (m2.isEchelon()) System.out.println("Check isEchelon passed!");
        //     // if (m2.isReducedEchelon()) System.out.println("Check isReducedEchelon passed!");
        //     SPL spl = new SPL(m2.row, m2.col);
        //     // System.out.println("passed");
        //     // m2.displayMatrix();
        //     m2.normalizeMatrix();
        //     spl.fromMatrix(m2);
        //     // spl.readFromUserInput();
        //     spl.setMethod(SPLMethod.GaussJordan);
        //     spl.setShowProcess(true);
        //     spl.solve(true);
        //     SimpleImage img = new SimpleImage("gana.png");



        // } catch (Exception e)
        // {
        //     System.out.println(e.getMessage());
        // }
        try {
            SimpleImage img = new SimpleImage("gana.png");
            img.EnlargeImage();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }
}
