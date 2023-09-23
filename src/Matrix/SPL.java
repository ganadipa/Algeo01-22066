package Matrix;

import java.util.*;

import Interface.Solvable;
import Matrix.Parametric.SolutionStatus;
import Utils.Utils;

class Parametric {
    /**
     * About Parametric Class:
     * 
     * Solusi dari setiap variabel pada SPL akan memiliki class parametrik, baik itu
     * variabel yang bernilai konstan maupun yang nilainya bisa banyak kemungkinan.
     * 
     * @see attribute c 
     * adalah nilai dari konstanta untuk setiap nilai variabel. Misal 
     * a. X1 = 7, maka nilai c adalah 7,
     * b. X2 = 7 - p untuk setiap p bilangan riil, maka nilai c adalah 7,
     * c. X3 = t untuk setiap t bilangan riil, maka nilai c adalah 0.
     * 
     * @see private attribute temporaryParametricVariable
     * Untuk setiap variabel di dalam suatu SPL, mereka pasti memiliki attribute temporaryParametricVariable.
     * Array ini digunakan untuk mencerna dengan mudah dependency antara variabel satu dengan
     * yang lainnya.
     * a. misal SPL kita memliki 3 variabel, x1, x2, dan x3. Maka x1, x2, dan x3 memiliki temporaryParametricVariable
     * yang panjangnya 3. Katakanlah x1 = 2 - x2 - 8x3 dengan x2 = s (untuk setiap bilangan riil s),
     * dan x3 = t untuk setiap bilangan riil t. Nanti temporaryParametricVariable dari x1 adalah
     * [0, -1, -8].
     * 
     * @see attribute isAssigned
     * Untuk mengetahui apakah suatu variabel sudah diketahui/dihitung nilainya atau belum.
     * 
     * @see attribute parametricCoefficient
     * Array yang menampung seluruh koefisien dari tiap-tiap base parametrik.
     * 
     * 
     */
    public double c = 0 ;
    private double temporaryParametricVariables[];
    public boolean isAssigned;

    // Effective parametric ordering
    public double parametricCoefficient[];

    

    /**
     * 
     * @param countVariable : jumlah variabel yang digunakan suatu dalam SPL
     */
    public Parametric(int countVariable){
        this.temporaryParametricVariables = new double[countVariable];
        this.c = 0;
        this.isAssigned = false;

    }

    public double getTmpParamElmt(int idx) {
        return temporaryParametricVariables[idx];
    }

    public double[] getTmpParamElmt()
    {
        return temporaryParametricVariables;
    }

    public void setTmpParamElmt(int idx, double val) {
        temporaryParametricVariables[idx] = val;
    }

    public void add(Parametric x) {
        this.c += x.c;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++)
        {
            this.temporaryParametricVariables[i] += x.temporaryParametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void add(Parametric x, double multiplier) {
        this.c += x.c*multiplier;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++)
        {
            this.temporaryParametricVariables[i] += x.temporaryParametricVariables[i]*multiplier;
        }
        this.isAssigned = true;
    }


    public void subtract(Parametric x){
        this.c -= x.c;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++)
        {
            this.temporaryParametricVariables[i] -= x.temporaryParametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void subtract(Parametric x, double multiplier){
        this.c -= x.c*multiplier;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++)
        {
            this.temporaryParametricVariables[i] -= x.temporaryParametricVariables[i]*multiplier;
        }
        this.isAssigned = true;
    }

    public void setAsBaseParametric(int x){
        this.temporaryParametricVariables[x] = 1;
        this.isAssigned = true;
    }

    enum SolutionStatus {
        Ready, NotReady
    }

    public void showParametric(SolutionStatus status){
        if (!this.hasParametricVariables()) {
            System.out.println(this.c);
            return;
        }
        String parametricNames = "abcdefghijklmnopqrstuvwyz";
        if (Utils.isNotEqual(this.c, 0))
        {
            System.out.printf("%.4f ", this.c);
        }
        int len = (status == SolutionStatus.NotReady) ? 
                this.temporaryParametricVariables.length : 
                this.parametricCoefficient.length;

        for (int i = 0; i < len; i++)
        {
            double[] using = (status == SolutionStatus.NotReady) ? 
                    this.temporaryParametricVariables : 
                    this.parametricCoefficient;

            if (Utils.isNotEqual(using[i], 0))
            {
                System.out.printf("%+.4f%c ", using[i], parametricNames.charAt(i) );
            }
        }
        System.out.println();

    }

    

    public boolean hasParametricVariables() {
        for (int i = 0; i < this.temporaryParametricVariables.length; i++)
        {
            if (Utils.isNotEqual(this.temporaryParametricVariables[i], 0))
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

    private SPLMethod method = SPLMethod.Gauss;
    private boolean showProcess = false;  

    public enum SPLMethod {
        Gauss, GaussJordan, Inverse, Cramer
    }

    public void setMethod(SPLMethod method) {
        this.method = method;
    }

    public void setShowProcess(boolean showProcess) {
        this.showProcess = showProcess;
    }
    public void init(Matrix matrix) {
        this.fromMatrix(matrix);
    }

    public SPL(int countEquations, int countVariables) {
        this.initSPL(countEquations, countVariables);
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


    public void solve() {
        if (this.method == SPLMethod.Gauss) {solveUsingGauss(this.showProcess);}
        else if (this.method == SPLMethod.GaussJordan) {solveUsingGaussJordan(this.showProcess);}
        else if (this.method == SPLMethod.Inverse) {solveUsingInverse(this.showProcess);}
        else if (this.method == SPLMethod.Cramer) {solveUsingCramer(this.showProcess);}
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

    private void solveUsingGauss(boolean showProcess) {

        if (showProcess){
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
                System.out.print(" -> dari persamaan :  ");
                showEquation(row);
                solveRow(row, true);
            }


            // STEP 4
            System.out.println("""
                    --> Wrapping up, sekaligus merapihkan penamaan variabel, solusi dari SPL tersebut adalah
                    """);
            
            this.solve_helper();
            this.showSolution();
        } else {
            this.augmentedMatrix.toRowEchelon();
            for (int i = this.augmentedMatrix.row-1; i >= 0; i--)
            {
                this.solveRow(i, false);
            }
            this.solve_helper();
            this.showSolution();
        }

    }

    private void solveUsingGaussJordan(boolean showProcess) {
        if (showProcess){
             // STEP 1

            System.out.println("--> Cek apakah augmented matriks sudah berupa matriks eselon baris tereduksi.");
            System.out.println();
            displayMatrix();
            System.out.println();

            if (augmentedMatrix.isReducedEchelon()) 
            {
                System.out.println("Matriks di atas sudah berupa matriks eselon baris tereduksi. Lanjut ke step berikutnya");
            } else {
                System.out.println("Karena matriks di atas belum merupakan matriks eselon baris tereduksi, Ubah ke matriks eselon baris.");
                this.augmentedMatrix.toReducedRowEchelon();
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
                System.out.print(" -> dari persamaan :  ");
                showEquation(row);
                solveRow(row, true);
            }


            // STEP 4
            System.out.println("""
                    --> Wrapping up, sekaligus merapihkan penamaan variabel, solusi dari SPL tersebut adalah
                    """);
            
            this.solve_helper();
            this.showSolution();
        } else {
            this.augmentedMatrix.toReducedRowEchelon();
            for (int i = this.augmentedMatrix.row-1; i >= 0; i--)
            {
                this.solveRow(i, false);
            }
            this.solve_helper();
            this.showSolution();
        }
    }

    private void solveUsingInverse(boolean showProcess){
        Matrix inversedMatrix;

        try {
            inversedMatrix = this.augmentedMatrix.getInverse();
        } catch (Error e)
        {
            System.out.println(e);
            System.out.println("Program berhenti.");
            return;
        }



        for (int row = 0; row < inversedMatrix.row; row++)
        {
            this.x[row].isAssigned = true;
            for (int col = 0; col < inversedMatrix.col; col++)
            {
                this.x[row].c += (inversedMatrix.matrix[row][col])*this.B[col];
            }
        }

        this.showSolution();
        

    }

    private void solveUsingCramer(boolean showProcess) {
        if (!this.augmentedMatrix.isSquare()){

        }
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

    public void solveRow(int row, boolean showProcess)
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

        String parametricNaming = "abcdefghijklmnopqrstuvwyz";


        this.x[leadingOnePosition].c += rowArray[rowArray.length - 1]; 


        if (showProcess) System.out.println(" | Kita dapati:");
        for (int i = rowArray.length - 2; i > leadingOnePosition; i--)
        {
            double multiplier = rowArray[i];
            if (!this.x[i].isAssigned){
                this.x[i].setAsBaseParametric(i);
                if (showProcess) System.out.printf(" | Jadikan x%d sebagai base parametrik, katakanlah %c\n", i+1, parametricNaming.charAt(i) );
            }
            this.x[leadingOnePosition].subtract(this.x[i], multiplier);
            
        }
        this.x[leadingOnePosition].isAssigned = true;

        if (showProcess)
        {
            System.out.printf(" | x%d = ", leadingOnePosition+1);
            this.x[leadingOnePosition].showParametric(SolutionStatus.NotReady);
            System.out.println();
        }
    
    }

    private void solve_helper()
    {

        // Set the effective used parametric variables.
        int lengthX = this.x.length;
        Integer[] usedParametricVariable = new Integer[lengthX];
        Arrays.fill(usedParametricVariable, 0, lengthX, 0);
        for (int i = 0; i < lengthX; i++)
        {
            Utils.andOrList(usedParametricVariable, this.x[i].getTmpParamElmt(), false);
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
            this.x[i].parametricCoefficient = new double[cnt];
            for (Integer j = 0; j < lengthX; j++)
            {
                if (Utils.isEqual(this.x[i].getTmpParamElmt(j), 0)) continue;
                this.x[i].parametricCoefficient[parametricLinking.get(j)] = this.x[i].getTmpParamElmt(j);
            }
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
                if (Utils.isEqual(this.x[i].getTmpParamElmt(j), 0)) continue;
                System.out.printf("%+.4f%c ",this.x[i].getTmpParamElmt(j) ,parametricNaming.charAt(parametricLinking.get(j)));
            }
            System.out.println();
        }

        

        
    }

    public void displayMatrix() {
        this.augmentedMatrix.displayMatrix("augmented");
    }



    
}
