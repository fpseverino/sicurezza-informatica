import java.util.Scanner;

public class Encryption {
    public static void main(String[] args) {
        Cipher cipher;
        int blockSize, keyLength;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the cipher type (des, blowfish or aes): ");
        String cipherType = scanner.nextLine();
        scanner.close();
        if (cipherType.equals("aes")) {
            cipher = new AES();
            blockSize = 128;
            keyLength = (int) (Math.random() * 3) * 64 + 128;
            System.out.println("\nKey length: " + keyLength + "\n");
        } else if (cipherType.equals("des")) {
            cipher = new DES();
            blockSize = 64;
            keyLength = 64;
        } else if (cipherType.equals("blowfish") || cipherType.equals("bf")) {
            keyLength = (int) (Math.random() * 3) * 64 + 64;
            System.out.println("\nKey length: " + keyLength + "\n");
            cipher = new Blowfish(randomString(keyLength));
            blockSize = 64;
        } else {
            System.out.println("Invalid cipher type");
            return;
        }
        boolean allTestPassed = true;
        for (int i = 0; i < 8; i++) {
            try {
                String plainText = Encryption.randomString(blockSize * 2);
                String key = Encryption.randomString(keyLength);

                String ECBcipherText = ECBencrypt(cipher, blockSize, plainText, key);
                String ECBdecryptedText = ECBdecrypt(cipher, blockSize, ECBcipherText, key);
                if (!plainText.equals(ECBdecryptedText)) {
                    System.out.println("ECB " + (i + 1) + ": " + plainText.equals(ECBdecryptedText));
                    System.out.println("\t" + binaryToHex(plainText));
                    System.out.println("\t" + binaryToHex(ECBdecryptedText));
                    System.out.println();
                    allTestPassed = false;
                }
                
                String CBCcipherText = CBCencrypt(cipher, blockSize, plainText, key);
                String CBCdecryptedText = CBCdecrypt(cipher, blockSize, CBCcipherText, key);
                if (!plainText.equals(CBCdecryptedText)) {
                    System.out.println("CBC " + (i + 1) + ": " + plainText.equals(CBCdecryptedText));
                    System.out.println("\t" + binaryToHex(plainText));
                    System.out.println("\t" + binaryToHex(CBCdecryptedText));
                    System.out.println();
                    allTestPassed = false;
                }

                String CFBcipherText = CFBencrypt(cipher, blockSize, plainText, key);
                String CFBdecryptedText = CFBdecrypt(cipher, blockSize, CFBcipherText, key);
                if (!plainText.equals(CFBdecryptedText)) {
                    System.out.println("CFB " + (i + 1) + ": " + plainText.equals(CFBdecryptedText));
                    System.out.println("\t" + binaryToHex(plainText));
                    System.out.println("\t" + binaryToHex(CFBdecryptedText));
                    System.out.println();
                    allTestPassed = false;
                }

                String OFBcipherText = OFBencrypt(cipher, blockSize, plainText, key);
                String OFBdecryptedText = OFBdecrypt(cipher, blockSize, OFBcipherText, key);
                if (!plainText.equals(OFBdecryptedText)) {
                    System.out.println("OFB " + (i + 1) + ": " + plainText.equals(OFBdecryptedText));
                    System.out.println("\t" + binaryToHex(plainText));
                    System.out.println("\t" + binaryToHex(OFBdecryptedText));
                    System.out.println();
                    allTestPassed = false;
                }

                String CTRcipherText = CTRencrypt(cipher, blockSize, plainText, key);
                String CTRdecryptedText = CTRdecrypt(cipher, blockSize, CTRcipherText, key);
                if (!plainText.equals(CTRdecryptedText)) {
                    System.out.println("CTR " + (i + 1) + ": " + plainText.equals(CTRdecryptedText));
                    System.out.println("\t" + binaryToHex(plainText));
                    System.out.println("\t" + binaryToHex(CTRdecryptedText));
                    System.out.println();
                    allTestPassed = false;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        if (allTestPassed)
            System.out.println("All tests passed.");
    }

    public static char XOR(char bit1, char bit2) {
        if (bit1 == bit2) return '0';
        else return '1';
    }

    public static String XOR(String string1, String string2) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < string1.length(); i++)
            output.append(XOR(string1.charAt(i), string2.charAt(i)));
        return output.toString();
    }

    public static String hexXOR(String hex1, String hex2) {
        return binaryToHex(XOR(hexToBinary(hex1), hexToBinary(hex2)));
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

    public static String binaryToHex(String binaryString) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i += 4)
            output.append(Integer.toHexString(Integer.parseInt(binaryString.substring(i, i + 4), 2)));
        return output.toString();
    }

    public static String hexToBinary(String hexString) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexString.length(); i++)
            output.append(String.format("%4s", Integer.toBinaryString(Integer.parseInt(hexString.charAt(i) + "", 16))).replace(' ', '0'));
        return output.toString();
    }

    public static String ECBencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % blockSize != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of " + blockSize);
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i += blockSize) {
            String block = plainText.substring(i, i + blockSize);
            try {
                cipherText += cipher.encrypt(block, key);
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return cipherText;
    }

    public static String ECBdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % blockSize != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of " + blockSize);
        String plainText = "";
        for (int i = 0; i < cipherText.length(); i += blockSize) {
            String block = cipherText.substring(i, i + blockSize);
            try {
                plainText += cipher.decrypt(block, key);
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return plainText;
    }

    public static String CBCencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
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

    public static String CBCdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
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

    public static String CFBencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
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

    public static String CFBdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
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

    public static String OFBencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
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

    public static String OFBdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
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

    public static String CTRencrypt(Cipher cipher, int blockSize, String plainText, String key) throws IllegalArgumentException {
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

    public static String CTRdecrypt(Cipher cipher, int blockSize, String cipherText, String key) throws IllegalArgumentException {
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