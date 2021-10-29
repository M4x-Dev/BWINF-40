package simulation;

import java.util.Arrays;

public class LudoField {

    public static final String FIELD_EMPTY = " ";

    public static final int POSITION_PLAYER_A_START = 0;
    public static final int POSITION_PLAYER_B_START = 20;

    public final String[] mainField = new String[40];
    public final String[] goalA = new String[4];
    public final String[] goalB = new String[4];

    public LudoField() {
        Arrays.fill(mainField, FIELD_EMPTY);
        Arrays.fill(goalA, FIELD_EMPTY);
        Arrays.fill(goalB, FIELD_EMPTY);
    }

    public void printField() {
        String[] fieldCopy = new String[mainField.length];
        for(int i = 0; i < mainField.length; i++) fieldCopy[i] = mainField[i].equals(FIELD_EMPTY) ? "X" : mainField[i];

        String[] goalACopy = new String[goalA.length];
        for(int i = 0; i < goalA.length; i++) goalACopy[i] = goalA[i] = goalA[i].equals(FIELD_EMPTY) ? "X" : goalA[i];

        String[] goalBCopy = new String[goalB.length];
        for(int i = 0; i < goalB.length; i++) goalBCopy[i] = goalB[i] = goalB[i].equals(FIELD_EMPTY) ? "X" : goalB[i];

        System.out.println();
        System.out.println(getPadding(4) + fieldCopy[18] + " " + fieldCopy[19] + " " + fieldCopy[20] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[17] + " " + goalBCopy[0] + " " + fieldCopy[21] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[16] + " " + goalBCopy[1] + " " + fieldCopy[22] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[15] + " " + goalBCopy[2] + " " + fieldCopy[23] + getPadding(4));
        System.out.println(fieldCopy[10] + " " + fieldCopy[11] + " " + fieldCopy[12] + " " + fieldCopy[13] + " " + fieldCopy[14] + " " + goalBCopy[3] + " " + fieldCopy[24] + " " + fieldCopy[25] + " " + fieldCopy[26] + " " + fieldCopy[27] + " " + fieldCopy[28]);
        System.out.println(fieldCopy[9] + getPadding(9) + " " + fieldCopy[29]);
        System.out.println(fieldCopy[8] + " " + fieldCopy[7] + " " + fieldCopy[6] + " " + fieldCopy[5] + " " + fieldCopy[4] + " " + goalACopy[3] + " " + fieldCopy[34] + " " + fieldCopy[33] + " " + fieldCopy[32] + " " + fieldCopy[31] + " " + fieldCopy[30]);
        System.out.println(getPadding(4) + fieldCopy[3] + " " + goalACopy[2] + " " + fieldCopy[35] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[2] + " " + goalACopy[1] + " " + fieldCopy[36] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[1] + " " + goalACopy[0] + " " + fieldCopy[37] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[0] + " " + fieldCopy[39] + " " + fieldCopy[38] + getPadding(4));
        System.out.println();
    }

    private String getPadding(int width) {
        return "  ".repeat(width);
    }

}