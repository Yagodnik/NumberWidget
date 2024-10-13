package com.yagodnik.numbertool;

public class NumberConverter {
    public static long fromAnyBase(String numberString) {
        int base = 10;

        if (numberString.contains("0x")) {
            base = 16;
        } else if (numberString.contains("0b")) {
            base = 2;
        }

        return Long.parseLong(
                numberString.replace("0x", "")
                            .replace("0b", ""),
                base
        );
    }

    public static String toDecimal(String numberString) {
        try {
            long number = fromAnyBase(numberString);
            return Long.toString(number);
        } catch (NumberFormatException e) {
            System.err.println("Cant convert: " + numberString);
        }

        return "";
    }

    public static String toHex(String numberString) {
        try {
            long number = fromAnyBase(numberString);
            return "0x" + Long.toHexString(number);
        } catch (NumberFormatException e) {
            System.err.println("Cant convert: " + numberString);
        }

        return "";
    }

    public static String toBinary(String numberString) {
        try {
            long number = fromAnyBase(numberString);
            return "0b" + Long.toBinaryString(number);
        } catch (NumberFormatException e) {
            System.err.println("Cant convert: " + numberString);
        }

        return "";
    }
}
