//
//  Encryption.java
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 11/04/23.
//

import java.io.*;

public class Encryption {
    public static void main(String[] args) throws IOException {
        String choice, cipherName, mode, keyBinary, inputFileName, outputFileName;
        Cipher cipher;
        int blockSize;
        try {
            choice = args[0];
            cipherName = args[1];
            mode = args[2];
            keyBinary = textToBinary(args[3]);
            inputFileName = args[4];
            outputFileName = args[5];
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                if (args[0].equals("test")) test(Integer.parseInt(args[1]));
            } catch (ArrayIndexOutOfBoundsException e2) {
                System.out.println("Usage: java Encryption <e|d|test> <cipher|iterations> <mode> <key> <input file> <output file>");
                return;
            }
            return;
        }
        if (!choice.equals("e") && !choice.equals("d") && !choice.equals("E") && !choice.equals("D")) {
            System.out.println("Usage: java Encryption <e|d|test> <cipher|iterations> <mode> <key> <input file> <output file>");
            return;
        }
        if (cipherName.equals("des")) {
            if (keyBinary.length() != 64) {
                System.out.println("ERROR: DES key must be 64 bits");
                return;
            }
            cipher = new DES();
            blockSize = 64;
        } else if (cipherName.equals("blowfish")) {
            if (keyBinary.length() < 32 || keyBinary.length() > 448) {
                System.out.println("ERROR: Blowfish key must be between 32 and 448 bits");
                return;
            }
            if (keyBinary.length() % 32 != 0) {
                System.out.println("ERROR: Blowfish key must be a multiple of 32 bits");
                return;
            }
            cipher = new Blowfish(keyBinary);
            blockSize = 64;
        } else if (cipherName.equals("aes")) {
            if (keyBinary.length() != 128 && keyBinary.length() != 192 && keyBinary.length() != 256) {
                System.out.println("ERROR: AES key must be 128, 192, or 256 bits");
                return;
            }
            cipher = new AES();
            blockSize = 128;
        } else {
            System.out.println("ERROR: Invalid cipher name");
            System.out.println("Usage: java Encryption <e|d|test> <cipher|iterations> <mode> <key> <input file> <output file>");
            return;
        }
        if (!mode.equals("ecb") && !mode.equals("cbc") && !mode.equals("cfb") && !mode.equals("ofb") && !mode.equals("ctr") && !mode.equals("ECB") && !mode.equals("CBC") && !mode.equals("CFB") && !mode.equals("OFB") && !mode.equals("CTR")) {
            System.out.println("ERROR: Invalid mode");
            System.out.println("Usage: java Encryption <e|d|test> <cipher|iterations> <mode> <key> <input file> <output file>");
            return;
        }
        String fileContentsBinary = textToBinary(pad(readFile(inputFileName), blockSize));
        String output = new String();
        try {
            if (choice.equals("e") || choice.equals("E")) {
                if (mode.equals("ecb") || mode.equals("ECB"))
                    output = ECBencrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("cbc") || mode.equals("CBC"))
                    output = CBCencrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("cfb") || mode.equals("CFB"))
                    output = CFBencrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("ofb") || mode.equals("OFB"))
                    output = OFBencrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("ctr") || mode.equals("CTR"))
                    output = CTRencrypt(cipher, blockSize, fileContentsBinary, keyBinary);
            } else if (choice.equals("d") || choice.equals("D")) {
                if (mode.equals("ecb") || mode.equals("ECB"))
                    output = ECBdecrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("cbc") || mode.equals("CBC"))
                    output = CBCdecrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("cfb") || mode.equals("CFB"))
                    output = CFBdecrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("ofb") || mode.equals("OFB"))
                    output = OFBdecrypt(cipher, blockSize, fileContentsBinary, keyBinary);
                else if (mode.equals("ctr") || mode.equals("CTR"))
                    output = CTRdecrypt(cipher, blockSize, fileContentsBinary, keyBinary);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        writeFile(outputFileName, binaryToText(output));
    }

    public static void test(int number) {
        Cipher cipher;
        int blockSize, keyLength;
        String cipherName;
        for (int j = 0; j < 3; j++) {
            if (j == 0) {
                cipher = new DES();
                blockSize = 64;
                keyLength = 64;
                cipherName = "DES";
            } else if (j == 1) {
                keyLength = (int) (Math.random() * 3) * 64 + 64;
                cipher = new Blowfish(randomString(keyLength));
                blockSize = 64;
                cipherName = "Blowfish-" + keyLength;
            } else if (j == 2) {
                cipher = new AES();
                blockSize = 128;
                keyLength = (int) (Math.random() * 3) * 64 + 128;
                cipherName = "AES-" + keyLength;
            } else return;
            boolean allTestPassed = true;
            for (int i = 0; i < number; i++) {
                try {
                    String plainText = Encryption.randomString(blockSize * 2);
                    String key = Encryption.randomString(keyLength);
                    String ECBcipherText = ECBencrypt(cipher, blockSize, plainText, key);
                    String ECBdecryptedText = ECBdecrypt(cipher, blockSize, ECBcipherText, key);
                    if (!plainText.equals(ECBdecryptedText)) {
                        System.out.println(cipherName + " ECB " + (i + 1) + ": " + plainText.equals(ECBdecryptedText));
                        allTestPassed = false;
                    }
                    String CBCcipherText = CBCencrypt(cipher, blockSize, plainText, key);
                    String CBCdecryptedText = CBCdecrypt(cipher, blockSize, CBCcipherText, key);
                    if (!plainText.equals(CBCdecryptedText)) {
                        System.out.println(cipherName + " CBC " + (i + 1) + ": " + plainText.equals(CBCdecryptedText));
                        allTestPassed = false;
                    }
                    String CFBcipherText = CFBencrypt(cipher, blockSize, plainText, key);
                    String CFBdecryptedText = CFBdecrypt(cipher, blockSize, CFBcipherText, key);
                    if (!plainText.equals(CFBdecryptedText)) {
                        System.out.println(cipherName + " CFB " + (i + 1) + ": " + plainText.equals(CFBdecryptedText));
                        allTestPassed = false;
                    }
                    String OFBcipherText = OFBencrypt(cipher, blockSize, plainText, key);
                    String OFBdecryptedText = OFBdecrypt(cipher, blockSize, OFBcipherText, key);
                    if (!plainText.equals(OFBdecryptedText)) {
                        System.out.println(cipherName + " OFB " + (i + 1) + ": " + plainText.equals(OFBdecryptedText));
                        allTestPassed = false;
                    }
                    String CTRcipherText = CTRencrypt(cipher, blockSize, plainText, key);
                    String CTRdecryptedText = CTRdecrypt(cipher, blockSize, CTRcipherText, key);
                    if (!plainText.equals(CTRdecryptedText)) {
                        System.out.println(cipherName + " CTR " + (i + 1) + ": " + plainText.equals(CTRdecryptedText));
                        allTestPassed = false;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            if (allTestPassed) System.out.println(cipherName + ": All tests passed.");
        }
    }

    public static String readFile(String filename) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        String str;
        while ((str = reader.readLine()) != null)
            output.append(str + "\n");
        reader.close();
        return output.toString();
    }

    public static void writeFile(String filename, String contents) throws IOException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename)));
        writer.print(contents);
        writer.close();
    }

    public static String textToBinary(String text) {
        StringBuilder output = new StringBuilder();
        for (char c : text.toCharArray())
            output.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        return output.toString();
    }

    public static String binaryToText(String binary) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8)
            output.append((char)Integer.parseInt(binary.substring(i, i + 8), 2));
        return output.toString();
    }

    public static String pad(String input, int blockSize) {
        StringBuilder output = new StringBuilder(input);
        while (output.length() % (blockSize / 8) != 0)
            output.append(' ');
        return output.toString();
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
            throw new IllegalArgumentException("ERROR: Plain text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Cipher text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Plain text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Cipher text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Plain text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Cipher text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Plain text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Cipher text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Plain text length must be a multiple of " + blockSize);
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
            throw new IllegalArgumentException("ERROR: Cipher text length must be a multiple of " + blockSize);
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