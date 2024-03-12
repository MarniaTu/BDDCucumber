package ru.netology.data;

import lombok.Value;
import org.checkerframework.checker.units.qual.C;

import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }


    public static String getVerificationCodeFor() {
        return "12345";
    }

    @Value
    public static class CardDetails {
        private String cardNumber;
        private String testId;
    }

    public static CardDetails getFirstCardDetails() {
        return new CardDetails("5559 0000 0000 0001", "92df3f1c-a033-48e6-8390-206f6b1f56c0");
    }

    public static CardDetails getSecondCardDetails() {
        return new CardDetails("5559 0000 0000 0002", "0f3f5c2a-249e-4c3d-8287-09f7a039391d");
    }

    public static String getMaskedCardNumber(String cardNumber) {
        String maskedCardNumber;
        return "**** **** **** " + cardNumber.substring(15);
    }

    public static int generateValidTransferAmount(int balance) {
        return new Random().nextInt(Math.abs(balance)) + 1;
    }

    public static int generateInvalidTransferAmount(int balance) {
        return Math.abs(balance) + new Random().nextInt(10000);
    }


}
