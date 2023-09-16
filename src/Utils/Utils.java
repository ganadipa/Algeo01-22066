package Utils;

import java.util.*;

public class Utils {


    private static Scanner userInput = new Scanner(System.in);

    // Jika gagal mendapatkan input dari user, mengembalikan -1.
    public static int getIntInput(String message, String mismatchMessage)
    {

        int res = -1;
        System.out.print(message);
        while (res == -1) {
            try {
                res = userInput.nextInt();
                if (res == -1) break;
            } catch (InputMismatchException e) {
                System.out.println(mismatchMessage);
            } 
        }

        return res;
    }






    

    // 

}
