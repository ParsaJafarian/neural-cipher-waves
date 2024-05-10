package com.cipher;



import static java.lang.Character.*;

/**
 * The following is the classic Caesar-Cipher class containing static methods. Thus called "Engine"
 * This is the back-end of this program
 */
public class CC_Engine {
    public static final String puncset = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"; //list of common symbols

    /**
     * Main function is a mini CUI app to test the encryption engine
     * @param args
     */
    public static void main(String[] args) {
        String enText = CC_encrypt("Hi team, I woke up sick. I can't come to work today!", 2, false,false);
        System.out.println(enText);
        String deText = CC_decrypt(enText, 2 ,false);
        System.out.println(deText);
        System.out.println(puncset.length());
    }

    /**
     * the decryption engine is actually the same encryption engine in reverse
     * @param encrypted The cipher text
     * @param key       The key
     * @param ensy      Option to decrypt symbols
     * @return          Return the decrypted text
     */
    static String CC_decrypt(String encrypted, int key, boolean ensy){
        return CC_encrypt(encrypted, 26 - (key % 26),ensy, true);
    }

    /**
     * the encryption engine is the core of the program
     * @param encryptee The raw unencrypted text
     * @param key       The key
     * @param ensy      Option to encrypt symbols
     * @param decrypt   To specify is the engine is being used for decryption
     * @return
     */
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