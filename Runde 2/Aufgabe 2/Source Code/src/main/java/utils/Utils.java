package utils;

import java.util.ArrayList;

public class Utils {

    public static boolean isEven(int num) {
        return num % 2 == 0;
    }

    public static boolean containsAny(String check, ArrayList<String> contains) {
        for (String contain : contains) {
            if(check.contains(contain))
                return true;
        }

        return false;
    }

}
