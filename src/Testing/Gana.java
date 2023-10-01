package Testing;



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

        Matrix matrixD = new Matrix(16, 16);
        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int y2 = -1; y2 < 3;y2++){
                    for (int x2 = -1; x2 < 3; x2++) {
                        matrixD.matrix[row][col] = 4*(x2 == x ?1 : 0)*(y2 == y? 1: 0);
                        matrixD.matrix[row + 4][col] = 2*((x2==(x+1))?1:0)*(y2==(y)?1:0) - 2*((x2==(x-1)?1:0)*(y2 == y?1:0));
                        matrixD.matrix[row + 8][col] = 2*((x2 == x?1:0)*(y2==(y+1)?1:0)) - 2*((x2==x?1:0)*(y2 == (y-1)?1:0));
                        matrixD.matrix[row + 12][col] =(((x2 == (x-1))&&(y2 == (y-1))) ? 1: 0) + (((x2 == (x+1))&&(y2 == (y+1))) ? 1: 0) - (((x2 == (x+1))&&(y2 == (y-1))) ? 1: 0) - (((x2 == (x-1))&&(y2 == (y+1))) ? 1: 0) ;
                        col++;
                    }
                }
                row++;
            }
        }

        matrixD.displayMatrix();
    }
}
