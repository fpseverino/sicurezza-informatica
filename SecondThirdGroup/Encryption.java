import java.io.*;
import java.util.Scanner;

public class Encryption {
    public static void main(String[] args) throws IOException {
        String keyBinary;
        try {
            keyBinary = textToBinary(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java Encryption <key>");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to encrypt or decrypt a file? (e/d)");
        String choice = scanner.nextLine();
        if (!choice.equals("e") && !choice.equals("d") && !choice.equals("E") && !choice.equals("D")) {
            System.out.println("Invalid choice");
            scanner.close();
            return;
        }
        Cipher cipher;
        int blockSize;
        System.out.println("Which cipher would you like to use? (des/blowfish/aes)");
        String cipherName = scanner.nextLine();
        if (cipherName.equals("des")) {
            cipher = new DES();
            blockSize = 64;
        } else if (cipherName.equals("blowfish")) {
            cipher = new Blowfish(keyBinary);
            blockSize = 64;
        } else if (cipherName.equals("aes")) {
            cipher = new AES();
            blockSize = 128;
        } else {
            System.out.println("Invalid cipher name");
            scanner.close();
            return;
        }
        System.out.println("Which mode would you like to use? (ecb/cbc/cfb/ofb/ctr)");
        String mode = scanner.nextLine();
        if (!mode.equals("ecb") && !mode.equals("cbc") && !mode.equals("cfb") && !mode.equals("ofb") && !mode.equals("ctr") && !mode.equals("ECB") && !mode.equals("CBC") && !mode.equals("CFB") && !mode.equals("OFB") && !mode.equals("CTR")) {
            System.out.println("Invalid mode");
            scanner.close();
            return;
        }
        System.out.println("What is the name of the input file?");
        String inputFileName = scanner.nextLine();
        System.out.println("What is the name of the output file?");
        String outputFileName = scanner.nextLine();
        scanner.close();
        String fileContentsBinary = trim(textToBinary(readFile(inputFileName)), blockSize);
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

    public static String readFile(String filename) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        String str;
        while ((str = reader.readLine()) != null)
            output.append(str);
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

    public static String trim(String input, int blockSize) {
        if (input.length() % blockSize == 0)
            return input;
        else
            return input.substring(0, input.length() - (input.length() % blockSize));
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