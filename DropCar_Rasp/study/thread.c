#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void *thread_test(void *arg);
pthread_t threads;


int t_flag = 1;
void delay(int n)
{
volatile int i,j;

for(i=0;i<n;i++)
{
for(j=0;j<600;j++);
}
}
void  main(void)
{
int t = 10;
int rc;
int status;



while(1)
{
	printf("h");
if(t_flag == 1)
{
rc = pthread_create(&threads,NULL,&thread_test,(void *)t);
//printf("after thread create:test[%s]\n",test);

rc = pthread_join(threads,(void**)&status);
}
}
//printf("get thread join:test[%s] %d \n",test,status);
}



void *thread_test(void *arg)
{
if(t_flag == 1)
{
printf("hellllllllo\n");
system("sudo sh /study/examples/gstreamer.sh");
t_flag = 2;
}
pthread_exit((void *)12);
}
