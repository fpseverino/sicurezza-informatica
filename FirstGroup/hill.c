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

int getMatrixSize(char * key);
int ** getKeyMatrix(char * key);
int ** getVector(char * text, int keySize);
char * getText(int ** vector, int keySize);
int ** invertKeyMatrix(int ** keyMatrix, int keySize);
char * hill(char * text, char * key);
char * readTextFile(char * fileName);
void writeTextFile(char * text, char * fileName);

int main(int argc, const char * argv[]) {
    if (argc != 5) {
        printf("Usage: %s <e|d> <input file> <output file> <key> \n", argv[0]);
        exit(EXIT_FAILURE);
    }
    char * key = malloc(sizeof(char) * (strlen(argv[4]) + 1));
    int i = 0;
    for (int j = 0; j < strlen(argv[4]); j++) {
        if (isalpha(argv[4][j])) {
            key[i] = toupper(argv[4][j]);
            i++;
        }
    }
    key[i] = '\0';
    // TODO
    free(key);
    return 0;
}

int getMatrixSize(char * key) {
    int size = 0;
    while (size * size < strlen(key))
        size++;
    if (size * size != strlen(key)) {
        printf("ERROR: Key lenght is not a perfect square");
        exit(EXIT_FAILURE);
    }
    return size;
}

int ** getKeyMatrix(char * key) {
    int size = getMatrixSize(key);
    int ** matrix = malloc(sizeof(int *) * size);
    for (int i = 0; i < size; i++)
        matrix[i] = malloc(sizeof(int) * size);
    int k = 0;
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            matrix[i][j] = key[k] - 'A';
            k++;
        }
    }
    return matrix;
}

int ** getVector(char * text, int keySize) {
    int ** vector = malloc(sizeof(int *) * (strlen(text) / keySize));
    for (int i = 0; i < strlen(text) / keySize; i++)
        vector[i] = malloc(sizeof(int) * keySize);
    int k = 0;
    for (int i = 0; i < strlen(text) / keySize; i++) {
        for (int j = 0; j < keySize; j++) {
            vector[i][j] = text[k] - 'A';
            k++;
        }
    }
    return vector;
}

char * getText(int ** vector, int keySize) {
    char * text = malloc(sizeof(char) * (keySize * (strlen(text) / keySize) + 1));
    int k = 0;
    for (int i = 0; i < strlen(text) / keySize; i++) {
        for (int j = 0; j < keySize; j++) {
            text[k] = vector[i][j] + 'A';
            k++;
        }
    }
    text[k] = '\0';
    return text;
}

int ** invertKeyMatrix(int ** keyMatrix, int keySize) {
    // TODO
    return NULL;
}

char * hill(char * text, char * key) {
    int keySize = getMatrixSize(key);
    int ** keyMatrix = getKeyMatrix(key);
    int ** vector = getVector(text, keySize);
    int ** result = malloc(sizeof(int *) * (strlen(text) / keySize));
    for (int i = 0; i < strlen(text) / keySize; i++)
        result[i] = malloc(sizeof(int) * keySize);
    for (int i = 0; i < strlen(text) / keySize; i++) {
        for (int j = 0; j < keySize; j++) {
            result[i][j] = 0;
            for (int k = 0; k < keySize; k++)
                result[i][j] += keyMatrix[i][k] * vector[k][j];
            result[i][j] %= 26;
        }
    }
    char * resultText = getText(result, keySize);
    for (int i = 0; i < strlen(text) / keySize; i++) {
        free(result[i]);
        free(vector[i]);
    }
    free(result);
    free(vector);
    for (int i = 0; i < keySize; i++)
        free(keyMatrix[i]);
    free(keyMatrix);
    return resultText;
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