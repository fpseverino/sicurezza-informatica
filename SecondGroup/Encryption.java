public class Encryption {
    public static void main(String[] args) {
        Encryption encryption = new Encryption();
        DES des = new DES();
        for (int i = 0; i < 8; i++) {
            try {
                String plainText = Encryption.randomString(2048);
                String key = Encryption.randomString(64);
                
                String CBCcipherText = encryption.CBCencrypt(des, Cipher.DES_BLOCK_SIZE, plainText, key);
                String CBCdecryptedText = encryption.CBCdecrypt(des, Cipher.DES_BLOCK_SIZE, CBCcipherText, key);
                if (!plainText.equals(CBCdecryptedText)) {
                    System.out.println("DES CBC " + (i + 1) + ": " + plainText.equals(CBCdecryptedText));
                    System.out.println("Plain text: " + plainText);
                    System.out.println("Cipher text: " + CBCcipherText);
                    System.out.println("Decrypted text: " + CBCdecryptedText);
                    return;
                }

                String CFBcipherText = encryption.CFBencrypt(des, Cipher.DES_BLOCK_SIZE, plainText, key);
                String CFBdecryptedText = encryption.CFBdecrypt(des, Cipher.DES_BLOCK_SIZE, CFBcipherText, key);
                if (!plainText.equals(CFBdecryptedText)) {
                    System.out.println("DES CFB " + (i + 1) + ": " + plainText.equals(CFBdecryptedText));
                    System.out.println("Plain text: " + plainText);
                    System.out.println("Cipher text: " + CFBcipherText);
                    System.out.println("Decrypted text: " + CFBdecryptedText);
                    return;
                }

                String OFBcipherText = encryption.OFBencrypt(des, Cipher.DES_BLOCK_SIZE, plainText, key);
                String OFBdecryptedText = encryption.OFBdecrypt(des, Cipher.DES_BLOCK_SIZE, OFBcipherText, key);
                if (!plainText.equals(OFBdecryptedText)) {
                    System.out.println("DES OFB " + (i + 1) + ": " + plainText.equals(OFBdecryptedText));
                    System.out.println("Plain text: " + plainText);
                    System.out.println("Cipher text: " + OFBcipherText);
                    System.out.println("Decrypted text: " + OFBdecryptedText);
                    return;
                }

                String CTRcipherText = encryption.CTRencrypt(des, Cipher.DES_BLOCK_SIZE, plainText, key);
                String CTRdecryptedText = encryption.CTRdecrypt(des, Cipher.DES_BLOCK_SIZE, CTRcipherText, key);
                if (!plainText.equals(CTRdecryptedText)) {
                    System.out.println("DES CTR " + (i + 1) + ": " + plainText.equals(CTRdecryptedText));
                    System.out.println("Plain text: " + plainText);
                    System.out.println("Cipher text: " + CTRcipherText);
                    System.out.println("Decrypted text: " + CTRdecryptedText);
                    return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        System.out.println("DES: All tests passed.");
    }

    public static char XOR(char bit1, char bit2) {
        if (bit1 == bit2) return '0';
        else return '1';
    }

    public static String XOR(String string1, String string2) {
        String output = "";
        for (int i = 0; i < string1.length(); i++)
            output += XOR(string1.charAt(i), string2.charAt(i));
        return output;
    }

    public static String leftShift(int times, String input) {
        String output = input;
        for (int i = 0; i < times; i++)
            output = output.substring(1) + output.charAt(0);
        return output;
    }

    public static String randomString(int length) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length; i++)
            output.append((int)(Math.random()*2));
        return output.toString();
    }

    public static String increment(String binaryString) {
        String result = "";
        int carry = 1;
        for (int i = binaryString.length() - 1; i >= 0; i--) {
            int sum = Integer.parseInt(binaryString.charAt(i) + "") + carry;
            if (sum == 2) {
                result = "0" + result;
                carry = 1;
            } else if (sum == 1) {
                result = "1" + result;
                carry = 0;
            } else {
                result = "0" + result;
                carry = 0;
            }
        }
        return result;
    }

    public static String hexToBinary(String input) {
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            String binary = Integer.toBinaryString(Integer.parseInt(input.charAt(i) + "", 16));
            while (binary.length() < 4)
                binary = "0" + binary;
            output += binary;
        }
        return output;
    }

    public static String binaryToHex(String input) {
        String output = "";
        for (int i = 0; i < input.length(); i += 4) {
            String hex = Integer.toHexString(Integer.parseInt(input.substring(i, i + 4), 2));
            output += hex;
        }
        return output;
    }

    public String CBCencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % blockSize != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of " + blockSize);
        String cipherText = "";
        String IV = randomString(blockSize);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += blockSize) {
            String block = plainText.substring(i, i + blockSize);
            try {
                String encryptedBlock = cipher.encrypt(XOR(block, previousCipherText), key);
                cipherText += encryptedBlock;
                previousCipherText = encryptedBlock;
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return IV + cipherText;
    }

    public String CBCdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % blockSize != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of " + blockSize);
        String plainText = "";
        String IV = cipherText.substring(0, blockSize);
        String previousCipherText = IV;
        for (int i = blockSize; i < cipherText.length(); i += blockSize) {
            String block = cipherText.substring(i, i + blockSize);
            try {
                String decryptedBlock = XOR(cipher.decrypt(block, key), previousCipherText);
                plainText += decryptedBlock;
                previousCipherText = block;
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return plainText;
    }

    public String CFBencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % blockSize != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of " + blockSize);
        String cipherText = "";
        String IV = randomString(blockSize);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += blockSize) {
            String block = plainText.substring(i, i + blockSize);
            try {
                String encryptedBlock = cipher.encrypt(previousCipherText, key);
                String XORedBlock = XOR(block, encryptedBlock);
                cipherText += XORedBlock;
                previousCipherText = XORedBlock;
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return IV + cipherText;
    }

    public String CFBdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % blockSize != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of " + blockSize);
        String plainText = "";
        String IV = cipherText.substring(0, blockSize);
        String previousCipherText = IV;
        for (int i = blockSize; i < cipherText.length(); i += blockSize) {
            String block = cipherText.substring(i, i + blockSize);
            try {
                String encryptedBlock = cipher.encrypt(previousCipherText, key);
                String XORedBlock = XOR(block, encryptedBlock);
                plainText += XORedBlock;
                previousCipherText = block;
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return plainText;
    }

    public String OFBencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % blockSize != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of " + blockSize);
        String cipherText = "";
        String IV = randomString(blockSize);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += blockSize) {
            String block = plainText.substring(i, i + blockSize);
            try {
                String encryptedBlock = cipher.encrypt(previousCipherText, key);
                String XORedBlock = XOR(block, encryptedBlock);
                cipherText += XORedBlock;
                previousCipherText = encryptedBlock;
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return IV + cipherText;
    }

    public String OFBdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % blockSize != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of " + blockSize);
        String plainText = "";
        String IV = cipherText.substring(0, blockSize);
        String previousCipherText = IV;
        for (int i = blockSize; i < cipherText.length(); i += blockSize) {
            String block = cipherText.substring(i, i + blockSize);
            try {
                String encryptedBlock = cipher.encrypt(previousCipherText, key);
                String XORedBlock = XOR(block, encryptedBlock);
                plainText += XORedBlock;
                previousCipherText = encryptedBlock;
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return plainText;
    }

    public String CTRencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % blockSize != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of " + blockSize);
        String cipherText = "";
        String IV = randomString(blockSize);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += blockSize) {
            String block = plainText.substring(i, i + blockSize);
            try {
                String encryptedBlock = cipher.encrypt(previousCipherText, key);
                String XORedBlock = XOR(block, encryptedBlock);
                cipherText += XORedBlock;
                previousCipherText = increment(previousCipherText);
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return IV + cipherText;
    }

    public String CTRdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % blockSize != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of " + blockSize);
        String plainText = "";
        String IV = cipherText.substring(0, blockSize);
        String previousCipherText = IV;
        for (int i = blockSize; i < cipherText.length(); i += blockSize) {
            String block = cipherText.substring(i, i + blockSize);
            try {
                String encryptedBlock = cipher.encrypt(previousCipherText, key);
                String XORedBlock = XOR(block, encryptedBlock);
                plainText += XORedBlock;
                previousCipherText = increment(previousCipherText);
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return plainText;
    }
}