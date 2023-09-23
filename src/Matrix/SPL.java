package Matrix;

import java.util.*;

import Interface.Solvable;
import Utils.Utils;

class Parametric {
    public double c = 0 ;
    public double fullParametricVariables[];
    public boolean isAssigned;

    // Effective parametric ordering
    public double param[];

    

    /**
     * 
     * @param n
     */
    public Parametric(int n){
        this.fullParametricVariables = new double[n];
        this.c = 0;
        this.isAssigned = false;

    }

    /**
     * 
     * @param x
     */

    public void add(Parametric x) {
        this.c += x.c;
        for (int i = 0; i < this.fullParametricVariables.length; i++)
        {
            this.fullParametricVariables[i] += x.fullParametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void add(Parametric x, double multiplier) {
        this.c += x.c*multiplier;
        for (int i = 0; i < this.fullParametricVariables.length; i++)
        {
            this.fullParametricVariables[i] += x.fullParametricVariables[i]*multiplier;
        }
        this.isAssigned = true;
    }


    public void subtract(Parametric x){
        this.c -= x.c;
        for (int i = 0; i < this.fullParametricVariables.length; i++)
        {
            this.fullParametricVariables[i] -= x.fullParametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void subtract(Parametric x, double multiplier){
        this.c -= x.c*multiplier;
        for (int i = 0; i < this.fullParametricVariables.length; i++)
        {
            this.fullParametricVariables[i] -= x.fullParametricVariables[i]*multiplier;
        }
        this.isAssigned = true;
    }

    public void setAsBaseParametric(int x){
        this.fullParametricVariables[x] = 1;
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
            System.out.printf("%.4f ", this.c);
        }

        for (int i = 0; i < param.length; i++)
        {
            if (Utils.isNotEqual(param[i], 0))
            {
                System.out.printf("%+.4f%c ", fullParametricVariables[i], parametricNames.charAt(i) );
            }
        }
        System.out.println();

    }

    public boolean hasParametricVariables() {
        for (int i = 0; i < fullParametricVariables.length; i++)
        {
            if (Utils.isNotEqual(this.fullParametricVariables[i], 0))
            {
                return true;
            }
        }
        return false;
    }


}

public class SPL implements Solvable {

    // Kesepakatan: Ax = B;
    public double A[][];
    public Parametric x[];
    public double B[];
    public Matrix augmentedMatrix;
    private Map<Integer, Integer> parametricLinking = new HashMap<Integer, Integer>();
    private SPLMethod method; 

    enum SPLMethod {
        Gauss, GaussJordan, Inverse, Cramer
    }

    public void setMethod(SPLMethod method) {
        this.method = method;
    }
    public void init(Matrix matrix) {
        this.fromMatrix(matrix);
    }
    public void solve() {

        if (!this.augmentedMatrix.isEchelon()) this.augmentedMatrix.toRowEchelon();

        for (int i = this.B.length-1; i >= 0; i--)
        {
            this.solveRow(i);
        }



        for (int i = 0; i < this.x.length; i++)
        {
            if (!this.x[i].isAssigned) this.x[i].setAsBaseParametric(i);
        }

        this.solve_helper();
    }

    private void showEquation(int row)
    {
        int cnt = 0;
        for(int i = 0 ; i < this.augmentedMatrix.col; i++)
        {
            if (cnt == 0) {
                if (Utils.isEqual(this.augmentedMatrix.matrix[row][i], 0)) continue;
                System.out.printf("%.4fx%d ", this.augmentedMatrix.matrix[row][i], i+1 );
                cnt += 1;
            } else if (i == this.augmentedMatrix.col - 1)
            {
                System.out.printf("= %.4f \n", this.augmentedMatrix.matrix[row][i]);
            } else {
                if (Utils.isEqual(this.augmentedMatrix.matrix[row][i], 0)) continue; 
                System.out.printf("%+.4fx%d ", this.augmentedMatrix.matrix[row][i], i+1 );
            }
        }
    }
    private void showEquations()
    {
        for (int row = 0; row < this.augmentedMatrix.row; row++)
        {
            showEquation(row);
        }
    }   

    public void solveUsingGauss(boolean showProcess) {


        // STEP 1

        System.out.println("--> Cek apakah augmented matriks sudah berupa matriks eselon baris.");
        System.out.println();
        displayMatrix();
        System.out.println();

        if (augmentedMatrix.isEchelon()) 
        {
            System.out.println("Matriks di atas sudah berupa matriks eselon baris. Lanjut ke step berikutnya");
        } else {
            System.out.println("Karena matriks di atas belum merupakan matriks eselon baris, Ubah ke matriks eselon baris.");
            this.augmentedMatrix.toRowEchelon();
            displayMatrix();
            System.out.println();
        }


        // STEP 2

        System.out.println();
        System.out.println("--> Setelah itu, ubah ke bentuk sistem persamaan linear.");
        System.out.println();
        this.showEquations();
        System.out.println();


        // STEP 3
        System.out.println("""
        --> Sekarang, selesaikan persamaan satu persatu mulai dari 
        persamaan yang paling bawah.
        """);

        for (int row = this.augmentedMatrix.row-1; row >= 0; row--)
        {
            System.out.print(" -> dari persamaan :");
            showEquation(row);
            solveRow(row);
        }
        
        











    }

    private void solveUsingGaussJordan() {

    }

    private void solveUsingInverse(){

    }

    private void solveUsingCramer() {

    }

    public void fromMatrix(Matrix m) {

        this.initSPL(m.row, m.col);

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


        /**
         * Jika semua elemen kecuali paling terakhir bernilai 0,
         * haruslah elemen paling terakhir bernilai 0. Karena apabila tak nol maka tidak ada solusi
         * dan sudah dihandle hasSolution();
         *
        */
        if (leadingOnePosition == -1) return;


        this.x[leadingOnePosition].c += rowArray[rowArray.length - 1]; 

        System.out.println("Kita dapati");
        for (int i = rowArray.length - 2; i > leadingOnePosition; i--)
        {
            double multiplier = rowArray[i];
            if (!this.x[i].isAssigned){
                this.x[i].setAsBaseParametric(i);
                System.out.printf("Jadikan x%d sebagai base parametrik.\n", i+1);
            }
            this.x[leadingOnePosition].subtract(this.x[i], multiplier);
            
        }
        this.x[leadingOnePosition].isAssigned = true;
        System.out.println();
    
    }

    private void solve_helper()
    {

        // Set the effective used parametric variables.
        int lengthX = this.x.length;
        Integer[] usedParametricVariable = new Integer[lengthX];
        Arrays.fill(usedParametricVariable, 0, lengthX, 0);
        for (int i = 0; i < lengthX; i++)
        {
            Utils.andOrList(usedParametricVariable, this.x[i].fullParametricVariables, false);
        }


        // Linking variables;
        int cnt = 0;
        for (Integer i = 0; i < lengthX; i++)
        {
            if (usedParametricVariable[i] == 0) continue;
            parametricLinking.put(i, cnt);
            cnt++;
        }

        // set the effective variables used in the parametric solution
        for (Integer i = 0; i < lengthX; i++)
        {
            this.x[i].param = new double[cnt];
            for (Integer j = 0; j < lengthX; j++)
            {
                if (this.x[i].fullParametricVariables[j] == 0) continue;
                this.x[i].param[parametricLinking.get(j)] = this.x[i].fullParametricVariables[j];
            }
        }

        // testing output
        for (int i = 0; i < lengthX; i++)
        {
            System.out.println(Arrays.toString(this.x[i].param)); 
        }
    }

    public void showSolution(){
        
        int lengthX = this.x.length;

        String parametricNaming = "abcdefghijklmnopqrstuvwyz";
        for (int i = 0; i<lengthX; i++){
            System.out.printf("x%d = ", i+1);
            if (Utils.isNotEqual(this.x[i].c, 0 ) || !this.x[i].hasParametricVariables()){
                System.out.printf("%.4f ", this.x[i].c);
            }
            
            for (int j = 0; j < lengthX; j++)
            {  
                if (this.x[i].fullParametricVariables[j] == 0) continue;
                System.out.printf("%+.4f%c ",this.x[i].fullParametricVariables[j] ,parametricNaming.charAt(parametricLinking.get(j)));
            }
            System.out.println();
        }

        

        
    }

    public void displayMatrix() {
        this.augmentedMatrix.displayMatrix("augmented");
    }



    
}
