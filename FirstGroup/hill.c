//
//  hill.c
//  sicurezza-informatica
//
//  Created by Francesco Paolo Severino on 12/04/23.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <math.h>

int getKeyOrder(char * key);
int ** getKeyMatrix(char * key);
int ** getTextVector(char * text, int keySize);
int ** invertKeyMatrix(int ** keyMatrix, int keySize);
char * encrypt(char * plainText, char * key);
char * decrypt(char * cipherText, char * key);
char * readTextFile(char * fileName);
void writeTextFile(char * text, char * fileName);

int main(int argc, const char * argv[]) {
    if (argc != 5) {
        printf("Usage: %s <e|d> <key> <input file> <output file> \n", argv[0]);
        exit(EXIT_FAILURE);
    }
    char * key = malloc(sizeof(char) * (strlen(argv[2]) + 1));
    int i = 0;
    for (int j = 0; j < strlen(argv[2]); j++) {
        if (isalpha(argv[2][j])) {
            key[i] = toupper(argv[2][j]);
            i++;
        }
    }
    key[i] = '\0';
    if (strcmp(argv[1], "e") == 0) {
        char * plainText = readTextFile((char *)argv[3]);
        char * cipherText = encrypt(plainText, key);
        writeTextFile(cipherText, (char *)argv[4]);
        free(plainText);
        free(cipherText);
    } else if (strcmp(argv[1], "d") == 0) {
        char * cipherText = readTextFile((char *)argv[3]);
        char * plainText = decrypt(cipherText, key);
        writeTextFile(plainText, (char *)argv[4]);
        free(cipherText);
        free(plainText);
    } else {
        printf("Usage: %s <e|d> <key> <input file> <output file> \n", argv[0]);
        exit(EXIT_FAILURE);
    }
    free(key);
    return 0;
}

int getKeyOrder(char * key) {
    if (strlen(key) == 4 || strlen(key) == 9)
        return (int)sqrt(strlen(key));
    else {
        printf("ERROR: Key length must be 4 or 9");
        exit(EXIT_FAILURE);
    }
}

int ** getKeyMatrix(char * key) {
    int keyOrder = getKeyOrder(key);
    int det = 0;
    if (keyOrder == 2) {
        det = (key[0] - 'A') * (key[3] - 'A') - (key[1] - 'A') * (key[2] - 'A');
    } else if (keyOrder == 3) {
        det = (key[0] - 'A') * ((key[4] - 'A') * (key[8] - 'A') - (key[5] - 'A') * (key[7] - 'A'))
        - (key[1] - 'A') * ((key[3] - 'A') * (key[8] - 'A') - (key[5] - 'A') * (key[6] - 'A'))
        + (key[2] - 'A') * ((key[3] - 'A') * (key[7] - 'A') - (key[4] - 'A') * (key[6] - 'A'));
    }
    if (det == 0) {
        printf("ERROR: Key matrix is not invertible");
        exit(EXIT_FAILURE);
    }
    int ** keyMatrix = malloc(sizeof(int *) * keyOrder);
    for (int i = 0; i < keyOrder; i++) {
        keyMatrix[i] = malloc(sizeof(int) * keyOrder);
        for (int j = 0; j < keyOrder; j++)
            keyMatrix[i][j] = key[i * keyOrder + j] - 'A';
    }
    return keyMatrix;
}

int ** getTextVector(char * text, int keyOrder) {
    int textLength = strlen(text);
    int vectorLength = textLength / keyOrder;
    if (textLength % keyOrder != 0)
        vectorLength++;
    int ** textVector = malloc(sizeof(int *) * vectorLength);
    for (int i = 0; i < vectorLength; i++) {
        textVector[i] = malloc(sizeof(int) * keyOrder);
        for (int j = 0; j < keyOrder; j++) {
            if (i * keyOrder + j < textLength)
                textVector[i][j] = text[i * keyOrder + j] - 'A';
            else
                textVector[i][j] = 0;
        }
    }
    return textVector;
}

int ** invertKeyMatrix(int ** keyMatrix, int keySize) {
    return NULL;
}

char * encrypt(char * plainText, char * key) {
    return NULL;
}

char * decrypt(char * cipherText, char * key) {
    return NULL;
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
        if (isalpha(c)) {
            text[i] = toupper(c);
            i++;
        }
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