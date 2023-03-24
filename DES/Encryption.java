public class Encryption {
    public static void main(String[] args) {
        DES des = new DES();
        // Test encryption and decryption on eight different 64-bit strings with different keys
        for (int i = 0; i < 8; i++) {
            String plainText = des.randomString(64);
            String key = des.randomString(64);
            String cipherText = des.encrypt(plainText, key);
            String decryptedText = des.decrypt(cipherText, key);
            System.out.println("Round " + (i + 1) + ": " + plainText.equals(decryptedText));
        }
    }
}
