package Testing;



import java.io.IOException;

import Matrix.*;
import Matrix.SPL.SPLMethod;
import Utils.Utils;

public class Gana {
    public static void main(String[] args) {
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
        //     spl.displayAugmentedMatrix();
        //     // spl.readFromUserInput();
        //     spl.setMethod(SPLMethod.GaussJordan);
        //     spl.setShowProcess(true);
        //     spl.displaySolution();
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }



        try {
            SimpleImage img = new SimpleImage("foto.jpg");
            img.setFactor(4);
            img.setOptions(SimpleImage.ColorOptions.NORMAL);
            img.EnlargeImage();
        } catch (IOException e) {
            e.getStackTrace();
        }
        
    }
}
