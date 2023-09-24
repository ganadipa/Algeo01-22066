package Utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Function;

public class Input {
    public static Scanner userInput = new Scanner(System.in);
    static boolean success = false;

    /**
    * Input dengan validasi integer sekalian validator ekstra dengan error message ekstranya.
    *
    * @param  errorMessage pesan yang ditampilkan jika validator mengembalikan false
    * @param  validator masukan fungsi lambda untuk validasi tambahan terhadap input jika input memang integer
    * @return      input integer dari user
    */
    public static int getInt(String errorMessage, Function<Integer,Boolean> validator)
    {
        success = false;
        Integer num = -1;
        while (!success) {
            try {
                num = userInput.nextInt();
                success = validator.apply(num);
                if(!success) {
                    System.out.println(errorMessage);
                }
            } catch (InputMismatchException e) {
                success = false;
                System.out.println(mustIntegerMessage);
                userInput.next();
            } 
        }

        return num;
    }

    public static double getDouble(String errorMessage, Function<Double,Boolean> validator)
    {
        success = false;
        double num = -1;
        while (!success) {
            try {
                num = userInput.nextDouble();
                success = validator.apply(num);
                if(!success) {
                    System.out.println(errorMessage);
                }
            } catch (InputMismatchException e) {
                success = false;
                System.out.println(mustIntegerMessage);
                userInput.next();
            }
        }

        return num;
    }

    /**
    * Input tanpa validasi integer error message
    *
    * @return      input integer dari user
    */
    public static int getInt()
    {
        success = false;
        int res = -1;
        while (!success) {
            try {
                res = userInput.nextInt();
                success = true;
            } catch (InputMismatchException e) {
                success = false;
                System.out.println(mustIntegerMessage);
            } 
        }

        return res;
    }

    /**
    * Input tanpa validasi double error message
    * @return      input double dari user
    */
    public static double getDouble()
    {
        success = false;
        int res = -1;
        while (!success) {
            try {
                res = userInput.nextInt();
                success = true;
            } catch (InputMismatchException e) {
                success = false;
                System.out.println(mustIntegerMessage);
            } 
        }

        return res;
    }


    private static final String mustIntegerMessage = "Masukan harus bilangan bulat";
}
