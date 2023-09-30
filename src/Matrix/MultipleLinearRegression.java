package Matrix;
import Interface.Solvable;
import Utils.Input;

public class MultipleLinearRegression extends Solvable {
    Matrix matrix; // input
    Matrix mlrMatrix; // bentuk regresi
    SPL spl;
    double x[]; // nilai yang ingin diestimasi
    @Override
    public void readVariablesFromUserInput() {
        System.out.println("Banyak sampel data:"); // Jumlah data point
        int dataPointAmount = Input.getInt();

        System.out.println("Banyak peubah x:"); // Jumlah predictor
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

        x = new double[predictorAmount];
        System.out.println("Nilai yang akan ditaksir:");
        for(int i = 0; i < predictorAmount; i++) System.out.print("x"+(i+1)+" ");
        System.out.println("");
        Input.userInput.nextLine();
        String ln = Input.userInput.nextLine();
        String[] elmts = ln.split(" ");
        for (int i = 0; i < predictorAmount; i++)
        {
            x[i] = Double.parseDouble(elmts[i]);
        }
        setMatrix(m);
        setState(State.Unsolved);
        setupMlrMatrix();
    }
    @Override
    public void readVariablesFromTextFile() {

    }
    @Override
    public void solve() {
        this.spl = new SPL(mlrMatrix.row,mlrMatrix.col-1);
        spl.setMatrix(mlrMatrix)
            .solve();
    }
    @Override
    public void displaySolution() {

    }

    public void init(Matrix matrix) {
        this.matrix = matrix;
    }
    
    public void display() {
        System.out.print("f(x) = ");
        System.out.printf("%.3f", spl.x[0].getConstant());
        for(int i = 1; i < spl.x.length; i++) {
            System.out.printf(" + %.3fx%d", spl.x[i].getConstant(), i);
        }

        System.out.print("\nEstimate:\n");
        System.out.print("f(");
        System.out.printf("%.3f", x[0]);

        for (int i = 1; i < x.length; i++)
        {
            System.out.printf(",%.3f", x[i]);
        }
        System.out.print(") = ");
        System.out.printf("%.3f\n", getEstimate());
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double getEstimate() {
        double result = 0;
        result += spl.x[0].getConstant();
        // System.out.printf("%.3f + ", spl.x[0].getConstant());
        for(int i = 1; i < mlrMatrix.row; i++) {
            // System.out.printf("%.3f * %.3f + ", spl.x[i].getConstant(), x[i-1]);
            result += spl.x[i].getConstant() * x[i-1];
        }

        return result;
    }
 

    public void setMatrix(Matrix m) {
        this.matrix = m;
    }
    void setupMlrMatrix() {
        if(getState() == State.UninitializedVariable) throw new Error("Variable not initialized");
        
        this.mlrMatrix = new Matrix(this.matrix.col, this.matrix.col+1);

        for(int m = 0; m < this.matrix.col; m++) {
            for(int n = 0; n < this.matrix.col+1; n++) {
                
                double total = 0;
                for(int i = 0; i < this.matrix.row; i++) {
                    if(m == 0 && n == 0) {
                        total += 1;
                    }
                    else if(m == 0){
                        total += this.matrix.matrix[i][n-1];
                    }
                    else if(n == 0){
                        total += this.matrix.matrix[i][m-1];
                    }
                    else { 
                        total += this.matrix.matrix[i][m-1]*this.matrix.matrix[i][n-1];
                    }
                }
                this.mlrMatrix.matrix[m][n] = total;
            }
        }
    }

    
}
