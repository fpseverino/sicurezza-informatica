public interface Cipher {
    public static final int DES_BLOCK_SIZE = 64;
    public static final int BLOWFISH_BLOCK_SIZE = 64;
    public static final int AES_BLOCK_SIZE = 128;

    public String encrypt(String plainText, String key) throws IllegalArgumentException;
    public String decrypt(String cipherText, String key) throws IllegalArgumentException;
}