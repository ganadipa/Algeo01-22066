package Utils;

import java.util.*;

public class Utils {

    private static double tolerance = 1e-6;


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

    public static boolean isEqual(double val1, double val2)
    {
        return (Math.abs(val1-val2) < tolerance);
    }

    public static boolean isNotEqual(double val1, double val2)
    {
        return (Math.abs(val1-val2) > tolerance);
    }

    public static void plusMinusList(double[] l1, double[] l2, boolean plus)
    {
        for (int i = 0; i < l1.length; i++)
        {
            if (plus){
                l1[i] = l1[i] + l2[i];
            } else {
                l1[i] = l1[i] - l2[i];
            }
        }
    }

    /**
     * 
     * Mengubah l1 menjadi l1+l2*multiplier atau l1-l2*multiplier,
     * @param l1
     * @param l2
     * @param plus
     * @param multiplier
     */
    public static void plusMinusList(double[] l1, double[] l2, boolean plus, double multiplier)
    {
        for (int i = 0; i < l1.length; i++)
        {
            if (plus){
                l1[i] = l1[i] + l2[i];
            } else {
                l1[i] = l1[i] - l2[i]*multiplier;
            }
        }
    }

    public static void multiplyListBy(double[] l, double multiplier)
    {
        for (int i = 0; i < l.length; i++)
        {
            l[i] *= multiplier;
        }
    }





    

    // 

}
