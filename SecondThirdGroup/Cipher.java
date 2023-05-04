//
//  Cipher.java
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 27/03/23.
//

public interface Cipher {
    String encrypt(String plainText, String key);
    String decrypt(String cipherText, String key);
}