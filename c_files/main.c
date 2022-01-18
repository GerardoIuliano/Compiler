#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdbool.h>
#include<stdlib.h>
#include<stddef.h>
#include<string.h>


double sommac(int a,double b,char *size){
double result;
result = a + b;
if(result > 100){
char* valore = "grande";

*size = valore;

} else {
char* valore = "piccola";

*size = valore;

}
return result;
}

int main(int argc, char *argv[]){
int a = 2;

char* s = strcat("a ", "ok");

printf("%s",s);
error write

}