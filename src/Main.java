import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.Action;

import Matrix.Matrix;
import Matrix.MultipleLinearRegression;
import Matrix.SPL;
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
                    handleSPL();
                    handleCobaLagi(() -> {handleSPL();});
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
                    handleCobaLagi(()-> {handleInterpolasi();});
                    break;
                case 5:
                    handleBicubic();
                    handleCobaLagi(()-> {handleBicubic();});
                    break;
                case 6:
                    RegresiLinierBerganda();
                    handleCobaLagi(()-> {RegresiLinierBerganda();});
                    break;
                case 7:
                    System.out.println("Keluar");
                    return;
            }
        }
    }
    static void handleCobaLagi(Runnable currentInstruction) {

        boolean isCobaLagi = true;
        while(isCobaLagi) {
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
                isCobaLagi = true;
                currentInstruction.run();
            }
            else isCobaLagi = false;

        }
        
    }

    static void handleBicubic() {
        System.out.println("\n[Interpolasi Bicubic Spline]");
        BicubicSplineInterpolation bicubic = new BicubicSplineInterpolation();
        bicubic.chooseReadVariablesMethodFromUserInput();
        bicubic.solve();
    }

    static void handleInterpolasi() {
        System.out.println("\n[Interpolasi polinom]");

        Interpolasi interpolasi = new Interpolasi();
        interpolasi.chooseReadVariablesMethodFromUserInput();
        interpolasi.solve();

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
        mat.chooseReadMatrixMethodFromUserInput();

        double determinan = mat.getDeterminant(chosenMethod == 1 ? Matrix.DeterminantMethod.RowReduction : Matrix.DeterminantMethod.CofactorExpansion);

        System.out.printf("\nDeterminan: %.3f\n", determinan);

    }

    static void handleMatrixBalikan() {
        System.out.println("\n[Matrix Balikan]");

        System.out.println(
"""
    
Metode
1. Metode Gauss Jordan
2. Metode Adjoin

Pilih metode: """
        );
        int chosenMethod = Input.getInt(
            "Masukan harus dalam range 1 - 2",
            (Integer n) -> n == 1 || n == 2
        );
        Matrix mat = new Matrix();
        mat.chooseReadMatrixMethodFromUserInput();

        Matrix determinan = mat.getInverse(chosenMethod == 1 ? Matrix.InverseMethod.GaussJordan : Matrix.InverseMethod.Adjoin);

        System.out.println("\nInverse:\n");
        determinan.displayMatrix();
    }

    static void RegresiLinierBerganda() {
        System.out.println("\n[Regresi Linier Berganda]");
        
        MultipleLinearRegression mlr = new MultipleLinearRegression();
        mlr.chooseReadVariablesMethodFromUserInput();
        mlr.solve();
        mlr.display();
    }

    static void handleSPL()
    {
        System.out.println(
            """
    
    MENU
    1. Metode eliminasi Gauss
    2. Metode eliminasi Gauss-Jordan
    3. Metode matriks balikan
    4. Kaidah Cramer
            """
        );
    
        System.out.println("Pilih menu (Angka): ");

        int chosenMenu = Input.getInt(
            "Masukan harus dalam range 1 sampai 7",
            (Integer n) -> n >= 1 && n <= 4
        );

        Matrix mat = new Matrix();
        mat.chooseReadMatrixMethodFromUserInput();
        SPL spl = new SPL(0, 0);
        spl.setMethod(chosenMenu == 1 ? SPL.SPLMethod.Gauss : chosenMenu == 2 ? SPL.SPLMethod.GaussJordan : chosenMenu == 3 ? SPL.SPLMethod.Inverse : SPL.SPLMethod.Cramer);
        spl.solve();
        handleCobaLagi(() -> {handleSPL();});
        
    }
}
