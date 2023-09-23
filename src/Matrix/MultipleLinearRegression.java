package Matrix;
import java.util.Scanner;

import Interface.Solvable;
import Utils.Input;

public class MultipleLinearRegression implements Solvable {
    Matrix matrix;
    SPL spl;
    double x[];
    public void init(Matrix matrix) {
        this.matrix = matrix;
    }
    public void solve() {
        this.spl = new SPL(matrix.row,matrix.col-1);
        spl.init(matrix);
        spl.solve();
    }
    public void display() {
        System.out.print("f(x) = ");
        System.out.print(spl.x[0].c+"x"+0);
        for(int i = 1; i < spl.x.length; i++) {
            System.out.print(" + "+spl.x[i].c+"x"+i);
        }

        System.out.print("\nEstimate:\n");
        for (int i = 0; i < x.length; i++)
        {
            System.out.println("f("+x[i]+") = "+ getEstimate(x[i]));
        }
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double getEstimate(double x) {
        double result = 0;
        for(int i = 0; i < spl.x.length; i++) {
            result += spl.x[i].c * Math.pow(x, i);
        }
        return result;
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

        System.out.println("Banyak nilai yang akan ditaksir:");
        int estimateAmount = Input.getInt();
        x = new double[estimateAmount];
        System.out.println("Nilai yang akan ditaksir ("+estimateAmount+" nilai):");
        Input.userInput.nextLine();
        String ln = Input.userInput.nextLine();
        String[] elmts = ln.split(" ");
        for (int i = 0; i < estimateAmount; i++)
        {
            x[i] = Double.parseDouble(elmts[i]);
        }

        return m;
    }

    
}
