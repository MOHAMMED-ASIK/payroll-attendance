package org.attendance;

import java.util.Random;

public class RandomIDGenerator {
    public String RandomIDGenerator() {
        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            numbers.append(i);
        }

        String id = generateRandomID(numbers.toString(), 7);
        return id;
    }

    public static String generateRandomID(String numbers, int length) {
        Random random = new Random();
        StringBuilder idBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(numbers.length());
            char digit = numbers.charAt(randomIndex);
            idBuilder.append(digit);
        }

        return idBuilder.toString();
    }
}
