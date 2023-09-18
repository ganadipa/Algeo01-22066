package Matrix;

import java.util.Arrays;

import Utils.Utils;

class Parametric {
    public double c = 0 ;
    public double parametricVariables[];
    public boolean isAssigned;


    /**
     * 
     * @param n
     */
    public Parametric(int n){
        this.parametricVariables = new double[n];
        this.c = 0;
        this.isAssigned = false;

    }

    /**
     * 
     * @param x
     */

    public void add(Parametric x) {
        this.c += x.c;
        for (int i = 0; i < this.parametricVariables.length; i++)
        {
            this.parametricVariables[i] += x.parametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void add(Parametric x, double multiplier) {
        this.c += x.c;
        for (int i = 0; i < this.parametricVariables.length; i++)
        {
            this.parametricVariables[i] += x.parametricVariables[i]*multiplier;
        }
        this.isAssigned = true;
    }


    public void subtract(Parametric x){
        this.c -= x.c;
        for (int i = 0; i < this.parametricVariables.length; i++)
        {
            this.parametricVariables[i] -= x.parametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void subtract(Parametric x, double multiplier){
        this.c -= x.c;
        for (int i = 0; i < this.parametricVariables.length; i++)
        {
            this.parametricVariables[i] -= x.parametricVariables[i]*multiplier;
        }
        this.isAssigned = true;
    }

    public void toBaseParametric(int x){
        this.parametricVariables[x] = 1;
        this.isAssigned = true;
    }

    public void showParametric(){

    }


}

public class SPL {

    // Kesepakatan: Ax = B;
    public double A[][];
    public Parametric x[];
    public double B[];
    public double augmentedMatrix[][] = {{1,6,6}};
    

    public SPL(int n) {
        this.x = new Parametric[n];
        for (int i = 0; i<n; i++)
        {
            x[i] = new Parametric(n);
        }
        
    }

    public void solve() {

    }

    public boolean hasSolution()
    {
        int[] allZeroRow = new int[this.B.length];
        int rowLengthInA = this.A.length;
        int colLengthInA = this.A[0].length;

        for (int row = rowLengthInA-1; row >= 0; row--)
        {
            boolean checkRow = true;
            for (int col = 0; col < colLengthInA; col ++)
            {
                if (Utils.isNotEqual(this.A[row][col], 0.0))
                {
                    checkRow = false;
                    break;
                }
            }
            if (checkRow) allZeroRow[row] = 1;
        }

        for(int row = 0; row < allZeroRow.length; row++)
        {
            if (allZeroRow[row] == 1 && Utils.isNotEqual(this.B[row], 0.0 ))
            {
                return false;
            }
        }

        return true;
    } 

    public void solveRow(int row)
    {
        double[] rowArray = this.augmentedMatrix[row];
        int leadingOnePosition = -1;
        for (int i =  0; i < rowArray.length-1; i++)
        {
            if (Utils.isEqual(rowArray[i], 1.00))
            {
                leadingOnePosition = i;
                break;
            }
        }

        /**
         * Jika semua elemen kecuali paling terakhir bernilai 0,
         * haruslah elemen paling terakhir bernilai 0. Karena apabila tak nol maka tidak ada solusi
         * dan sudah dihandle hasSolution();
         *
        */
        if (leadingOnePosition == -1) return;


        this.x[leadingOnePosition].c += rowArray[rowArray.length - 1]; 
        for (int i = rowArray.length - 2; i > leadingOnePosition; i--)
        {
            double multiplier = rowArray[i];
            if (!this.x[i].isAssigned) this.x[i].toBaseParametric(i);
            this.x[leadingOnePosition].subtract(this.x[i], multiplier);
        }

        System.out.println(this.x[leadingOnePosition].c);
        System.out.println(Arrays.toString(this.x[leadingOnePosition].parametricVariables));
    
    }

    public void showSolution(){
        // String variables = "pqrstuvwxyzabcdefghijklmno";
        // for(int i)
    }

    
}
