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

    public void setAsBaseParametric(int x){
        this.parametricVariables[x] = 1;
        this.isAssigned = true;
    }

    public void showParametric(){
        if (!this.hasParametricVariables()) {
            System.out.println(this.c);
            return;
        }
        String parametricNames = "abcdefghijklmnopqrstuvwyz";
        if (Utils.isNotEqual(this.c, 0))
        {
            System.out.printf("%.3f ", this.c);
        }

        for (int i = 0; i < parametricVariables.length; i++)
        {
            if (Utils.isNotEqual(parametricVariables[i], 0))
            {
                System.out.printf("%.3f%c ", parametricVariables[i], parametricNames.charAt(i) );
            }
        }
        System.out.println();

    }

    public boolean hasParametricVariables() {
        for (int i = 0; i < parametricVariables.length; i++)
        {
            if (Utils.isNotEqual(this.parametricVariables[i], 0))
            {
                return true;
            }
        }
        return false;
    }


}

public class SPL {

    // Kesepakatan: Ax = B;
    public double A[][];
    public Parametric x[];
    public double B[];
    public Matrix augmentedMatrix;
    

    public SPL(int countEq, int countVar) {
        this.initSPL(countEq, countVar);
    }

    public void initSPL(int row, int col) {
        this.A = new double[row][col-1];
        this.B = new double[row];
        this.augmentedMatrix = new Matrix(row, col);


        this.x = new Parametric[col-1];
        for (int i = 0; i<col-1; i++)
        {
            x[i] = new Parametric(col-1);
        }
    }
    
    

    public void solve() {
        if (!this.augmentedMatrix.isEchelon()) this.augmentedMatrix.toRowEchelon(null);

        for (int i = this.B.length-1; i >= 0; i--)
        {
            System.out.printf("solving row: %d", i);
            this.solveRow(i);
        }


        for (int i = 0; i < this.x.length; i++)
        {
            if (!this.x[i].isAssigned) this.x[i].setAsBaseParametric(i);
        }
    }

    public void fromMatrix(Matrix m) {

        this.initSPL(m.row, m.col);
        System.out.println("init passed");

        for(int row = 0; row < m.matrix.length; row++)
        {
            for (int col = 0; col < m.matrix[0].length; col++)
            {
                this.augmentedMatrix.matrix[row][col] = m.matrix[row][col]; 
                if (col == m.matrix[0].length-1) {
                    this.B[row] = m.matrix[row][col];
                    continue;
                }

                this.A[row][col] = m.matrix[row][col];
            }
        }

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
        double[] rowArray = this.augmentedMatrix.matrix[row];
        int leadingOnePosition = -1;
        for (int i =  0; i < rowArray.length-1; i++)
        {
            if (Utils.isEqual(rowArray[i], 1.00))
            {
                leadingOnePosition = i;
                break;
            }
        }

        System.out.println("get leading done");

        /**
         * Jika semua elemen kecuali paling terakhir bernilai 0,
         * haruslah elemen paling terakhir bernilai 0. Karena apabila tak nol maka tidak ada solusi
         * dan sudah dihandle hasSolution();
         *
        */
        if (leadingOnePosition == -1) return;


        this.x[leadingOnePosition].c += rowArray[rowArray.length - 1]; 
        System.out.println(Arrays.toString(rowArray));
        System.out.print("x length is: ");
        System.out.println(this.x.length);
        System.out.println(Arrays.toString(this.x));
        for (int i = rowArray.length - 2; i > leadingOnePosition; i--)
        {
            System.out.println(i);
            double multiplier = rowArray[i];
            System.out.println(this.x[i].c); 
            System.out.println("parameyric variable length: ");
            System.out.println(this.x[i].parametricVariables.length);
            if (!this.x[i].isAssigned) this.x[i].setAsBaseParametric(i);
            System.out.println("we have done set base parametric");
            this.x[leadingOnePosition].subtract(this.x[i], multiplier);
            
        }
        this.x[leadingOnePosition].isAssigned = true;
        System.out.println(this.x[leadingOnePosition].c);
        System.out.println(Arrays.toString(this.x[leadingOnePosition].parametricVariables));
        this.x[leadingOnePosition].showParametric();
    
    }

    public void showSolution(){
        for (int i = 0; i<this.x.length; i++){
            System.out.printf("x%d = ", i+1);
            this.x[i].showParametric();
        }
        
    }

    public void displayMatrix() {
        this.augmentedMatrix.displayMatrix("augmented");
    }

    
}
