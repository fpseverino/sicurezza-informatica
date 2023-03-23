#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void printTable();

char * encrypt(char * plainText, char * key);
char * decrypt(char * cipherText, char * key);

int main() {
    printTable();

    char plainText[] = "FRANCESCOPAOLOSEVERINO";
    char key[64];
    printf("Inserire chiave: ");
    scanf("%s", key);
    char * cipherText = encrypt(plainText, key);
    printf("Cipher text: %s\n", cipherText);
    printf("Testo decriptato: %s\n", decrypt(cipherText, key));

    return 0;
}

void printTable() {
    for (int i = 0; i < 26; i++) {
        for (int j = 0; j < 26; j++)
            printf("%c ", 'A' + ((i + j) % 26));
        puts("");
    } 
}

char * encrypt(char * plainText, char * key) {
    int plainTextLength = strlen(plainText);
    int keyLength = strlen(key);
    char * cipherText = malloc(sizeof(char) * (plainTextLength + 1));
    for (int i = 0, j = 0; i < plainTextLength; i++, j++) {
        if (j == keyLength)
            j = 0;
        cipherText[i] = ((plainText[i] + key[j]) % 26) + 'A';
    }
    cipherText[plainTextLength] = '\0';
    return cipherText;
}

char * decrypt(char * cipherText, char * key) {
    int cipherTextLength = strlen(cipherText);
    int keyLength = strlen(key);
    char * plainText = malloc(sizeof(char) * (cipherTextLength + 1));
    for (int i = 0, j = 0; i < cipherTextLength; i++, j++) {
        if (j == keyLength)
            j = 0;
        plainText[i] = (((cipherText[i] - key[j]) + 26) % 26) + 'A';
    }
    plainText[cipherTextLength] = '\0';
    return plainText;
}