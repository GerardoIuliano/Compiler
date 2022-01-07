#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdbool.h>
#include<stdlib.h>
#include<stddef.h>
#include<string.h>

int c = 1;

double sommac(int a,double b,char* *size){
double result;
result = a + b + c;
if(result > 100){
char* valore = "grande";

*size = valore;

} else {
char* valore = "piccola";

*size = valore;

}
return result;
}

void stampa(char* messaggio){
int i = 1;
while(i <= 4){
int incremento = 1;

printf("%s\n","");
i = i + incremento;

}
printf("%s\n",messaggio);
}

int main(int argc, char *argv[]){
int a = 1;

double b = 2.2;

char* taglia;

char* ans = "no";

double risultato = sommac(a, b, &taglia);

stampa(strcat(strcat(strcat(strcat(strcat(strcat(strcat("la somma di ", a), " e "), b), " incrementata di "), c), " è "), taglia));
stampa(strcat("ed è pari a ", risultato));
printf("%s\t","vuoi continuare? (si/no)");
scanf("%s", &ans);
while(strcmp(&ans,"si") == 0){

printf("inserisci un intero:");
scanf("%d", &a);
printf("inserisci un reale:");
scanf("%lf", &b);
risultato = sommac(a, b, &taglia);
stampa(strcat(strcat(strcat(strcat(strcat(strcat(strcat("la somma di ", a), " e "), b), " incrementata di "), c), " è "), taglia));
stampa(strcat(" ed è pari a ", risultato));
printf("vuoi continuare? (si/no):\t");
scanf("%s", &ans);

}
while(strcmp(&ans,"si") != 0){

printf("vuoi continuare? (si/no):\t");
scanf("%s", &ans);

}
printf("%s\n","");
printf("%s","ciao");

}