//
//  vigenere.c
//  Vigenere
//
//  Created by Francesco Paolo Severino on 07/03/23.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

void printTable();

char * encrypt(char * plainText, char * key);
char * decrypt(char * cipherText, char * key);
char * readTextFile(char * fileName);
void writeTextFile(char * text, char * fileName);

int main(int argc, const char * argv[]) {
    //printTable();

    char * key = malloc(sizeof(char) * (strlen(argv[1]) + 1));
    strcpy(key, argv[1]);
    for (int i = 0; i < strlen(key); i++)
        key[i] = toupper(key[i]) - 'A';
    
    char choice;
    printf("Encrypt or decrypt? (e/d): ");
    scanf("%c", &choice);

    char * fileName = malloc(sizeof(char) * 255);
    printf("File name: ");
    scanf("%s", fileName);

    if (choice == 'e') {
        char * plainText = readTextFile(fileName);
        char * cipherText = encrypt(plainText, key);
        writeTextFile(cipherText, "ciphertext.txt");
    } else if (choice == 'd') {
        char * cipherText = readTextFile(fileName);
        char * plainText = decrypt(cipherText, key);
        writeTextFile(plainText, "plaintext.txt");
    } else
        printf("Invalid choice");

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

char * readTextFile(char * fileName) {
    FILE * file = fopen(fileName, "r");
    if (file == NULL) {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }
    char * text = malloc(sizeof(char) * 1023);
    int i = 0;
    char c;
    while ((c = fgetc(file)) != EOF) {
        text[i] = c;
        i++;
    }
    text[i] = '\0';
    fclose(file);
    return text;
}

void writeTextFile(char * text, char * fileName) {
    FILE * file = fopen(fileName, "w");
    if (file == NULL) {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }
    fprintf(file, "%s", text);
    fclose(file);
}
