package com.api.manager.people.util.validation;

public class DocumentValidation {
    public static boolean isValidCpf(String document) {
        String cleanDocument = document.replaceAll("[.-]", "");

        if (cleanDocument.length() != 11) {
            throw new IllegalArgumentException("CPF must have 11 digits!");
        }

        int firstDigit = calculateDigit(cleanDocument, 9, 10);
        int secondDigit = calculateDigit(cleanDocument, 10, 11);

        // Compare the verified digits with the given
        return cleanDocument.charAt(9) == Character.forDigit(firstDigit, 10) &&
                cleanDocument.charAt(10) == Character.forDigit(secondDigit, 10);
    }

    private static int calculateDigit(String cpf, int length, int factor) {
        int sum = 0;

        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * factor--;
        }

        int digit = sum % 11;
        return (digit < 2) ? 0 : 11 - digit;
    }
}
