#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdbool.h>
#include<stdlib.h>
#include<stddef.h>
#include<string.h>
#define STRING 100
char BUFFER[STRING];
char STRING_CAT[STRING];
char STRING_CAT_1[STRING];
char* concatena(char* dest, char* src){
    strcat(strcat(STRING_CAT, dest),src);
    strcpy(STRING_CAT_1,STRING_CAT);
    strcpy(STRING_CAT,"");
    return STRING_CAT_1;
}
char* convertInt(int intero){
    char * num = malloc(sizeof(char)*STRING);
    itoa(intero,BUFFER,10);
    strcpy(num,BUFFER);
    return num;
}
char* convertReal(double real){
    char * num = malloc(sizeof(char)*STRING);
    gcvt(real,10,BUFFER);
    strcpy(num,BUFFER);
    return num;
}
int prodotto(int a,int b){
int p = 0;

int i = 0;
while(i < b){

p = p + a;
i = i + 1;

}
return p;
}

int somma(int a,int b){
return a + b;
}

int potenza(int a,int b){
int res = pow(a, b);
return res;
}

int divisione(int a,int b){
int result = 0;
if(b != 0){

result = a / b;

}
return result;
}

void fibonacci(int n){
int first = 0;

int second = 1;

int sum = 0;

int i = 0;
printf("%s ","Serie di Fibonacci: ");
while(i < n){

if(i <= 1){

sum = i;

} else {

sum = first + second;
first = second;
second = sum;

}
printf("%d\t",sum);
i = i + 1;

}
}

int scegli_operazione(int op,int a,int b){
int result = 0;
if(op == 1){

result = somma(a, b);

}
if(op == 2){

result = prodotto(a, b);

}
if(op == 3){

result = divisione(a, b);

}
if(op == 4){

result = potenza(a, b);

}
if(op == 5){

fibonacci(a);

}
return result;
}

int main(int argc, char *argv[]){
int a;

int b;

int ris;

int op;

char risposta[STRING] = "si";

while(strcmp(risposta,"si") == 0){

printf("%s\t","Scegli l'operazione aritmetica: 1.somma 2.moltiplicazione 3.divisione 4.elevamento a potenza 5.successione di Fibonacci");
printf("operazione: ");
scanf("%d", &op);
if(op == 5){

printf("inserisci intero: ");
scanf("%d", &a);

} else {

printf("inserisci primo intero: ");
scanf("%d", &a);
printf("inserisci secondo intero: ");
scanf("%d", &b);

}
ris = scegli_operazione(op, a, b);
if(op != 5){

printf("%s","Risultato: ");
printf("%d\t",ris);

}
printf("vuoi continuare? (si/no):\t");
scanf("%s", risposta);

}

}