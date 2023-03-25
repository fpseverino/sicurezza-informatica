public class Encryption {
    public static void main(String[] args) {
        DES des = new DES();
        for (int i = 0; i < 8; i++) {
            try {
                String plainText = des.randomString(2048);
                String key = des.randomString(63);

                String CBCcipherText = des.CBCencrypt(plainText, key);
                String CBCdecryptedText = des.CBCdecrypt(CBCcipherText, key);
                System.out.println("CBC " + (i + 1) + ": " + plainText.equals(CBCdecryptedText));

                String CFBcipherText = des.CFBencrypt(plainText, key);
                String CFBdecryptedText = des.CFBdecrypt(CFBcipherText, key);
                System.out.println("CFB " + (i + 1) + ": " + plainText.equals(CFBdecryptedText));

                String OFBcipherText = des.OFBencrypt(plainText, key);
                String OFBdecryptedText = des.OFBdecrypt(OFBcipherText, key);
                System.out.println("OFB " + (i + 1) + ": " + plainText.equals(OFBdecryptedText));

                String CTRcipherText = des.CTRencrypt(plainText, key);
                String CTRdecryptedText = des.CTRdecrypt(CTRcipherText, key);
                System.out.println("CTR " + (i + 1) + ": " + plainText.equals(CTRdecryptedText));

                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }
}
