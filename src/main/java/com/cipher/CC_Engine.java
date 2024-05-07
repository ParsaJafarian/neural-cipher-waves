package com.cipher;



import static java.lang.Character.*;
public class CC_Engine {
    public static final String puncset = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    public static void main(String[] args) {
        String enText = CC_encrypt("Hi team, I woke up sick. I can't come to work today!", 2, false,false);
        System.out.println(enText);
        String deText = CC_decrypt(enText, 2 ,false);
        System.out.println(deText);
        System.out.println(puncset.length());
    }
    static String CC_decrypt(String encrypted, int key, boolean ensy){
        return CC_encrypt(encrypted, 26 - (key % 26),ensy, true);
    }
    static String CC_encrypt(String encryptee, int key, boolean ensy, boolean decrypt){
        StringBuilder encrypted = new StringBuilder();
        for (char character : encryptee.toCharArray())
        {
            if (character != ' ' && character != '\n') {
                if (isLowerCase(character)) {
                    int InPos = character - 'a';
                    int FiPos = (InPos + key) % 26;
                    char enChar = (char) ('a' + FiPos);
                    encrypted.append(enChar);
                } else if (isUpperCase(character)){
                    int InPos = character - 'A';
                    int FiPos = (InPos + key) % 26;
                    char enChar = (char) ('A' + FiPos);
                    encrypted.append(enChar);
                } else{
                    if (ensy&&!decrypt){
                        int InPos = puncset.indexOf(character);
                        int FiPos = (InPos + (key%26)) % 32;
                        char enChar = puncset.charAt(FiPos);
                        encrypted.append(enChar);
                    } else if (ensy&&decrypt) {
                        int InPos = puncset.indexOf(character);
                        int FiPos = (InPos + (key%26)+6) % 32;
                        char enChar = puncset.charAt(FiPos);
                        encrypted.append(enChar);
                    } else{
                    encrypted.append(character);
                }
                }
            }
            else {
                encrypted.append(character);
            }
        }
        return encrypted.toString();
    }
}