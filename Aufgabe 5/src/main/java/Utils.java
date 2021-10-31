import java.util.ArrayList;

public class Utils {

    public static int sumIntegerList(ArrayList<Integer> list) {
        int sum = 0;

        for(Integer num : list)
            sum += num;

        return sum;
    }

}