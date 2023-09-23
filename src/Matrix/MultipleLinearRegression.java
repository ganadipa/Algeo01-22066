package Matrix;
import java.util.Scanner;

import Interface.Solvable;
import Utils.Input;

public class MultipleLinearRegression implements Solvable {
    Matrix matrix;
    public void init(Matrix matrix) {
        this.matrix = matrix;
    }
    public void solve() {
        SPL spl = new SPL(matrix.row,matrix.col-1);
        spl.init(matrix);
        spl.solve();
        matrix.displayMatrix(null);
        System.out.print("f(x) = ");
        System.out.print(spl.x[0].c+"x"+0);
        for(int i = 1; i < spl.x.length; i++) {
            System.out.print(" + "+spl.x[i].c+"x"+i);
        }
    }
 

    public MultipleLinearRegression() {

    }

    public Matrix getMatrixFromUserInput() {
        System.out.println("Data point amount:");
        int dataPointAmount = Input.getInt();

        System.out.println("Predictor amount:");
        int predictorAmount = Input.getInt();
        
        for (int i = 0; i < predictorAmount; i++)
        {
            System.out.print("x"+(i+1)+" ");
        }
        System.out.print("y");

        System.out.println("");
        Matrix m = new Matrix(dataPointAmount, predictorAmount+1);
        try {
            m.readMatrixFromUserInput();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return m;
    }

    
}
