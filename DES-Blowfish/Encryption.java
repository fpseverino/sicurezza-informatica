public class Encryption {
    public static void main(String[] args) {
        Encryption encryption = new Encryption();
        DES des = new DES();
        for (int i = 0; i < 8; i++) {
            try {
                String plainText = encryption.randomString(2048);
                String key = encryption.randomString(64);

                String CBCcipherText = encryption.CBCencrypt(des, plainText, key);
                String CBCdecryptedText = encryption.CBCdecrypt(des, CBCcipherText, key);
                if (!plainText.equals(CBCdecryptedText)) {
                    System.out.println("DES CBC " + (i + 1) + ": " + plainText.equals(CBCdecryptedText));
                    System.out.println("Plain text: " + plainText);
                    System.out.println("Cipher text: " + CBCcipherText);
                    System.out.println("Decrypted text: " + CBCdecryptedText);
                    return;
                }

                String CFBcipherText = encryption.CFBencrypt(des, plainText, key);
                String CFBdecryptedText = encryption.CFBdecrypt(des, CFBcipherText, key);
                if (!plainText.equals(CFBdecryptedText)) {
                    System.out.println("DES CFB " + (i + 1) + ": " + plainText.equals(CFBdecryptedText));
                    System.out.println("Plain text: " + plainText);
                    System.out.println("Cipher text: " + CFBcipherText);
                    System.out.println("Decrypted text: " + CFBdecryptedText);
                    return;
                }

                String OFBcipherText = encryption.OFBencrypt(des, plainText, key);
                String OFBdecryptedText = encryption.OFBdecrypt(des, OFBcipherText, key);
                if (!plainText.equals(OFBdecryptedText)) {
                    System.out.println("DES OFB " + (i + 1) + ": " + plainText.equals(OFBdecryptedText));
                    System.out.println("Plain text: " + plainText);
                    System.out.println("Cipher text: " + OFBcipherText);
                    System.out.println("Decrypted text: " + OFBdecryptedText);
                    return;
                }

                String CTRcipherText = encryption.CTRencrypt(des, plainText, key);
                String CTRdecryptedText = encryption.CTRdecrypt(des, CTRcipherText, key);
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

    public char XOR(char bit1, char bit2) {
        if (bit1 == bit2) return '0';
        else return '1';
    }

    public String XOR(String string1, String string2) {
        String output = "";
        for (int i = 0; i < string1.length(); i++)
            output += XOR(string1.charAt(i), string2.charAt(i));
        return output;
    }

    public String leftShift(int times, String input) {
        String output = input;
        for (int i = 0; i < times; i++)
            output = output.substring(1) + output.charAt(0);
        return output;
    }

    public String randomString(int length) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length; i++)
            output.append((int)(Math.random()*2));
        return output.toString();
    }

    public String increment(String binaryString) {
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

    public String CBCencrypt(Cipher cipher, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % 64 != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of 64.");
        String cipherText = "";
        String IV = randomString(64);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += 64) {
            String block = plainText.substring(i, i + 64);
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

    public String CBCdecrypt(Cipher cipher, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % 64 != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of 64.");
        String plainText = "";
        String IV = cipherText.substring(0, 64);
        String previousCipherText = IV;
        for (int i = 64; i < cipherText.length(); i += 64) {
            String block = cipherText.substring(i, i + 64);
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

    public String CFBencrypt(Cipher cipher, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % 64 != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of 64.");
        String cipherText = "";
        String IV = randomString(64);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += 64) {
            String block = plainText.substring(i, i + 64);
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

    public String CFBdecrypt(Cipher cipher, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % 64 != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of 64.");
        String plainText = "";
        String IV = cipherText.substring(0, 64);
        String previousCipherText = IV;
        for (int i = 64; i < cipherText.length(); i += 64) {
            String block = cipherText.substring(i, i + 64);
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

    public String OFBencrypt(Cipher cipher, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % 64 != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of 64.");
        String cipherText = "";
        String IV = randomString(64);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += 64) {
            String block = plainText.substring(i, i + 64);
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

    public String OFBdecrypt(Cipher cipher, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % 64 != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of 64.");
        String plainText = "";
        String IV = cipherText.substring(0, 64);
        String previousCipherText = IV;
        for (int i = 64; i < cipherText.length(); i += 64) {
            String block = cipherText.substring(i, i + 64);
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

    public String CTRencrypt(Cipher cipher, String plainText, String key) throws IllegalArgumentException {
        if (plainText.length() % 64 != 0)
            throw new IllegalArgumentException("Plain text length must be a multiple of 64.");
        String cipherText = "";
        String IV = randomString(64);
        String previousCipherText = IV;
        for (int i = 0; i < plainText.length(); i += 64) {
            String block = plainText.substring(i, i + 64);
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

    public String CTRdecrypt(Cipher cipher, String cipherText, String key) throws IllegalArgumentException {
        if (cipherText.length() % 64 != 0)
            throw new IllegalArgumentException("Cipher text length must be a multiple of 64.");
        String plainText = "";
        String IV = cipherText.substring(0, 64);
        String previousCipherText = IV;
        for (int i = 64; i < cipherText.length(); i += 64) {
            String block = cipherText.substring(i, i + 64);
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