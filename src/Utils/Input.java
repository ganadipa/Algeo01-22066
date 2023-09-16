package Utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Function;

public class Input {
    private static Scanner userInput = new Scanner(System.in);
    public boolean success = false;

    /**
     * This function gets user input with integer data type
     * 
     * @param errorMessage This is the message that pops when it doesnt match the validator function
     * @param validator function to validate the input.
     * @return -1 if input invalid, otherwise get integer inputted from user
     */
    public int getInt(String errorMessage, Function<Integer,Boolean> validator)
    {
        success = false;
        Integer res = -1;
        while (!success) {
            try {
                res = userInput.nextInt();
                success = validator.apply(res);
                if(!success) {
                    System.out.println(errorMessage);
                }
            } catch (InputMismatchException e) {
                if(userInput.nextLine() == "exit") {
                    return res;
                }
                success = false;
                System.out.println(mustIntegerMessage);
            } 
        }

        return res;
    }
    public int getInt()
    {
        success = false;
        int res = -1;
        while (res == -1) {
            try {
                res = userInput.nextInt();
                success = true;
            } catch (InputMismatchException e) {
                success = false;
                System.out.println(mustIntegerMessage);
                userInput.nextLine();
            } 
        }

        return res;
    }

    private static final String mustIntegerMessage = "Masukan harus bilangan bulat";
}
