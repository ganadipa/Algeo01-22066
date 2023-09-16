import Utils.Input;

public class Main {
    public static void main(String[] args) {

        System.out.println(
        """
[Welcome to Library Matrix]

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

        Input input = new Input();
        int chosenMenu;
        do {
            chosenMenu = input.getInt(
                "Masukan harus dalam range 1 sampai 7",
                (Integer d) -> d >= 1 && d <= 7
            );
        } while (!input.success);

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
                do {
                    chosenMenu = input.getInt(
                        "Masukan harus dalam range 1 sampai 4",
                        (Integer d) -> d >= 1 && d <= 4
                    );
                } while (!input.success);

                switch(chosenMenu) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
                

                break;

            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
        }
        
        

        

    }
    boolean a() {return true;};
}
