package Matrix;

import Utils.*;

import java.io.*;
import java.util.*;

public class Matrix {

    private static Scanner userInput = new Scanner(System.in);
    

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

    // Inisialisasi matriks
    public void initMatrix(int row, int col)
    {
        this.row = row;
        this.col = col;
        this.matrix = new double[row][col];
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                matrix[i][j] = 0;
            }
        }
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
        int row = 0;

        System.out.println("Membuat matriks persegi berukuran n x n...\n");

        // Dapatkan jumlah panjang matriks yang valid dari user.
        do {
            row = Utils.getIntInput("Masukkan jumlah baris (n): ", "Tipe masukkan diharapkan berupa integer.");
            if (row < 0) {
            System.out.println("Masukkanlah jumlah baris yang nonnegatif.");
            }
        } while (row < 0);

        // Early return untuk matriks yang berukuran salah satu baris/kolom adalah nol.
        if (row == 0) return;

        // Menyiapkan baris dan kolom matriks.
        this.initMatrix(row, row);
        
        System.out.println("Input Matriks: ");
        try {
            this.readMatrixFromUserInput();
        } catch (Exception e) {
            System.out.println("Terjadi suatu kesalahan, gagal membaca matriks.");
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
                col = Math.max(line.split(" ").length, col);
                row += 1;
            }

            this.initMatrix(row, col);
            bufferedReader.reset();

            int i = 0;
            while((line = bufferedReader.readLine()) != null) {
                String[] elmts = line.split(" ");
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
                    System.out.printf(" %.3f", this.matrix[i][j]);
                    break;
                }
                System.out.printf(" %.3f ", this.matrix[i][j]);
            }
            System.out.print("]");
            System.out.println();
        }
        System.out.println("]");
    }


    // Metode yang digunakan untuk mendapatkan determinan
    public enum DeterminantMethod {
        RowReduction, CofactorExpansion
    } 
    public double getDeterminant(DeterminantMethod method) {
        if(matrix.length != matrix[0].length) throw new Error("Panjang dan lebar matrix harus sama");

        if(method == DeterminantMethod.RowReduction) {
            return 0;
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
                subMatrix.displayMatrix(null);


                total += Math.pow(-1, j) * matrix[i][j] * subMatrix.getDeterminant(method);
                System.out.println(total);
            }

            return total;
        }
    }

}