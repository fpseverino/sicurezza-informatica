public class Encryption {
    public static void main(String[] args) {
        DES des = new DES();
        // Test encryption and decryption on eight different 64-bit strings with different keys
        for (int i = 0; i < 8; i++) {
            String plainText = randomString(64);
            String key = randomString(64);
            String cipherText = des.encrypt(plainText, key);
            String decryptedText = des.decrypt(cipherText, key);
            System.out.println("Round " + (i + 1) + ": " + plainText.equals(decryptedText));
        }
    }

    public static String randomString(int length) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length; i++)
            output.append((int)(Math.random()*2));
        return output.toString();
    }
}
