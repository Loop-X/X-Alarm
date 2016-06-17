package io.github.loop_x.yummywakeup.tools;

import android.util.Log;

import java.util.Random;

public class CalculationFormula {

    // Uses int to present math operations
    public final static int PLUS_SIGN = 101;
    public final static int MINUS_SIGN = 102;

    /**
     * Random int between 0 and max
     * @param max
     * @return
     */
    public static int getRandomInt(int max) {
        Random rand = new Random(max);
        return rand.nextInt();
    }

    /**
     * Random int between 1 and 100
     * @return
     */
    public static int getRandomInt() {
        Random rand = new Random();
        return rand.nextInt(100) + 1; // Return 1 to 100
    }

    /**
     * Random true or false
     * @return
     */
    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    /**
     * Random plus sign or minus sign
     * @return plus sign (101) or minus sign (102)
     */
    public static int getRandomOperation() {
        return getRandomBoolean()? PLUS_SIGN:MINUS_SIGN;
    }

    /**
     * Generate formula according to difficult level
     * @return formula in int[]
     */
    public static int[] generateFormula() {
        return initFormula(5);
    }

    /**
     * Calculates formula's result
     * @param formula
     * @return result
     */
    public static int getFormulaResult(int[] formula) {
        int result = 0;
        int signal = PLUS_SIGN;
        for (int i = 0; i < formula.length; i++) {
            if(isEvenOrOdd(i)) {
                if(signal == PLUS_SIGN) {
                    result += formula[i];
                } else {
                    result -= formula[i];
                }
            }else{
                signal = formula[i];
            }
        }
        return result;
    }

    /**
     * Gets the String format of formula
     * @param formula
     * @return
     */
    public static String getFormulaString(int[] formula) {
        String formulaString = "";
        for (int i = 0; i < formula.length; i++) {
            if(isEvenOrOdd(i)) {
                formulaString += String.valueOf(formula[i]);
            }else{
                if(formula[i] == PLUS_SIGN) {
                    formulaString += " + ";
                }else{
                    formulaString += " - ";
                }
            }
        }
        return formulaString;
    }

    /**
     * Init formula with the size given
     * @param size  3+2 -> size 3
     * @return
     */
    private static int[] initFormula(int size) {
        int[] formula = new int[size];
        for (int i = 0; i < formula.length; i++) {
            if(isEvenOrOdd(i)) {
                formula[i] = getRandomInt();
                Log.d("aaa", "" + formula[i]);
            }else{
                formula[i] = getRandomOperation();
            }
        }
        return formula;
    }

    /**
     * Checks Even or Odd
     * @param i
     * @return true if i is Even, false if Odd
     */
    public static boolean isEvenOrOdd(int i) {
        return (i%2 == 0)? true:false;
    }

}