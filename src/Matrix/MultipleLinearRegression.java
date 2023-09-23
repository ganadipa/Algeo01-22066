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
        
    }

    public MultipleLinearRegression() {

    }

    public Matrix getMatrixFromUserInput() {
        System.out.println("Predictor amount:");
        int predictorAmount = Input.getInt();

        System.out.println("Data point amount:");
        int dataPointAmount = Input.getInt();

        System.out.println("y ("+dataPointAmount+" Data Points):");
        double yValues[] = new double[dataPointAmount];

        String yln = Input.userInput.nextLine();
        String[] yelmts = yln.split(" ");
        for (int i = 0; i < dataPointAmount; i++)
        {
            yValues[i] = Double.parseDouble(yelmts[i]);
        }

        System.out.println("\nx ("+predictorAmount+" Predictors):");
        double xValues[][] = new double[predictorAmount][dataPointAmount];
        for (int i = 0; i < predictorAmount; i++)
        {
            System.out.println("x"+(i+1)+" ("+dataPointAmount+" Data Points):");

            String xln = Input.userInput.nextLine();
            String[] xelmts = xln.split(" ");
            for (int j = 0; j < dataPointAmount; j++)
            {
                xValues[i][j] = Double.parseDouble(xelmts[j]);
            }
        }
        Matrix mat = new Matrix();

        

        return mat;
    }

    
}
