package utils;

public class DebugUtils {

    public static final boolean DEBUG = true;

    public static void print(String s) {
        if(DEBUG) System.out.print(s);
    }

    public static void println(String s) {
        if(DEBUG) System.out.println(s);
    }

}
