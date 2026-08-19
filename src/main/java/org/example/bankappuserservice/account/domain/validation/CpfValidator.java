package org.example.bankappuserservice.account.domain.validation;

public final class CpfValidator {

    private CpfValidator() {
    }

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11 || digits.chars().distinct().count() == 1) {
            return false;
        }
        return hasValidCheckDigits(digits);
    }

    private static boolean hasValidCheckDigits(String digits) {
        return checkDigit(digits, 9, 10) == charToInt(digits, 9)
                && checkDigit(digits, 10, 11) == charToInt(digits, 10);
    }

    private static int checkDigit(String digits, int length, int startWeight) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += charToInt(digits, i) * (startWeight - i);
        }
        int remainder = (sum * 10) % 11;
        return remainder == 10 ? 0 : remainder;
    }

    private static int charToInt(String digits, int index) {
        return digits.charAt(index) - '0';
    }
}