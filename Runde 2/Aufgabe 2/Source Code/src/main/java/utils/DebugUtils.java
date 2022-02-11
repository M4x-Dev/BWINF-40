package utils;

public class DebugUtils {

    public static final boolean DEBUG = false;

    public static void print(String s) {
        if(DEBUG) System.out.print(s);
    }

    public static void println(String s) {
        if(DEBUG) System.out.println(s);
    }

}
