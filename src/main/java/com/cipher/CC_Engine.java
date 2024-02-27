package com.cipher;

import java.util.Arrays;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

public class CC_Engine {
    public static void main(String[] args) {
        String enText = CC_encrypt("yo bro parsa this be real good for real imma have the best\ntime of my life for real ", 0);
        System.out.println(enText);
        String deText = CC_decrypt(enText, 3);
        System.out.println(deText);
    }

    static String CC_decrypt(String encrypted, int key){
        return CC_encrypt(encrypted, 26 - (key % 26));
    }
    static int CC_findkey(String encryptee){
        double[] englishLettersProbabilities = {0.073, 0.009, 0.030, 0.044, 0.130, 0.028, 0.016, 0.035, 0.074,
                0.002, 0.003, 0.035, 0.025, 0.078, 0.074, 0.027, 0.003,
                0.077, 0.063, 0.093, 0.027, 0.013, 0.016, 0.005, 0.019, 0.001};
        double[] expectedLettersFrequencies = Arrays.stream(englishLettersProbabilities)
                .map(probability -> probability * encryptee.length())
                .toArray();
        double[] chiSquares = new double[26];

        for (int key = 0; key < chiSquares.length; key++) {
            String decipheredMessage = CC_decrypt(encryptee, key);
            long[] lettersFrequencies = observedLettersFrequencies(decipheredMessage);
            double chiSquare = new ChiSquareTest().chiSquare(expectedLettersFrequencies, lettersFrequencies);
            chiSquares[key] = chiSquare;
        }
        return chiSquares;
    }
    static String CC_encrypt(String encryptee, int key){
        StringBuilder encrypted = new StringBuilder();
        for (char character : encryptee.toCharArray())
        {
            if (character != ' ' && character != '\n') {
                int InPos = character - 'a';
                int FiPos = (InPos + key) % 26;
                char enChar = (char) ('a' + FiPos);
                encrypted.append(enChar);
            }
            else {
                encrypted.append(character);
            }
        }
        return encrypted.toString();
    }
}