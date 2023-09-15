


import Utils.*;
import java.util.*;

public class Matrix {

    private static Scanner userInput = new Scanner(System.in);
    

    // Property of the matrix dan set to public sehingga bisa di akses darimana saja.
    public int row;
    public int col;
    public double matrix[][];
    
    /**
     * Konstruktor
     */

    // Inisialisasi matriks tanpa baris kolom.
    Matrix()
    {
        this.row = 0;
        this.col = 0;
    }


    // Inisialisasi matriks jika diberikan baris dan kolom.
    Matrix(int row, int col)
    {
        this.initMatrix(row, col);
    }

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

        // Input matriks dilakukan baris per baris (BELUM HANDLE ERROR).
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
                if (j == this.col) {
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
}



