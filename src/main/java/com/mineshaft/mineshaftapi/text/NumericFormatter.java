package com.mineshaft.mineshaftapi.text;

import java.text.DecimalFormat;

public class NumericFormatter {
    public static DecimalFormat getDecimalFormat() {
        return new DecimalFormat("#,###.00");
    }

    public static DecimalFormat getIntegerFormat() {
        return new DecimalFormat("#,###");
    }

    public static String formatNumber(double number) {
        return getDecimalFormat().format(number);
    }

    public static String formatNumberAdvanced(double number) {

        double updatedNumber = number - (int) number;

        if(updatedNumber==0) {
            return formatInteger((int) number);
        }
        return formatDecimal(number);
    }

    public static String formatDecimal(double decimal) {
        return getDecimalFormat().format(decimal);
    }

    public static String formatInteger(int integer) {
        return getIntegerFormat().format(integer);
    }

}
