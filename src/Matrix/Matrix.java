package Matrix;

import Utils.*;

import java.io.*;
import java.util.*;

public class Matrix{
    private Scanner userInput = new Scanner(System.in);
    

    // Property of the matrix dan set to public sehingga bisa di akses darimana saja.
    public int row;
    public int col;
    public double matrix[][];
    
    /**
     * KONSTRUKTOR CLASS MATRIX =========================================================
     */

    // Inisialisasi matriks tanpa baris kolom.
    public Matrix()
    {
        this.row = 0;
        this.col = 0;
    }


    // Inisialisasi matriks jika diberikan baris dan kolom.
    public Matrix(int row, int col)
    {
        this.initMatrix(row, col);
    }

    // ***********************************************************************************

    // Inisialisasi matriks
    public void initMatrix(int row, int col)
    {
        this.row = row;
        this.col = col;
        this.matrix = new double[row][col];

    }

    // read Matrix from user input
    public void readMatrix(){
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
        if (row == 0 || col == 0) return;

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
    public void readSquareMatrix(){

        System.out.println("\nMasukkan panjang baris dan kolom: ");

        int N = Input.getInt(
            "Masukan harus positif",
            (Integer n) -> n >= 1
        );
        initMatrix(N, N);

        System.out.println("\nMasukkan matrix "+N+"x"+N+": ");
        boolean isMatrixValid = false;
        while(!isMatrixValid) {
            try {
                readMatrixFromUserInput();
                isMatrixValid = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                isMatrixValid = false;
            }
        }
    }

    public void readMatrixFromFile() throws Exception {
        System.out.println("Masukkan nama file beserta ekstensinya.");
        System.out.print("(dir: test/input): ");
        String fileName = userInput.next();
        String fileInputPath = "test/input/" + fileName;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInputPath))){
            bufferedReader.mark(1000);
            int row = 0, col = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {   
                int tmpCol = line.split(" ").length;
                if (tmpCol < col) {
                    continue;
                }
                col = Math.max(tmpCol, col);
                row += 1;
            }


            this.initMatrix(row, col);
            bufferedReader.reset();


            int i = 0;
            while((line = bufferedReader.readLine()) != null) {
                String[] elmts = line.split(" ");
                if (elmts.length == 0) continue;
                if (elmts.length < this.col) throw new IllegalArgumentException();
                for (int j = 0; j < elmts.length; j++)
                {
                    this.matrix[i][j] = Double.parseDouble(elmts[j]);
                }
                i++;
            }
        } catch (IOException e) {
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

    public void readMatrixFromUserInput() throws Exception {
        // Input matriks dilakukan baris per baris (BELUM HANDLE ERROR SPESIFIK).
        for (int i = 0; i < this.row; i++)
        {
            String ln = userInput.nextLine();
            String[] elmts = ln.split(" ");
            
            for (int j = 0; j < this.col; j++)
            {
                this.matrix[i][j] = Double.parseDouble(elmts[j]);
            }
        }
    }


    // print matriks dengan opsi tertentu (opsi belum dibuat)
    public void displayMatrix(String opt) {
        System.out.println("[");
        for (int i = 0; i < this.row; i++)
        {
            System.out.print("    [");
            for (int j = 0; j < this.col; j++)
            {
                if (j == this.col - 1) {
                    if (opt == "augmented") {System.out.print("|");}
                    System.out.printf(" %.4f", this.matrix[i][j]);
                    break;
                }
                System.out.printf(" %.4f ", this.matrix[i][j]);
            }
            System.out.print("]");
            System.out.println();
        }
        System.out.println("]");
    }


    // Tested
    public boolean isEchelon() {
        int currentLeadingOne = -1;

        for(int row = 0; row < this.row; row++)
        {
            int prev = currentLeadingOne;

            for (int col = 0; col < this.col; col++)
            {

                if (Utils.isEqual(this.matrix[row][col], 0.0  )){

                    continue;
                } else if (Utils.isNotEqual(this.matrix[row][col], 1.0 )){

                    return false;
                } else {

                    if (col > currentLeadingOne){
                        currentLeadingOne = col;
                        
                        break;
                    } else return false;
                }
            }

            // jika currentLeadingOne ga keupdate berarti isi rownya cuma 0;
            if (currentLeadingOne == prev) currentLeadingOne = this.col;
        }


        return true;
    }

    public boolean isReducedEchelon() {

        int leadingOnePosition[];
        leadingOnePosition = new int[this.col];
        int currentLeadingOne = -1;

        for(int row = 0; row < this.row; row++)
        {
            int prev = currentLeadingOne;

            for (int col = 0; col < this.col; col++)
            {

                if (Utils.isEqual(this.matrix[row][col], 0.0  )){

                    continue;
                } else if (Utils.isNotEqual(this.matrix[row][col], 1.0 )){

                    return false;
                } else {

                    if (col > currentLeadingOne){
                        currentLeadingOne = col;
                        leadingOnePosition[col] = 1;
                        break;
                    } else return false;
                }
            }

            // jika currentLeadingOne ga keupdate berarti isi rownya cuma 0;
            if (currentLeadingOne == prev) {
                currentLeadingOne = this.col;
                continue;
            }
        }

        // check the colomn that has leading one, does it only contain the leading one?
        for (int pos = 0; pos < this.col; pos++){
            if (leadingOnePosition[pos] == 1) {
                // checking if its colomn only contains the leading one 
                for (int row = 0; row < this.row; row++)
                {
                    if (Utils.isNotEqual(this.matrix[row][pos], 1.0) 
                        && Utils.isNotEqual(this.matrix[row][pos], 0.0)) 
                    {
                        return false;
                    }
                }
            }
        }


        return true;
    }

    public void swapRow(int baris1, int baris2) {
        if (baris1 == baris2) return;
        if (baris1 < 0 || baris1 >= this.row || baris2 < 0 || baris2 >= this.row) {
            System.out.println("Input baris tidak berada di dalam range baris yang valid.");
        }

        

        double tmpBaris[];
        tmpBaris = new double[this.col];
        for (int i = 0; i < this.col; i++)
        {
            tmpBaris[i] = this.matrix[baris1][i];
            this.matrix[baris1][i] = this.matrix[baris2][i];
            this.matrix[baris2][i] = tmpBaris[i];
        }
    }

 




    // Metode yang digunakan untuk mendapatkan determinan
    public enum DeterminantMethod {
        RowReduction, CofactorExpansion
    } 
    /**
    * Mengembalikan determinan matrix dengan metode yang dipilih.
    *
    * @param  method Metode yang digunakan untuk mendapatkan determinan
    * @return  determinan matrix
    * @see DeterminantMethod
    */
    public double getDeterminant(DeterminantMethod method) {
        if(row != col) throw new Error("Panjang baris dan kolom harus sama");

        if(method == DeterminantMethod.RowReduction) {
            this.toRowEchelon();
            double total = 1;
            for(int i = 0; i < matrix.length; i++) {
                total *= matrix[i][i];
            }
            return total;
        }
        else { // (method == DeterminantMethod.CofactorExpansion)

            if(matrix.length == 2) {
                return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0];
            }

            double total = 0;
            int i = 0;
            for(int j = 0; j < matrix.length; j++) {
                
                // Bikin matrix kecilnya
                Matrix subMatrix = new Matrix(matrix.length-1, matrix.length-1);
                for(int k = 0; k < subMatrix.matrix.length; k++) {
                    for(int l = 0; l < subMatrix.matrix.length; l++) {
                        if(k >= i && l >= j) subMatrix.matrix[k][l] = matrix[k+1][l+1];
                        else if(k >= i) subMatrix.matrix[k][l] = matrix[k+1][l];
                        else if(l >= j) subMatrix.matrix[k][l] = matrix[k][l+1];
                        else subMatrix.matrix[k][l] = matrix[k][l];
                    }
                }

                total += Math.pow(-1, j) * matrix[i][j] * subMatrix.getDeterminant(method);
            }

            return total;
        }
    }

    public int getColomnNotEntirelyZero(int startRow, int endRow)
    {
        int idx = -1;
        
        int endTraverseRow = Math.min(this.row, endRow);
        for (int col = 0; col < this.col; col++)
        {
            for (int row = startRow; row < endTraverseRow; row++)
            {
                if (Utils.isNotEqual(this.matrix[row][col], 0)) {
                    return col;
                }
            }
            
        }

        

        return idx;
    }

    /**
    * Mengubah matrix menjadi matrix eselon. Digunakan pada determinan. Belum handle kasus kalau matrix[i-1][j] nya 0. Cara handlenya bikin swapRow dulu. Trus kalau ketemu 0, swap ke paling bawah semua
    * @see getDeterminan
    */
    public void toRowEchelon() {
        // We'll be using gauss elimination

        // 1. get the first left most non zero colomn
        int currRow = 0;
        int notZeroColomn = getColomnNotEntirelyZero(currRow, this.row);

        while (notZeroColomn != -1 && currRow < this.row)
        {
            int row;
            for (row = currRow; row < this.row; row++)
            {
                if (Utils.isNotEqual(this.matrix[row][notZeroColomn], 0))
                {
                    break;
                }
            }

            // swap the row
            swapRow(currRow, row);

            // make the currRow have a leading one.
            Utils.multiplyListBy(this.matrix[currRow], 1/this.matrix[currRow][notZeroColomn]);


            // the rest row in the same colomn make it zero by subtracting or adding.
            for (row = currRow+1; row < this.row; row++){
                Utils.plusMinusList(this.matrix[row],  this.matrix[currRow], false, this.matrix[row][notZeroColomn]);
            }

            // do it again but with the next row
            currRow += 1;
            notZeroColomn = getColomnNotEntirelyZero(currRow, this.row);
        }

        this.normalizeMatrix();
    }

    public void toRowReducedEchelon() {
        this.toRowEchelon();

        for( int currRow = 0; currRow < this.row; currRow++)
        {
            int leadingOnePosition= -1;
            for (leadingOnePosition = 0; leadingOnePosition < this.col; leadingOnePosition++)
            {
                if (Utils.isEqual(this.matrix[currRow][leadingOnePosition], 1)) break;
            }

            // Sudah berada di row yang isinya 0 semua, jadi ga perlu lanjut proses lagi.
            if (leadingOnePosition == -1) break;

            // the other row in the colomn make it to zero.
            for (int row = 0; row < this.row; row++)
            {
                if (currRow == row) break;
                Utils.plusMinusList(this.matrix[row], this.matrix[currRow], false, this.matrix[row][leadingOnePosition]);
            }
        }
    }

    // To normalize matrix, make the -0 to 0.
    public void normalizeMatrix()
    {
        for (int row = 0; row < this.row; row++)
        {
            for (int col = 0; col < this.col; col++)
            {
                this.matrix[row][col] += 0.0;
            }
        }
    }


    /**
    * Mengembalikan inverse matrix.
    * @return  inverse matrix
    */
    public Matrix getInverse() {
        if(getDeterminant(DeterminantMethod.CofactorExpansion) == 0) {
            throw new Error("Determinan tidak boleh 0");
        }
        else if(row != col) {
            throw new Error("Panjang baris dan kolom harus sama");
        }

        // Bikin matrix augmented dengan identitas di kanan
        Matrix augMatrix = new Matrix(row, col*2);
        
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                augMatrix.matrix[i][j] = matrix[i][j];
            }
        }
        for(int i = 0; i < row; i++) {
            augMatrix.matrix[i][i+3] = 1;
        }

        augMatrix.toRowReducedEchelon();
        Matrix inverseMatrix = new Matrix(row, col);

        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                inverseMatrix.matrix[i][j] = augMatrix.matrix[i][j+col];
            }
        }

        return inverseMatrix;
    }

    public void readInterpolasi() {
        System.out.print("Masukkan  banyak titik: ");
        int n = Input.getInt("Banyak titik harus lebih besar dari 0", (num) -> num > 0);

        this.initMatrix(n, n+1);

        for (int i = 0; i < this.row; i++) {
            System.out.printf("Masukan titik ke %d: ", i + 1);
            String input = userInput.nextLine();
            String[] titik = input.split(" ");
            double x = Double.parseDouble(titik[0]);
            double y = Double.parseDouble(titik[1]);

            for (int j = 0; j < this.col; j++) {
                if (j == this.col - 1) {
                    matrix[i][j] = y;
                }else {
                    matrix[i][j] = Math.pow(x,j);
                }
            }
            //c + bx + ax2 = y
        }
    }

    public void readInterpolasiFromFile() {
        System.out.println("Masukkan nama file beserta ekstensinya.");
        System.out.print("(dir: test/input): ");
        String fileName = userInput.next();
        String fileInputPath = "test/input/" + fileName;
        List<String> titiks = new LinkedList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInputPath))){
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                titiks.add(line);
            }

            this.initMatrix(titiks.size(), titiks.size() + 1);

            for (int i = 0; i < this.row; i++) {
                String[] titik = titiks.get(i).split(" ");
                double x = Double.parseDouble(titik[0]);
                double y = Double.parseDouble(titik[1]);
                for (int j = 0; j < this.col; j++) {
                    if (j == this.col - 1) {
                        matrix[i][j] = y;
                    }else {
                        matrix[i][j] = Math.pow(x,j);
                    }
                }
            }
        } catch (IOException e) {
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
}