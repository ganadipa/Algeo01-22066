import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.Action;

import Matrix.Matrix;
import Matrix.MultipleLinearRegression;
import Utils.Input;
import Matrix.Interpolasi;
import Matrix.BicubicSplineInterpolation;

public class Main {

    public static void main(String[] args) {
        System.out.println("[Welcome to Library Matrix]");
        while(true) {
            System.out.println(
            """
    
    MENU
    1. Sistem Persamaaan Linier
    2. Determinan
    3. Matriks balikan
    4. Interpolasi Polinom
    5. Interpolasi Bicubic Spline
    6. Regresi linier berganda
    7. Keluar
            """
            );
    
            System.out.println("Pilih menu (Angka): ");
    
            int chosenMenu;
            chosenMenu = Input.getInt(
                "Masukan harus dalam range 1 sampai 7",
                (Integer n) -> n >= 1 && n <= 7
            );

            switch(chosenMenu) {
                case 1:
                    System.out.println(
            """
    
    SUBMENU Sistem Persamaaan Linier
    1. Metode eliminasi Gauss
    2. Metode eliminasi Gauss-Jordan
    3. Metode matriks balikan
    4. Kaidah Cramer
            """
                    );
                    chosenMenu = Input.getInt(
                        "Masukan harus dalam range 1 sampai 4",
                        (Integer n) -> n >= 1 && n <= 4
                    );
    
                    switch(chosenMenu) {
                        case 1:
                            System.out.println("Metode eliminasi Gauss");
                            break;
                        case 2:
                            System.out.println("Metode eliminasi Gauss-Jordan");
                            break;
                        case 3:
                            System.out.println("Metode matriks balikan");
                            break;
                        case 4:
                            System.out.println("Kaidah Cramer");
                            break;
                    }
                    
    
                    break;
    
                case 2:
                    handleDeterminan();
                    handleCobaLagi(()-> {handleDeterminan();});
                    break;
                case 3:
                    handleMatrixBalikan();
                    handleCobaLagi(()-> {handleMatrixBalikan();});
                    break;
                case 4:
                    handleInterpolasi();
                    break;
                case 5:
                    handleBicubic();
                    break;
                case 6:
                    RegresiLinierBerganda();
                    handleCobaLagi(()-> {RegresiLinierBerganda();});
                    break;
                case 7:
                    System.out.println("Keluar");
                    break;
            }
        }
    }

    static void handleBicubic() {
        System.out.println("\n[Interpolasi Bicubic Spline]");
        BicubicSplineInterpolation bicubic = new BicubicSplineInterpolation();
        Matrix matrixF = new Matrix();

        Double[] ab = matrixF.readBicubicFromFile();
        bicubic.init(matrixF);
        bicubic.setA(ab[0]);
        bicubic.setB(ab[1]);

        bicubic.solve();
    }

    static  void handleInterpolasi() {
        System.out.println("\n[Interpolasi polinom]");

        Interpolasi interpolasi = new Interpolasi();
        Matrix matrix = new Matrix();

        System.out.println("""
                Pilih cara input
                1. Keyboard
                2. File
                """);

        System.out.print("Masukkan pilihan: ");
        int input = Input.getInt("Tidak ada pilihan dengan angka tersebut", (num) -> num == 1 || num == 2);
        double x = 0;
        if (input == 1) {
            x = matrix.readInterpolasi();
        } else {
            x = matrix.readInterpolasiFromFile();
        }

        interpolasi.init(matrix);
        interpolasi.setX(x);

        interpolasi.solve();

    }

    static void handleCobaLagi(Runnable currentHandler) {
        
        System.out.println(
                """
                Coba lagi ?
                1. Ya
                2. Keluar
                
                Pilih instruksi: """
        );

        int chosenInstruction = Input.getInt(
            "Masukan harus dalam range 1 - 2",
            (Integer n) -> n == 1 || n == 2
        );

        if(chosenInstruction == 1) {
            currentHandler.run();
            return;
        }
    }

    static void handleDeterminan() {
        System.out.println("\n[Determinan]");

        System.out.println(
            """
    
            Metode
            1. Metode Reduksi Baris
            2. Metode Expansi Kofaktor

            Pilih metode: """
        );
        int chosenMethod = Input.getInt(
            "Masukan harus dalam range 1 - 2",
            (Integer n) -> n == 1 || n == 2
        );

        
        Matrix mat = new Matrix();
        mat.readSquareMatrix();

        double determinan = mat.getDeterminant(chosenMethod == 1 ? Matrix.DeterminantMethod.RowReduction : Matrix.DeterminantMethod.CofactorExpansion);

        System.out.printf("\nDeterminan: %.3f\n", determinan);

    }

    static void RegresiLinierBerganda() {
        System.out.println("\n[Regresi Linier Berganda]");
        
        MultipleLinearRegression mlr = new MultipleLinearRegression();
        Matrix mat = mlr.getMatrixFromUserInput();
        mlr.init(mat);
        mlr.solve();
    }

    static void handleMatrixBalikan() {
        System.out.println("\n[Inverse]");
        
        Matrix mat = new Matrix();
        mat.readSquareMatrix();

        Matrix determinan = mat.getInverse();

        System.out.println("\nInverse:\n");
        determinan.displayMatrix(null);
    }
}
