package com.cipher;

public class CC_Engine {
    public static void main(String[] args) {
        String enText = CC_encrypt("yo bro parsa this be real good for real imma have the best\ntime of my life for real ", 3);
        System.out.println(enText);
        String deText = CC_decrypt(enText, 3);
        System.out.println(deText);
    }
    static String CC_decrypt(String encrypted, int key){
        return CC_encrypt(encrypted, 26 - (key % 26));
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