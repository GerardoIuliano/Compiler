#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<stdbool.h>
#include<stdlib.h>
#include<stddef.h>
#include<string.h>


int sommac(int a,int b){
int result;
result = a + b;
return result;
}

int main(int argc, char *argv[]){
int a = 2;

int b = 4;

int result = sommac(a, b);

scanf("%d", &b);
printf("%d",result);

}