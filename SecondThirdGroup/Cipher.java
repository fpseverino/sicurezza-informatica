public interface Cipher {
    String encrypt(String plainText, String key);
    String decrypt(String cipherText, String key);
}