#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdbool.h>
#include<stdlib.h>
#include<stddef.h>
#include<string.h>

int c = 1;

int somma(int a,int b){
int result;
result = a + b + c;
if(result > 100){

printf("%s\n","Valore grande");

} else {

printf("%s\n","Valore piccolo");

}
return result;
}

int main(int argc, char *argv[]){
int a = 10;

int b = 22;

int risultato;

risultato = somma(a, b);
printf("%d\n",risultato);

}