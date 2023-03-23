public class DES {
    public static void main(String[] args) {
        String plainText = "0000000100100011010001010110011110001001101010111100110111101111";
        String key = "0001001100110100010101110111100110011011101111001101111111110001";
        String cipherText = encrypt(plainText, key);
        String decrypted = decrypt(cipherText, key);
        System.out.println("Plain text:\t" + plainText);
        System.out.println("Cipher text:\t" + cipherText);
        System.out.println("Decypted text:\t" + decrypted);
    }

    public static char XOR(char bit1, char bit2) {
        if (bit1 == bit2) return '0';
        else return '1';
    }

    public static String leftShift(int times, String input) {
        String output = input;
        for (int i = 0; i < times; i++)
            output = output.substring(1) + output.charAt(0);
        return output;
    }

    public static String IP(String input) {
        int[] IPtable = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
        StringBuilder output = new StringBuilder();
        for (int i =0; i < 64; i++)
            output.append(input.charAt(IPtable[i]-1));
        return output.toString();
    }

    public static String FP(String input) {
        int[] FPtable = {40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
        StringBuilder output = new StringBuilder();
        for (int i =0; i < 64; i++)
            output.append(input.charAt(FPtable[i]-1));
        return output.toString();
    }

    public static String E(String input) {
        int[] Etable = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
        StringBuilder output = new StringBuilder();
        for (int i =0; i < 48; i++)
            output.append(input.charAt(Etable[i]-1));
        return output.toString();
    }

    public static String P(String input) {
        int[] Ptable = {16,	7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2,	8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6,	22,	11,	4, 25};
        StringBuilder output = new StringBuilder();
        for (int i =0; i < 32; i++)
            output.append(input.charAt(Ptable[i]-1));
        return output.toString();
    }

    public static String S(int number, String input) {
        int[][][] Sboxes = {
            {
                {14, 4, 13,	1, 2, 15, 11, 8, 3,	10,	6, 12, 5, 9, 0, 7},
                {0, 15,	7, 4, 14, 2, 13, 1, 10,	6, 12, 11, 9, 5, 3,	8},
                {4, 1, 14, 8, 13, 6, 2,	11,	15,	12,	9, 7, 3, 10, 5,	0},
                {15, 12, 8,	2, 4, 9, 1,	7, 5, 11, 3, 14, 10, 0,	6, 13}
            },
            {
                {15, 1,	8, 14, 6, 11, 3, 4,	9, 7, 2, 13, 12, 0,	5, 10},
            	{3,	13,	4, 7, 15, 2, 8,	14,	12,	0, 1, 10, 6, 9,	11,	5},
                {0,	14,	7, 11, 10, 4, 13, 1, 5,	8, 12, 6, 9, 3,	2, 15},
                {13, 8,	10,	1, 3, 15, 4, 2,	11,	6, 7, 12, 0, 5,	14,	9}
            },
            {
                {10, 0,	9, 14, 6, 3, 15, 5,	1, 13, 12, 7, 11, 4, 2,	8},
            	{13, 7,	0, 9, 3, 4,	6, 10, 2, 8, 5,	14,	12,	11,	15,	1},
            	{13, 6,	4, 9, 8, 15, 3,	0, 11, 1, 2, 12, 5,	10, 14,	7},
            	{1,	10,	13,	0, 6, 9, 8,	7, 4, 15, 14, 3, 11, 5,	2, 12}
            },
            {
                {7,	13,	14,	3, 0, 6, 9,	10,	1, 2, 8, 5,	11,	12,	4, 15},
	            {13, 8,	11,	5, 6, 15, 0, 3,	4, 7, 2, 12, 1,	10,	14,	9},
	            {10, 6,	9, 0, 12, 11, 7, 13, 15, 1,	3, 14, 5, 2, 8,	4},
	            {3,	15,	0, 6, 10, 1, 13, 8,	9, 4, 5, 11, 12, 7,	2, 14}
            },
            {
                {2, 12, 4, 1, 7, 10, 11, 6,	8, 5, 3, 15, 13, 0, 14, 9},
            	{14, 11, 2, 12, 4, 7, 13, 1, 5,	0, 15, 10, 3, 9, 8,	6},
            	{4, 2, 1, 11, 10, 13, 7, 8,	15,	9, 12, 5, 6, 3,	0, 14},
            	{11, 8, 12, 7, 1, 14, 2, 13, 6,	15,	0, 9, 10, 4, 5,	3}
            },
            {
                {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
	            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
	            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            	{11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
	            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
	            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
	            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
	            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
	            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
	            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}

            }
        };
        
        int row = Integer.parseInt("" + input.charAt(0) + input.charAt(5), 2);
        int column = Integer.parseInt(input.substring(1, 5), 2);

        String binaryString = Integer.toBinaryString(Sboxes[number - 1][row][column]);
        return String.format("%4s", binaryString).replace(' ', '0');
    }

    public static String dropParityBits(String input) {
        String output = "";
        for (int i = 0; i < 64; i++) {
            if ((i + 1) % 8 != 0)
                output += input.charAt(i);
        }
        return output;
    }

    public static String dropEBits(String input) {
        String output = "";
        for (int i = 0; i < 56; i++)
            if (input.charAt(i) != 'E')
                output += input.charAt(i);
        return output;
    }

    public static String PC1(String input) {
        int[] PC1table = {
            57,	49,	41,	33,	25,	17,	9,	1,
	        58,	50,	42,	34,	26,	18,	10,	2,
	        59,	51,	43,	35,	27,	19,	11,	3,
	        60,	52,	44,	36,	63,	55,	47,	39,
	        31,	23,	15,	7,	62,	54,	46,	38,
	        30,	22,	14,	6,	61,	53,	45,	37,
	        29,	21,	13,	5,	28,	20,	12,	4
        };
        String output = "";
        int PC1index = 0;
        for (int i = 0; i < 64; i++) {
            if ((i + 1) % 8 != 0) {
                output += input.charAt(PC1table[PC1index] - 1);
                PC1index++;
            } else output += 'E';
        }
        return dropParityBits(output.toString());
    }

    public static String PC2(String input) {
        int[] PC2table = {
            14,	17,	11,	24,	1,	5,
            3,	28,	15,	6,	21,	10,
            23,	19,	12,	4,	26,	8,
            16,	7,	27,	20,	13,	2,
            41,	52,	31,	37,	47,	55,
            30,	40,	51,	45,	33,	48,
            44,	49,	39,	56,	34,	53,
            46,	42,	50,	36,	29,	32
        };
        String output = "";
        int PC2index = 0;
        for (int i = 0; i < 56; i++) {
            if ((i + 1) == 9 || (i + 1) == 18 || (i + 1) == 22 || (i + 1) == 25 || (i + 1) == 35 || (i + 1) == 38 || (i + 1) == 43 || (i + 1) == 54)
                output += 'E';
            else {
                output += input.charAt(PC2table[PC2index] - 1);
                PC2index++;
            }
        }
        return dropEBits(output.toString());
    }

    public static String[] generateSubkeys(String key) {
        int[] numberOfRotations = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
        String[] subkeys = new String[16];
        String PC1output = PC1(key);
        String A = PC1output.substring(0, 28);
        String B = PC1output.substring(28, 56);
        for (int i = 0; i < 16; i++) {
            A = leftShift(numberOfRotations[i], A);
            B = leftShift(numberOfRotations[i], B);
            subkeys[i] = PC2(A + B);
        }
        return subkeys;
    }

    public static String F(String halfBlock, String subkey) {
        String expanded = E(halfBlock);
        String xored = "";
        String output = "";
        for (int i = 0; i < 48; i++) {
            xored += XOR(expanded.charAt(i), subkey.charAt(i));
        }
        String[] piece = new String[8];
        int index = 0;
        for (int i = 0; i < 8; i++) {
            piece[i] = xored.substring(index, index + 6);
            index += 6;
        }
        for (int i = 0; i < 8; i++)
            output += S(i + 1, piece[i]);
        return P(output);
    }

    public static String encrypt(String plainText, String key) {
        String[] subkeys = generateSubkeys(key);
        String IPoutput = IP(plainText);
        String L = IPoutput.substring(0, 32);
        String R = IPoutput.substring(32, 64);
        for (int i = 0; i < 16; i++) {
            String temp = L;
            L = R;
            String Foutput = F(R, subkeys[i]);
            for (int j = 0; j < 32; j++)
                R += XOR(temp.charAt(j), Foutput.charAt(j));
        }
        return FP(R + L);
    }

    public static String decrypt(String cipherText, String key) {
        String[] subkeys = generateSubkeys(key);
        String IPoutput = IP(cipherText);
        String L = IPoutput.substring(0, 32);
        String R = IPoutput.substring(32, 64);
        for (int i = 15; i >= 0; i--) {
            String temp = L;
            L = R;
            String Foutput = F(R, subkeys[i]);
            for (int j = 0; j < 32; j++)
                R += XOR(temp.charAt(j), Foutput.charAt(j));
        }
        return FP(R + L);
    }
}