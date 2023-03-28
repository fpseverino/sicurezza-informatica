public interface Cipher {
    public String encrypt(String plainText, String key) throws IllegalArgumentException;
    public String decrypt(String cipherText, String key) throws IllegalArgumentException;
}