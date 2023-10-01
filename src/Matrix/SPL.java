package Matrix;

import java.util.*;

import Interface.Solvable;
import Matrix.Parametric.SolutionStatus;
import Utils.Input;
import Utils.Utils;

class Parametric {
    /**
     * About Parametric Class:
     * 
     * Solusi dari setiap variabel pada SPL akan memiliki class parametrik, baik itu
     * variabel yang bernilai konstan maupun yang nilainya bisa banyak kemungkinan.
     * 
     * @see attribute c
     *      adalah nilai dari konstanta untuk setiap nilai variabel. Misal
     *      a. X1 = 7, maka nilai c adalah 7,
     *      b. X2 = 7 - p untuk setiap p bilangan riil, maka nilai c adalah 7,
     *      c. X3 = t untuk setiap t bilangan riil, maka nilai c adalah 0.
     * 
     * @see private attribute temporaryParametricVariable
     *      Untuk setiap variabel di dalam suatu SPL, mereka pasti memiliki
     *      attribute temporaryParametricVariable.
     *      Array ini digunakan untuk mencerna dengan mudah dependency antara
     *      variabel satu dengan
     *      yang lainnya.
     *      a. misal SPL kita memliki 3 variabel, x1, x2, dan x3. Maka x1, x2, dan
     *      x3 memiliki temporaryParametricVariable
     *      yang panjangnya 3. Katakanlah x1 = 2 - x2 - 8x3 dengan x2 = s (untuk
     *      setiap bilangan riil s),
     *      dan x3 = t untuk setiap bilangan riil t. Nanti
     *      temporaryParametricVariable dari x1 adalah
     *      [0, -1, -8].
     * 
     * @see attribute isAssigned
     *      Untuk mengetahui apakah suatu variabel sudah diketahui/dihitung nilainya
     *      atau belum.
     * 
     * @see attribute parametricCoefficient
     *      Array yang menampung seluruh koefisien dari tiap-tiap base parametrik.
     * 
     * 
     */
    private double c = 0;
    private double temporaryParametricVariables[];
    private boolean isAssigned;

    // Effective parametric ordering
    private double parametricCoefficient[];

    /**
     * 
     * @param countVariable : jumlah variabel yang digunakan suatu dalam SPL
     */
    public Parametric(int countVariable) {
        this.temporaryParametricVariables = new double[countVariable];
        this.c = 0;
        this.isAssigned = false;

    }

    public double getTmpParamElmt(int idx) {
        return temporaryParametricVariables[idx];
    }

    public double[] getTmpParamElmt() {
        return temporaryParametricVariables;
    }

    public void setTmpParamElmt(int idx, double val) {
        temporaryParametricVariables[idx] = val;
    }

    public double getConstant() {
        return this.c;
    }

    public void setConstant(double val) {
        this.c = val;
        setIsAssigned(true);
    }

    public boolean getIsAssigned() {
        return this.isAssigned;
    }

    public void setIsAssigned(boolean val) {
        this.isAssigned = val;
    }

    public double getParametricCoefficient(int idx) {
        return this.parametricCoefficient[idx];
    }

    public void setParametricCoefficient(int idx, double val) {
        this.parametricCoefficient[idx] = val;
        setIsAssigned(true);
    }

    public void initializeParametricCoefficient(int num) {
        this.parametricCoefficient = new double[num];
    }

    public void add(Parametric x) {
        this.c += x.c;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++) {
            this.temporaryParametricVariables[i] += x.temporaryParametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void add(Parametric x, double multiplier) {
        this.c += x.c * multiplier;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++) {
            this.temporaryParametricVariables[i] += x.temporaryParametricVariables[i] * multiplier;
        }
        this.isAssigned = true;
    }

    public void subtract(Parametric x) {
        this.c -= x.c;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++) {
            this.temporaryParametricVariables[i] -= x.temporaryParametricVariables[i];
        }
        this.isAssigned = true;
    }

    public void subtract(Parametric x, double multiplier) {
        this.c -= x.c * multiplier;
        for (int i = 0; i < this.temporaryParametricVariables.length; i++) {
            this.temporaryParametricVariables[i] -= x.temporaryParametricVariables[i] * multiplier;
        }
        this.isAssigned = true;
    }

    public void setAsBaseParametric(int x) {
        this.temporaryParametricVariables[x] = 1;
        this.isAssigned = true;
    }

    enum SolutionStatus {
        Ready, NotReady
    }

    public void showParametric(SolutionStatus status) {
        if (!this.hasParametricVariables()) {
            System.out.println(this.c);
            return;
        }
        String parametricNames = "abcdefghijklmnopqrstuvwyz";
        if (Utils.isNotEqual(this.c, 0)) {
            System.out.printf("%.4f ", this.c);
        }
        int len = (status == SolutionStatus.NotReady) ? this.temporaryParametricVariables.length
                : this.parametricCoefficient.length;

        for (int i = 0; i < len; i++) {
            double[] using = (status == SolutionStatus.NotReady) ? this.temporaryParametricVariables
                    : this.parametricCoefficient;

            if (Utils.isNotEqual(using[i], 0)) {
                System.out.printf("%+.4f%c ", using[i], parametricNames.charAt(i));
            }
        }
        System.out.println();

    }

    public boolean hasParametricVariables() {
        for (int i = 0; i < this.temporaryParametricVariables.length; i++) {
            if (Utils.isNotEqual(this.temporaryParametricVariables[i], 0)) {
                return true;
            }
        }
        return false;
    }

}

public class SPL extends Solvable {

    // Kesepakatan: Ax = B;
    public Matrix A;
    public Parametric x[];
    public Matrix B;
    public Matrix augmentedMatrix;
    private Map<Integer, Integer> parametricLinking = new HashMap<Integer, Integer>();

    private SPLMethod method = SPLMethod.Gauss;
    private boolean showProcess = false;

    @Override
    public void readVariablesFromUserInput() {
        System.out.println("Masukkan jumlah baris atau jumlah persamaan dalam SPL tersebut (m): ");
        int m = Input.getInt("Masukkan harus berupa angka positif saja.", (Integer t) -> (t > 0));

        System.out.println("Masukkan jumlah variabel yang ditambah satu atau jumlah kolom dalam SPL tersebut (n): ");
        int n = Input.getInt("Masukkan harus berupa angka positif saja.", (Integer t) -> (t > 0));

        System.out.println("Jika SPL tersebut dibentuk ke dalam persamaan Ax = B, dengan A, x, dan B adalah matriks,");
        System.out.println("Silakan input matriks A: ");

        this.A = new Matrix(m, n - 1);
        this.A.displayMatrix();
        try {
            this.A.readMatrixFromUserInput();
        } catch (Exception e) {
            System.out.println("Terjadi suatu kesalahan, gagal membaca matriks, program berhenti.");
            throw new Error("Program berhenti");
        }

        System.out.println("Silakan input matriks B: ");
        this.B = new Matrix(m, 1);
        this.B.displayMatrix();
        try {
            this.B.readMatrixFromUserInput();
        } catch (Exception e) {
            System.out.println("Terjadi suatu kesalahan, gagal membaca matriks, program berhenti.");
            throw new Error("Program berhenti");
        }

        this.readOutputFileYesOrNo();

        this.augmentedMatrix = new Matrix(m, n);
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                this.augmentedMatrix.displayMatrix();
                if (col == n - 1) {
                    System.out.println("in b");
                    this.augmentedMatrix.matrix[row][col] = this.B.matrix[row][0];

                    continue;
                }
                System.out.println("in a");
                this.augmentedMatrix.matrix[row][col] = this.A.matrix[row][col];
            }
        }

        System.out.println("begin loop param");

        this.x = new Parametric[n - 1];
        for (int i = 0; i < n - 1; i++) {
            x[i] = new Parametric(n - 1);
        }

    }

    @Override
    public void readVariablesFromTextFile() {

    }

    public SPL() {

    }

    @Override
    public void solve() {
        solution = "";
        if (this.method == SPLMethod.Gauss) {
            solveUsingGauss(this.showProcess);
        } else if (this.method == SPLMethod.GaussJordan) {
            solveUsingGaussJordan(this.showProcess);
        } else if (this.method == SPLMethod.Inverse) {
            solveUsingInverse(this.showProcess);
        } else if (this.method == SPLMethod.Cramer) {
            solveUsingCramer(this.showProcess);
        }
    }

    public void displaySolutionToFile() {
        this.solve();
        Utils.printFile(solution, "/output/outputSPL.txt");
        System.out.println("Jawaban akan terdapat pada folder output dengan nama file 'outputSPL.txt'");
    }

    public void displaySolutionToTerminal() {
        this.solve();
        for (int i = 0; i < this.x.length; i++) {
            // If there is a not-assigned-variables, then it is not yet solved.
            if (!this.x[i].getIsAssigned())
                this.showSolution(true);
        }

        System.out.println(solution);
        // int lengthX = this.x.length;

        // String parametricNaming = "abcdefghijklmnopqrstuvwyz";
        // for (int i = 0; i < lengthX; i++) {
        // int cnt = 0;
        // System.out.printf("x%d = ", i + 1);
        // if (Utils.isNotEqual(this.x[i].getConstant(), 0) ||
        // !this.x[i].hasParametricVariables()) {
        // System.out.printf("%.4f ", this.x[i].getConstant());
        // cnt++;
        // }

        // for (int j = 0; j < lengthX; j++) {
        // if (Utils.isEqual(this.x[i].getTmpParamElmt(j), 0))
        // continue;
        // if (cnt > 0) {
        // System.out.printf("%+.4f%c ", this.x[i].getTmpParamElmt(j),
        // parametricNaming.charAt(parametricLinking.get(j)));
        // } else {
        // System.out.printf("%.4f%c ", this.x[i].getTmpParamElmt(j),
        // parametricNaming.charAt(parametricLinking.get(j)));
        // cnt++;
        // }
        // }
        // System.out.println();
        // }
    }

    @Override
    public void displaySolution() {
        // for (int i = 0; i < this.x.length; i++) {
        // // If there is a not-assigned-variables, then it is not yet solved.
        // if (!this.x[i].getIsAssigned())
        // this.showSolution(true);
        // }

        // int lengthX = this.x.length;

        // String parametricNaming = "abcdefghijklmnopqrstuvwyz";
        // for (int i = 0; i < lengthX; i++) {
        // int cnt = 0;
        // System.out.printf("x%d = ", i + 1);
        // if (Utils.isNotEqual(this.x[i].getConstant(), 0) ||
        // !this.x[i].hasParametricVariables()) {
        // System.out.printf("%.4f ", this.x[i].getConstant());
        // cnt++;
        // }

        // for (int j = 0; j < lengthX; j++) {
        // if (Utils.isEqual(this.x[i].getTmpParamElmt(j), 0))
        // continue;
        // if (cnt > 0) {
        // System.out.printf("%+.4f%c ", this.x[i].getTmpParamElmt(j),
        // parametricNaming.charAt(parametricLinking.get(j)));
        // } else {
        // System.out.printf("%.4f%c ", this.x[i].getTmpParamElmt(j),
        // parametricNaming.charAt(parametricLinking.get(j)));
        // cnt++;
        // }
        // }
        // System.out.println();
        // }
        if (!getIsPrintFile()) {
            displaySolutionToTerminal();
        } else {
            displaySolutionToFile();
        }

    }

    public enum SPLMethod {
        Gauss, GaussJordan, Inverse, Cramer
    }

    public SPL setMethod(SPLMethod method) {
        this.method = method;
        return this;
    }

    public SPL setShowProcess(boolean showProcess) {
        this.showProcess = showProcess;
        return this;
    }

    public SPL setMatrix(Matrix matrix) {
        this.fromMatrix(matrix);
        return this;
    }

    public SPL(int countEquations, int countVariables) {
        this.initSPL(countEquations, countVariables);
    }

    public void fromMatrix(Matrix m) {

        this.initSPL(m.row, m.col);

        for (int row = 0; row < m.matrix.length; row++) {
            for (int col = 0; col < m.matrix[0].length; col++) {
                this.augmentedMatrix.matrix[row][col] = m.matrix[row][col];
                if (col == m.matrix[0].length - 1) {
                    this.B.matrix[row][0] = m.matrix[row][col];
                    continue;
                }

                this.A.matrix[row][col] = m.matrix[row][col];
            }
        }

    }

    public SPL getCopySPL() {
        // Copy matrix and turn it into row echelon form
        Matrix tmp = new Matrix(this.augmentedMatrix.row, this.augmentedMatrix.col);
        for (int row = 0; row < tmp.row; row++) {
            for (int col = 0; col < tmp.col; col++) {
                tmp.matrix[row][col] = this.augmentedMatrix.matrix[row][col];
            }
        }

        SPL tmpSPL = new SPL(this.augmentedMatrix.row, this.augmentedMatrix.col - 1);
        tmpSPL.fromMatrix(tmp);
        return tmpSPL;
    }

    public void solve(boolean stay) {
        if (!stay)
            this.solve();

        SPL tmpSPL = getCopySPL();
        tmpSPL.setMethod(this.method);
        tmpSPL.setShowProcess(this.showProcess);
        tmpSPL.solve();
    }

    private void showEquation(int row) {
        System.out.printf("(%d) . . .   ", row + 1);
        int cnt = 0;
        for (int i = 0; i < this.augmentedMatrix.col; i++) {

            if (cnt == 0) {
                if (i == this.augmentedMatrix.col - 1) {
                    System.out.printf("0.0000 = %.4f\n", this.augmentedMatrix.matrix[row][i]);
                    break;
                }
                if (Utils.isEqual(this.augmentedMatrix.matrix[row][i], 0))
                    continue;
                System.out.printf("%.4fx%d ", this.augmentedMatrix.matrix[row][i], i + 1);
                cnt += 1;
            } else if (i == this.augmentedMatrix.col - 1) {
                System.out.printf("= %.4f \n", this.augmentedMatrix.matrix[row][i]);
            } else {
                if (Utils.isEqual(this.augmentedMatrix.matrix[row][i], 0))
                    continue;
                System.out.printf("%+.4fx%d ", this.augmentedMatrix.matrix[row][i], i + 1);
            }
        }
    }

    public void showEquations() {
        for (int row = 0; row < this.augmentedMatrix.row; row++) {
            showEquation(row);
        }
    }

    private void solveUsingGauss(boolean showProcess) {

        if (showProcess) {
            // STEP 1

            System.out.println("--> Cek apakah augmented matriks sudah berupa matriks eselon baris.");
            System.out.println();
            this.displayAugmentedMatrix();
            System.out.println();

            if (augmentedMatrix.isEchelon()) {
                System.out.println("Matriks di atas sudah berupa matriks eselon baris. Lanjut ke step berikutnya");
            } else {
                System.out.println(
                        "Karena matriks di atas belum merupakan matriks eselon baris, Ubah ke matriks eselon baris.");
                this.augmentedMatrix.toRowEchelon();
                this.displayAugmentedMatrix();
                System.out.println();
            }

            // STEP 2

            System.out.println();
            System.out.println("--> Setelah itu, ubah ke bentuk sistem persamaan linear.");
            System.out.println();
            this.showEquations();
            System.out.println();

            // STEP 3
            System.out.println("--> Cek apakah ada suatu persamaan yang tidak konsisten.");
            if (!this.hasSolution()) {
                System.out.println("\n--> Kesimpulannya, SPL tersebut tidak memiliki solusi.");
                System.out.println("Program selesai.");
                return;
            }

            // STEP 4
            System.out.println("""
                    --> Sekarang, selesaikan persamaan satu persatu mulai dari
                    persamaan yang paling bawah.
                    """);

            for (int row = this.augmentedMatrix.row - 1; row >= 0; row--) {
                System.out.print(" -> dari persamaan :  ");
                showEquation(row);
                solveRow(row, true);
            }

            // STEP 4
            System.out.println("""
                    --> Wrapping up, sekaligus merapihkan penamaan variabel, solusi dari SPL tersebut adalah
                    """);

            this.solve_helper();
            // this.displaySolution();
        } else {

            this.augmentedMatrix.toRowEchelon();

            if (!this.hasSolution()) {
                System.out.println("SPL ini tidak memiliki solusi.");
                System.out.println("Program selesai.");
                this.setSolution(false, "SPL ini tidak memiliki solusi.");
                return;
            }
            for (int i = this.augmentedMatrix.row - 1; i >= 0; i--) {
                this.solveRow(i, false);
            }
            this.solve_helper();
            // this.displaySolution();
        }
        this.setSolution();

    }

    public void setSolution() {
        this.setSolution(true, "");
    }

    public void setSolution(boolean isHasSolution, String error) {
        if (isHasSolution) {

            int lengthX = this.x.length;

            String parametricNaming = "abcdefghijklmnopqrstuvwyz";
            for (int i = 0; i < lengthX; i++) {
                int cnt = 0;
                solution += String.format("x%d = ", i + 1);
                if (Utils.isNotEqual(this.x[i].getConstant(), 0) || !this.x[i].hasParametricVariables()) {
                    solution += String.format("%.4f ", this.x[i].getConstant());
                    cnt++;
                }

                for (int j = 0; j < lengthX; j++) {
                    if (Utils.isEqual(this.x[i].getTmpParamElmt(j), 0))
                        continue;
                    if (cnt > 0) {
                        solution += String.format("%+.4f%c ", this.x[i].getTmpParamElmt(j),
                                parametricNaming.charAt(parametricLinking.get(j)));
                    } else {
                        solution += String.format("%.4f%c ", this.x[i].getTmpParamElmt(j),
                                parametricNaming.charAt(parametricLinking.get(j)));
                        cnt++;
                    }
                }
            }
        } else {
            solution = error;
        }
    }

    private void solveUsingGaussJordan(boolean showProcess) {
        if (showProcess) {
            // STEP 1

            System.out.println("--> Cek apakah augmented matriks sudah berupa matriks eselon baris tereduksi.");
            System.out.println();
            this.displayAugmentedMatrix();
            System.out.println();

            if (augmentedMatrix.isReducedEchelon()) {
                System.out.println(
                        "Matriks di atas sudah berupa matriks eselon baris tereduksi. Lanjut ke step berikutnya");
            } else {
                System.out.println(
                        "Karena matriks di atas belum merupakan matriks eselon baris tereduksi, Ubah ke matriks eselon baris.");
                this.augmentedMatrix.toReducedRowEchelon();
                this.displayAugmentedMatrix();
                System.out.println();
            }

            // STEP 2

            System.out.println();
            System.out.println("--> Setelah itu, ubah ke bentuk sistem persamaan linear.");
            System.out.println();
            this.showEquations();
            System.out.println();

            // STEP 3
            System.out.println("--> Cek apakah ada suatu persamaan yang tidak konsisten.");
            if (!this.hasSolution()) {
                System.out.println("\n--> Kesimpulannya, SPL tersebut tidak memiliki solusi.");
                System.out.println("Program selesai.");
                return;
            }

            // STEP 3
            System.out.println("""
                    --> Sekarang, selesaikan persamaan satu persatu mulai dari
                    persamaan yang paling bawah.
                    """);

            for (int row = this.augmentedMatrix.row - 1; row >= 0; row--) {
                System.out.print(" -> dari persamaan :  ");
                showEquation(row);
                solveRow(row, true);
            }

            // STEP 4
            System.out.println("""
                    --> Wrapping up, sekaligus merapihkan penamaan variabel, solusi dari SPL tersebut adalah
                    """);

            this.solve_helper();
            // this.displaySolution();
        } else {
            this.augmentedMatrix.toReducedRowEchelon();
            if (!this.hasSolution()) {

                System.out.println("SPL ini tidak memiliki solusi.");
                System.out.println("Program selesai.");
                this.setSolution(false, "SPL ini tidak memiliki solusi.");
                return;
            }
            for (int i = this.augmentedMatrix.row - 1; i >= 0; i--) {
                this.solveRow(i, false);
            }
            this.solve_helper();
            // this.displaySolution();
        }
        this.setSolution();
    }

    private void solveUsingInverse(boolean showProcess) {
        Matrix inversedMatrix;
        if (showProcess) {
            System.out.println("""
                    --> Misal SPL dibentuk ke dalam Ax = B,
                      | dengan A adalah matriks:""");
            this.A.displayMatrix();
            System.out.println();

            System.out.println("""
                    . | x adalah matriks:""");
            System.out.println("[");
            for (int i = 0; i < this.x.length; i++) {
                System.out.printf("\t[ x%d ]\n", i + 1);
            }
            System.out.println("]");
            System.out.println();

            System.out.println("""
                    . | dan B adalah matriks:""");
            this.B.displayMatrix();
            System.out.println();

            System.out.println(
                    "Jika solusinya tunggal (memiliki balikan/inverse), maka berlaku A^(-1)Ax = A^(-1)B <=> x = A^(-1)B \n");

            try {
                inversedMatrix = this.A.getInverse();
            } catch (Error e) {
                System.out.printf("***) Untuk menggunakan metode ini: ");
                System.out.println(e.getMessage());
                System.out.println(
                        "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");
                System.out.println("Program berhenti.");
                return;
            }

            System.out.println("""
                    --> Dapatkan inverse dari matriks A,
                      | Matriks A^(-1) = """);
            inversedMatrix.displayMatrix();
            System.out.println("""
                    --> Hitung matriks A^(-1)B,
                      | Matriks A^(-1)B = """);
            inversedMatrix.multiply(this.B);
            inversedMatrix.displayMatrix();
            System.out.println();
            System.out.println("""
                    --> Simpulkan.""");
            for (int row = 0; row < this.B.row; row++) {
                this.x[row].setConstant(inversedMatrix.matrix[row][0]);
            }

            // this.displaySolution();
        } else {
            try {
                inversedMatrix = this.A.getInverse();
            } catch (Error e) {
                System.out.printf("***) Untuk menggunakan metode ini: ");
                System.out.println(e.getMessage());
                System.out.println(
                        "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");
                System.out.println("Program berhenti.");
                this.setSolution(false, String.format("""
                            Untuk menggunakan metode ini: %s
                            Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan
                        """, e.getMessage()));
                return;
            }
            inversedMatrix.multiply(this.B);
            for (int row = 0; row < this.B.row; row++) {
                this.x[row].setConstant(inversedMatrix.matrix[row][0]);
            }

            // this.displaySolution();
        }
        this.setSolution();

    }

    private void solveUsingCramer(boolean showProcess) {
        if (showProcess) {
            System.out.println("""
                    --> Misal SPL dibentuk ke dalam Ax = B,
                      | dengan A adalah matriks:""");
            this.A.displayMatrix();
            System.out.println();

            System.out.println("""
                    . | x adalah matriks:""");
            System.out.println("[");
            for (int i = 0; i < this.x.length; i++) {
                System.out.printf("\t[ x%d ]\n", i + 1);
            }
            System.out.println("]");
            System.out.println();

            System.out.println("""
                    . | dan B adalah matriks:""");
            this.B.displayMatrix();
            System.out.println();

            System.out.println("""
                    --> Metode Cramer adalah salah satu metode untuk menyelesaikan SPL yang memiliki solusi tunggal,
                      | yaitu dengan: """);
            System.out.printf("  |\txi = |Ai|/|A| untuk setiap i yang terdefinisi.\n");
            System.out.println("""
                    . |
                      | dengan Ai adalah matriks A tetapi setiap elemen pada kolom i diganti menjadi elemen dari B.
                      | Oleh karena itu, nilai dari |A| dan |Ai| untuk setiap i yang terdefinisi haruslah ada,
                      | serta nilai dari |A| tak nol.""");

            System.out.println("\n--> Hitung nilai |A| ");

            double detA;
            try {
                detA = this.A.getDeterminant();
            } catch (Error e) {
                System.out.printf("***) Untuk menggunakan metode ini: ");
                System.out.println(e.getMessage());
                System.out.println(
                        "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");
                System.out.println("Program berhenti.");
                return;
            }

            if (Utils.isEqual(detA, 0)) {
                System.out.println(
                        "SPL tidak mendapati kesimpulan melalui metode cramer karena determinan matrika A bernilai 0.");
                System.out.println(
                        "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");

                System.out.println("Program selesai.");
                return;
            }

            System.out.printf("  | Setelah dihitung, nilai |A| = %.4f\n", detA);

            System.out.printf("""

                    --> Sekarang, untuk setiap i yang terdefinisi, yaitu 1 <= i <= jumlah variabel, yang dalam hal ini
                      | berarti i <= i <= %d.
                      |
                      """, this.A.row);

            for (int i = 0; i < this.A.row; i++) {
                System.out.printf("  | Untuk i = %d,\n", i + 1);
                System.out.printf("  | Matriks A%d =\n", i + 1);

                // copy matrix
                Matrix ai = new Matrix(this.A.row, this.A.row);
                for (int row = 0; row < this.A.row; row++) {
                    for (int col = 0; col < this.A.row; col++) {
                        if (col == i) {
                            ai.matrix[row][col] = this.B.matrix[row][0];
                            continue;
                        } else {
                            ai.matrix[row][col] = this.A.matrix[row][col];
                        }
                    }
                }
                ai.displayMatrix();

                // get det Ai
                double detAi;
                try {
                    detAi = ai.getDeterminant();
                } catch (Error e) {
                    System.out.printf("***) Untuk menggunakan metode ini: ");
                    System.out.println(e.getMessage());
                    System.out.println(
                            "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");
                    System.out.println("Program berhenti.");
                    return;
                }

                System.out.printf("""
                        . | yang mana |A%d| = %.4f, sehingga
                          | x%d = |A%d|/|A| = (%.4f)/(%.4f) = %.4f
                          |
                          |
                                        """, i + 1, detAi, i + 1, i + 1, detAi, detA, detAi / detA);
                this.x[i].setConstant(detAi / detA);
                System.out.println();

            }
            System.out.println("--> Wrapping up, solusi SPL ini adalah");
            // this.displaySolution();
        } else {
            double detA;
            try {
                detA = this.A.getDeterminant();
            } catch (Error e) {
                System.out.printf("***) Untuk menggunakan metode ini: ");
                System.out.println(e.getMessage());
                System.out.println(
                        "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");
                System.out.println("Program berhenti.");
                this.setSolution(false, String.format("""
                            Untuk menggunakan metode ini: %s
                            Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan
                        """, e.getMessage()));
                return;
            }

            if (Utils.isEqual(detA, 0)) {
                System.out.println(
                        "SPL tidak mendapati kesimpulan melalui metode cramer karena determinan matrika A bernilai 0.");
                System.out.println(
                        "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");

                System.out.println("Program selesai.");
                this.setSolution(false,
                        "SPL tidak mendapati kesimpulan melalui metode cramer karena determinan matrika A bernilai 0.\nSilakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan");
                return;
            }

            for (int i = 0; i < this.A.row; i++) {
                // copy matrix
                Matrix ai = new Matrix(this.A.row, this.A.row);
                for (int row = 0; row < this.A.row; row++) {
                    for (int col = 0; col < this.A.row; col++) {
                        if (col == i) {
                            ai.matrix[row][col] = this.B.matrix[row][0];
                            continue;
                        } else {
                            ai.matrix[row][col] = this.A.matrix[row][col];
                        }
                    }
                }

                // get det Ai
                double detAi;
                try {
                    detAi = ai.getDeterminant();
                } catch (Error e) {
                    System.out.printf("***) Untuk menggunakan metode ini: ");
                    System.out.println(e.getMessage());
                    System.out.println(
                            "***) Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan\n");

                    System.out.println("Program berhenti.");
                    this.setSolution(false, String.format("""
                                Untuk menggunakan metode ini: %s
                                Silakan coba menyelesaikan SPL ini menggunakan metode eliminasi Gauss atau Gauss-Jordan
                            """, e.getMessage()));
                    return;
                }

                this.x[i].setConstant(detAi / detA);

            }
            // this.displaySolution();
        }
        this.setSolution();
    }

    public void initSPL(int row, int col) {
        this.A = new Matrix(row, col - 1);
        this.B = new Matrix(row, 1);
        this.augmentedMatrix = new Matrix(row, col);

        this.x = new Parametric[col - 1];
        for (int i = 0; i < col - 1; i++) {
            x[i] = new Parametric(col - 1);
        }

    }

    public boolean hasSolution(boolean stay) {
        if (!stay)
            return hasSolution();
        else {
            SPL tmpSPL = getCopySPL();
            tmpSPL.augmentedMatrix.toRowEchelon();
            return (tmpSPL.hasSolution());
        }
    }

    public boolean hasSolution() {
        if (!this.augmentedMatrix.isEchelon()) {
            return hasSolution(true);
        }
        int[] allZeroRow = new int[this.B.row];
        int rowLengthInA = this.A.row;
        int colLengthInA = this.A.col;

        for (int row = rowLengthInA - 1; row >= 0; row--) {
            boolean checkRow = true;
            for (int col = 0; col < colLengthInA; col++) {
                if (Utils.isNotEqual(this.A.matrix[row][col], 0.0)) {
                    checkRow = false;
                    break;
                }
            }
            if (checkRow)
                allZeroRow[row] = 1;
        }

        for (int row = 0; row < allZeroRow.length; row++) {
            if (allZeroRow[row] == 1 && Utils.isNotEqual(this.B.matrix[row][0], 0.0)) {
                if (showProcess) {
                    System.out.printf(
                            "***) Dapat dilihat bahwa persamaan ke-%d tak konsisten, karena tidak akan ada \n***) nilai variabel yang memenuhi persamaan tsb.\n\n",
                            row + 1);
                }
                return false;
            }
        }

        return true;
    }

    private void solveRow(int row, boolean showProcess) {
        double[] rowArray = this.augmentedMatrix.matrix[row];
        int leadingOnePosition = -1;
        for (int i = 0; i < rowArray.length - 1; i++) {
            if (Utils.isEqual(rowArray[i], 1.00)) {
                leadingOnePosition = i;
                break;
            }
        }

        /**
         * Jika semua elemen kecuali paling terakhir bernilai 0,
         * haruslah elemen paling terakhir bernilai 0. Karena apabila tak nol maka tidak
         * ada solusi
         * dan sudah dihandle hasSolution();
         *
         */
        if (leadingOnePosition == -1)
            return;

        String parametricNaming = "abcdefghijklmnopqrstuvwyz";

        this.x[leadingOnePosition]
                .setConstant(this.x[leadingOnePosition].getConstant() + rowArray[rowArray.length - 1]);

        if (showProcess)
            System.out.println(" | Kita dapati:");
        for (int i = rowArray.length - 2; i > leadingOnePosition; i--) {
            double multiplier = rowArray[i];
            if (!this.x[i].getIsAssigned()) {
                this.x[i].setAsBaseParametric(i);
                if (showProcess)
                    System.out.printf(" | Jadikan x%d sebagai base parametrik, katakanlah %c\n", i + 1,
                            parametricNaming.charAt(i));
            }
            this.x[leadingOnePosition].subtract(this.x[i], multiplier);

        }
        this.x[leadingOnePosition].setIsAssigned(true);

        if (showProcess) {
            System.out.printf(" | x%d = ", leadingOnePosition + 1);
            this.x[leadingOnePosition].showParametric(SolutionStatus.NotReady);
            System.out.println();
        }

    }

    private void solve_helper() {

        // Set the effective used parametric variables.
        int lengthX = this.x.length;
        Integer[] usedParametricVariable = new Integer[lengthX];
        Arrays.fill(usedParametricVariable, 0, lengthX, 0);
        for (int i = 0; i < lengthX; i++) {
            Utils.andOrList(usedParametricVariable, this.x[i].getTmpParamElmt(), false);
        }

        // Linking variables;
        int cnt = 0;
        for (Integer i = 0; i < lengthX; i++) {
            if (usedParametricVariable[i] == 0)
                continue;
            parametricLinking.put(i, cnt);
            cnt++;
        }

        // set the effective variables used in the parametric solution
        for (Integer i = 0; i < lengthX; i++) {
            this.x[i].initializeParametricCoefficient(cnt);
            for (Integer j = 0; j < lengthX; j++) {
                if (Utils.isEqual(this.x[i].getTmpParamElmt(j), 0))
                    continue;
                this.x[i].setParametricCoefficient(parametricLinking.get(j), this.x[i].getTmpParamElmt(j));
            }
        }

    }

    public void showSolution(boolean stay) {
        SPL tmpSPL = getCopySPL();
        tmpSPL.solve();
        tmpSPL.displaySolution();
    }

    public void displayAugmentedMatrix() {
        this.augmentedMatrix.displayAugmentedMatrix(-2);
    }

}
