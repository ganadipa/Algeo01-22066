package Class;


import Utils.*;

import java.io.*;
import java.util.*;

public class Matrix {
    private Scanner userInput = new Scanner(System.in);

    // Property of the matrix dan set to public sehingga bisa di akses darimana
    // saja.
    public int row;
    public int col;
    public double matrix[][];

    /**
     * KONSTRUKTOR CLASS MATRIX
     * =========================================================
     */

    // Inisialisasi matriks tanpa baris kolom.
    public Matrix() {
        this.row = 0;
        this.col = 0;
    }

    // Inisialisasi matriks jika diberikan baris dan kolom.
    public Matrix(int row, int col) {
        this.initMatrix(row, col);
    }


    // Inisialisasi matriks
    public void initMatrix(int row, int col) {
        this.row = row;
        this.col = col;
        this.matrix = new double[row][col];

    }

    public boolean isSquare() {
        return (this.row == this.col);
    }

    public String getMatrixString() {
        String matrixString = "";
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (j == 0) {
                    matrixString += String.format("%.4f", this.matrix[i][j]);
                    continue;
                }
                matrixString += String.format(" %.4f", this.matrix[i][j]);
            }
            if (!(i == this.row - 1))
                matrixString += "\n";
        }
        return matrixString;
    }

    // read Matrix from user input
    public void readMatrix() {
        int row = 0;
        int col = 0;

        // Dapatkan jumlah baris matriks yang valid dari user.
        do {
            row = Utils.getIntInput("Masukkan jumlah baris (m): ", "Tipe masukkan diharapkan berupa integer.");
            if (row < 0) {
                System.out.println("Masukkanlah jumlah baris yang nonnegatif.");
            }
        } while (row < 0);

        // Dapatkan jumlah kolom matriks yang valid dari user.
        do {
            col = Utils.getIntInput("Masukkan jumlah kolom (n): ", "Tipe masukkan diharapkan berupa integer.");
            if (col < 0) {
                System.out.println("Masukkanlah jumlah kolom yang nonnegatif.");
            }
        } while (col < 0);

        // Early return untuk matriks yang berukuran salah satu baris/kolom adalah nol.
        if (row == 0 || col == 0)
            return;

        // Menyiapkan baris dan kolom matriks.
        this.initMatrix(row, col);

        System.out.println("Input Matriks: ");
        try {
            this.readMatrixFromUserInput();
        } catch (Exception e) {
            System.out.println("Terjadi suatu kesalahan, gagal membaca matriks.");
        }
    }

    // read Matrix from user input
    public void readSquareMatrix() {

        System.out.println("\nMasukkan panjang baris dan kolom: ");

        int N = Input.getInt(
                "Masukan harus positif",
                (Integer n) -> n >= 1);
        initMatrix(N, N);

        System.out.println("\nMasukkan matrix " + N + "x" + N + ": ");
        boolean isMatrixValid = false;
        while (!isMatrixValid) {
            try {
                readMatrixFromUserInput();
                isMatrixValid = true;
            } catch (Exception e) {
                if(e.getMessage().substring(0, 5).equals("Index"))
                    System.out.println("Masukan matriks harus berukuran "+N+"x"+N);
                else if(e.getMessage().substring(0, 5).equals("For i"))
                    System.out.println("Masukan matriks harus berupa bilangan real semua");
                else
                    System.out.println(e.getMessage());
                isMatrixValid = false;
            }
        }
    }

    public void readMatrixFromFile() {
        System.out.println("Masukkan nama file beserta ekstensinya.");
        System.out.print("(dir: test/input): ");
        String fileName = userInput.next();
        String fileInputPath = "test/input/" + fileName;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInputPath))) {
            bufferedReader.mark(10000);
            int row = 0, col = 0;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] elmts = line.split(" ");
                int tmpCol = elmts.length;
                int k = tmpCol;
                for (int i = 0; i < k; i++) {
                    if (elmts[i].length() == 0)
                        tmpCol--;
                }
                if (tmpCol < col) {
                    continue;
                }
                col = Math.max(tmpCol, col);
                row += 1;
            }

            this.initMatrix(row, col);
            bufferedReader.reset();

            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                int j = 0;
                String[] elmts = line.split(" ");
                if (elmts.length < this.col)
                    throw new IllegalArgumentException();
                for (int k = 0; k < elmts.length; k++) {
                    if (elmts[k].length() == 0)
                        continue;
                    this.matrix[i][j] = Double.parseDouble(elmts[k]);
                    j++;

                }
                i++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // Handle case saat file not found atau ada IO error.
            System.out.println("File tidak ditemukan.");
        } catch (NumberFormatException e) {
            // Handle case saat ada nonnumeric di input.
            System.out.println("Sepertinya terdapat suatu nonnumeric value di file Anda. Program berhenti.");
        } catch (IllegalArgumentException e) {
            // Jumlah elemen di setiap baris tidak konsisten.
            System.out.println("Jumlah elemen pada setiap baris tidak konsisten, program berhenti.");
        }

    }

    public void readMatrixFromUserInput() {
        // Input matriks dilakukan baris per baris (BELUM HANDLE ERROR SPESIFIK).
        for (int i = 0; i < this.row; i++) {
            String ln = userInput.nextLine();
            String[] elmts = ln.split(" ");

            for (int j = 0; j < this.col; j++) {
                this.matrix[i][j] = Double.parseDouble(elmts[j]);
            }
        }
    }

    public void chooseReadMatrixMethodFromUserInput() {
        System.out.println("""

                Cara input
                1. Keyboard
                2. File

                Pilih cara input:
                        """);
        int input = Input.getInt("Tidak ada pilihan dengan angka tersebut", (num) -> num == 1 || num == 2);
        if (input == 1) {
            readSquareMatrix();
        } else {
            readMatrixFromFile();
        }
    }

    // print matriks dengan opsi tertentu (opsi belum dibuat)
    public void displayMatrix() {
        System.out.println("[");
        for (int i = 0; i < this.row; i++) {
            System.out.print("    [");
            for (int j = 0; j < this.col; j++) {
                if (j == this.col - 1) {
                    System.out.printf(" %.4f", this.matrix[i][j]);
                    break;
                }
                System.out.printf(" %.4f ", this.matrix[i][j]);
            }
            System.out.print(" ]");
            System.out.println();
        }
        System.out.println("]");
    }

    public void displayAugmentedMatrix(int afterCol) {
        afterCol += this.col;
        afterCol %= this.col;
        System.out.println("[");
        for (int i = 0; i < this.row; i++) {
            System.out.print("    [");
            for (int j = 0; j < this.col; j++) {
                if (j == afterCol + 1) {
                    System.out.print("|");
                    System.out.printf(" %.4f", this.matrix[i][j]);
                    break;
                }
                System.out.printf(" %.4f ", this.matrix[i][j]);
            }
            System.out.print(" ]");
            System.out.println();
        }
        System.out.println("]");
    }

    // Tested
    public boolean isEchelon() {
        int currentLeadingOne = -1;

        for (int row = 0; row < this.row; row++) {
            int prev = currentLeadingOne;

            for (int col = 0; col < this.col; col++) {

                if (Utils.isEqual(this.matrix[row][col], 0.0)) {

                    continue;
                } else if (Utils.isNotEqual(this.matrix[row][col], 1.0)) {

                    return false;
                } else {

                    if (col > currentLeadingOne) {
                        currentLeadingOne = col;

                        break;
                    } else
                        return false;
                }
            }

            // jika currentLeadingOne ga keupdate berarti isi rownya cuma 0;
            if (currentLeadingOne == prev)
                currentLeadingOne = this.col;
        }

        return true;
    }

    public boolean isReducedEchelon() {

        int leadingOnePosition[];
        leadingOnePosition = new int[this.col];
        int currentLeadingOne = -1;

        for (int row = 0; row < this.row; row++) {
            int prev = currentLeadingOne;

            for (int col = 0; col < this.col; col++) {

                if (Utils.isEqual(this.matrix[row][col], 0.0)) {

                    continue;
                } else if (Utils.isNotEqual(this.matrix[row][col], 1.0)) {

                    return false;
                } else {

                    if (col > currentLeadingOne) {
                        currentLeadingOne = col;
                        leadingOnePosition[col] = 1;
                        break;
                    } else
                        return false;
                }
            }

            // jika currentLeadingOne ga keupdate berarti isi rownya cuma 0;
            if (currentLeadingOne == prev) {
                currentLeadingOne = this.col;
                continue;
            }
        }

        // check the colomn that has leading one, does it only contain the leading one?
        for (int pos = 0; pos < this.col; pos++) {
            if (leadingOnePosition[pos] == 1) {
                // checking if its colomn only contains the leading one
                for (int row = 0; row < this.row; row++) {
                    if (Utils.isNotEqual(this.matrix[row][pos], 1.0)
                            && Utils.isNotEqual(this.matrix[row][pos], 0.0)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void swapRow(int baris1, int baris2) {
        if (baris1 == baris2)
            return;
        if (baris1 < 0 || baris1 >= this.row || baris2 < 0 || baris2 >= this.row) {
            System.out.println("Input baris tidak berada di dalam range baris yang valid.");
        }

        double[] tmpBaris;
        tmpBaris = new double[this.col];
        for (int i = 0; i < this.col; i++) {
            tmpBaris[i] = this.matrix[baris1][i];
            this.matrix[baris1][i] = this.matrix[baris2][i];
            this.matrix[baris2][i] = tmpBaris[i];
        }
    }

    public int toMatrixSegitiga() {
        int swap = 0;
        boolean isSwap = false;
        for (int i = 0; i < this.col; i++) {
            if (this.matrix[i][i] == 0) {
                isSwap = false;
                for (int l = i + 1; l < this.row; l++) {
                    if (this.matrix[l][i] != 0) {
                        this.swapRow(i, l);
                        swap++;
                        isSwap = true;
                        break;
                    }
                }
                if (!isSwap) return -1;
            }

            for (int j = i + 1; j < this.row; j++) {
                double rasio = this.matrix[j][i] / this.matrix[i][i];
                for (int k = i; k < this.col; k++) {
                    this.matrix[j][k] -= rasio * this.matrix[i][k];
                }
            }
        }
        return swap;
    }

    // Metode yang digunakan untuk mendapatkan determinan
    public enum DeterminantMethod {
        RowReduction, CofactorExpansion
    }

    /**
     * Mengembalikan determinan matrix dengan metode yang dipilih.
     *
     * @param method Metode yang digunakan untuk mendapatkan determinan
     * @return determinan matrix
     * @see DeterminantMethod
     */
    public double getDeterminant(DeterminantMethod method) throws Error {
        if (row != col)
            throw new Error("Panjang baris dan kolom harus sama");

        if (method == DeterminantMethod.RowReduction) {
            Matrix copy = this.getCopyMatrix();
            int swap = copy.toMatrixSegitiga();
            System.out.println("\n\n");
            double total = 0;
            total = swap == -1 ? 0 : ((swap % 2 != 0) ? -1 : 1);
            for (int i = 0; i < copy.matrix.length; i++) {
                total *= copy.matrix[i][i];
            }

            return total;
        } else { // (method == DeterminantMethod.CofactorExpansion)
            if (this.matrix.length == 2) {
                return this.matrix[0][0] * matrix[1][1] - this.matrix[0][1] * matrix[1][0];
            } else {
                double total = 0;
                int i = 0;
                for (int j = 0; j < this.matrix.length; j++) {
                    // Bikin matrix kecilnya
                    Matrix subMatrix = new Matrix(this.matrix.length - 1, this.matrix.length - 1);
                    for (int k = 0; k < subMatrix.matrix.length; k++) {
                        for (int l = 0; l < subMatrix.matrix.length; l++) {
                            if (l >= j)
                                subMatrix.matrix[k][l] = this.matrix[k + 1][l + 1];
                            else
                                subMatrix.matrix[k][l] = this.matrix[k + 1][l];
                        }
                    }
                    total += Math.pow(-1, j) * matrix[i][j] * subMatrix.getDeterminant(method);
                }
                return total;
            }
        }
    }

    /**
     * Mengembalikan determinan matrix dengan metode default yaitu
     * CofactorExpansion.
     * 
     * @return determinan matrix
     * @see getDeterminant
     */
    public double getDeterminant() throws Error {
        return getDeterminant(DeterminantMethod.RowReduction);
    }

    public int getColomnNotEntirelyZero(int startRow, int endRow) {
        int idx = -1;

        int endTraverseRow = Math.min(this.row, endRow);
        for (int col = 0; col < this.col; col++) {
            for (int row = startRow; row < endTraverseRow; row++) {
                if (Utils.isNotEqual(this.matrix[row][col], 0)) {
                    return col;
                }
            }

        }

        return idx;
    }

    public Matrix getCopyMatrix() {
        Matrix tmp = new Matrix(this.row, this.col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                tmp.matrix[i][j] = this.matrix[i][j];
            }
        }

        return tmp;
    }

    /**
     * Mengubah matrix menjadi matrix eselon. Digunakan pada determinan. Belum
     * handle kasus kalau matrix[i-1][j] nya 0. Cara handlenya bikin swapRow dulu.
     * Trus kalau ketemu 0, swap ke paling bawah semua
     * 
     * @see getDeterminan
     */
    public void toRowEchelon() {
        // We'll be using gauss elimination

        // 1. get the first left most non zero colomn
        int currRow = 0;
        int notZeroColomn = getColomnNotEntirelyZero(currRow, this.row);
        while (notZeroColomn != -1 && currRow < this.row) {

            int row;
            for (row = currRow; row < this.row; row++) {
                if (Utils.isNotEqual(this.matrix[row][notZeroColomn], 0)) {
                    break;
                }
            }

            // swap the row
            swapRow(currRow, row);

            // make the currRow have a leading one.
            Utils.multiplyListBy(this.matrix[currRow], 1 / this.matrix[currRow][notZeroColomn]);

            // the rest row in the same colomn make it zero by subtracting or adding.
            for (row = currRow + 1; row < this.row; row++) {
                Utils.plusMinusList(this.matrix[row], this.matrix[currRow], false, this.matrix[row][notZeroColomn]);
            }

            // do it again but with the next row
            currRow += 1;
            notZeroColomn = getColomnNotEntirelyZero(currRow, this.row);

        }

        this.normalizeMatrix();

    }

    public void multiply(Matrix m) {
        if (this.col != m.row) {
            System.out.println(
                    "Jumlah kolom matriks pertama tidak sama dengan jumlah baris matriks kedua. Prosedur gagal dijalankan.");
            return;
        }

        // Get the result of the multiplied matrix
        Matrix tmp = new Matrix(this.row, m.col);
        for (int row = 0; row < tmp.row; row++) {
            for (int col = 0; col < tmp.col; col++) {

                for (int i = 0; i < tmp.col; i++) {
                    tmp.matrix[row][col] += this.matrix[row][i] * this.matrix[i][col];
                }
            }
        }

        // copy the result to this matrix
        this.initMatrix(tmp.row, tmp.col);
        for (int row = 0; row < this.row; row++) {
            for (int col = 0; col < this.col; col++) {
                this.matrix[row][col] = tmp.matrix[row][col];
            }
        }

    }

    public void multiply(double[][] m) {
        if (this.col != m.length) {
            System.out.println(
                    "Jumlah kolom matriks pertama tidak sama dengan jumlah baris matriks kedua. Prosedur gagal dijalankan.");
            return;
        }

        // Get the result of the multiplied matrix
        Matrix tmp = new Matrix(this.row, m.length);
        for (int row = 0; row < tmp.row; row++) {
            for (int col = 0; col < tmp.col; col++) {

                for (int i = 0; i < tmp.col; i++) {
                    tmp.matrix[row][col] += this.matrix[row][i] * this.matrix[i][col];
                }
            }
        }

        // copy the result to this matrix
        this.initMatrix(tmp.row, tmp.col);
        for (int row = 0; row < this.row; row++) {
            for (int col = 0; col < this.col; col++) {
                this.matrix[row][col] = tmp.matrix[row][col];
            }
        }

    }

    public void toReducedRowEchelon() {
        this.toRowEchelon();

        for (int currRow = 0; currRow < this.row; currRow++) {
            int leadingOnePosition;
            for (leadingOnePosition = 0; leadingOnePosition < this.col; leadingOnePosition++) {
                if (Utils.isEqual(this.matrix[currRow][leadingOnePosition], 1)) {
                    break;
                }
            }

            // Sudah berada di row yang isinya 0 semua, jadi ga perlu lanjut proses lagi.
            if (leadingOnePosition == this.col)
                break;

            // the other row in the colomn make it to zero.
            for (int row = 0; row < currRow; row++) {
                Utils.plusMinusList(this.matrix[row], this.matrix[currRow], false,
                        this.matrix[row][leadingOnePosition]);
            }
        }

        this.normalizeMatrix();
    }

    // To normalize matrix, make the -0 to 0.
    public void normalizeMatrix() {
        for (int row = 0; row < this.row; row++) {
            for (int col = 0; col < this.col; col++) {
                this.matrix[row][col] += 0.0;
            }
        }
    }

    // Metode yang digunakan untuk mendapatkan determinan
    public enum InverseMethod {
        GaussJordan, Adjoin
    }

    /**
     * Mengembalikan inverse matrix.
     * 
     * @return inverse matrix
     */
    public Matrix getInverse(InverseMethod method) throws Error {
        if (Utils.isEqual(getDeterminant(), 0)) {
            throw new Error("Determinan tidak boleh 0");
        } else if (row != col) {
            throw new Error("Panjang baris dan kolom harus sama");
        }

        if (method == InverseMethod.GaussJordan) {
            // Bikin matrix augmented dengan identitas di kanan
            Matrix augMatrix = new Matrix(row, col * 2);

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    augMatrix.matrix[i][j] = matrix[i][j];
                }
            }
            for (int i = 0; i < row; i++) {
                augMatrix.matrix[i][i + row] = 1;
            }

            augMatrix.toReducedRowEchelon();
            Matrix inverseMatrix = new Matrix(row, col);

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    inverseMatrix.matrix[i][j] = augMatrix.matrix[i][j + col];
                }
            }
            return inverseMatrix;
        } else {
            return getAdjoin().MultiplyByConst((double) 1 / (double) getDeterminant());
        }

    }

    /**
     * Mengembalikan inverse matrix dengan metode default yaitu gauss jordan.
     * 
     * @return inverse matrix
     */
    public Matrix getInverse() throws Error {
        return getInverse(InverseMethod.Adjoin);
    }

    /**
     * Mengembalikan matrix kofaktor.
     * 
     * @param row baris
     * @param col kolom
     * @return kofaktor matrix
     */
    public Matrix getCofactor(int row, int col) {
        if (matrix.length <= 2) {
            throw new Error("Panjang baris dan kolom minimal 3");
        }

        Matrix subMatrix = new Matrix(matrix.length - 1, matrix.length - 1);
        for (int k = 0; k < subMatrix.matrix.length; k++) {
            for (int l = 0; l < subMatrix.matrix.length; l++) {
                if (k >= row && l >= col)
                    subMatrix.matrix[k][l] = matrix[k + 1][l + 1];
                else if (k >= row)
                    subMatrix.matrix[k][l] = matrix[k + 1][l];
                else if (l >= col)
                    subMatrix.matrix[k][l] = matrix[k][l + 1];
                else
                    subMatrix.matrix[k][l] = matrix[k][l];
            }
        }

        return subMatrix;
    }

    public Matrix getAdjoin() {
        Matrix m = new Matrix(row, col);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                m.matrix[i][j] = Math.pow(-1, i + j) * getCofactor(i, j).getDeterminant();
            }
        }

        return m.transpose();
    }

    public Matrix transpose() {
        Matrix m = new Matrix(col, row);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                m.matrix[j][i] = matrix[i][j];
            }
        }
        return m;
    }

    public Matrix MultiplyByConst(double x) {
        Matrix m = new Matrix(row, col);
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                m.matrix[i][j] = matrix[i][j] * x;
            }
        }
        return m;
    }

    public Matrix multiplyBy(Matrix m) {

        Matrix result = new Matrix(this.row, m.col);

        for (int i = 0; i < result.row; i++) {
            for (int j = 0; j < result.col; j++) {
                for (int k = 0; k < this.col; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * m.matrix[k][j];
                }
            }
        }

        return result;
    }
}