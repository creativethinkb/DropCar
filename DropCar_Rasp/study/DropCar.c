#include <wiringPi.h>
#include <bcm2835.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <pthread.h>

pthread_t threads;
void *thCamera(void *arg);

#define PORT 9000

#define SERVO_PWM 18 //no.12
#define PWM_CLOCK BCM2835_PWM_CLOCK_DIVIDER_16
#define SERVO_RANGE 22500
#define PWM_CHANNEL0 0

#define PWM_CHANNEL1 1
#define MOTOR_RANGE 1024
#define MOTOR_PWM 19 //no.11
#define MOTOR_FW 4 // wiringPi no.16
#define MOTOR_BW 5 // wiringPi no.18
#define FW 0
#define BW 1
#define LEFT 0
#define RIGHT 1

void dcMove(int dir, int speed)
{
	bcm2835_pwm_set_data(PWM_CHANNEL1, speed);
	bcm2835_delay(50);

	switch(dir)
	{
		case FW : digitalWrite(MOTOR_FW, 1);
			  digitalWrite(MOTOR_BW, 0);
			  printf("FW Go\n");
			 break;
		case BW : digitalWrite(MOTOR_FW, 0);
			  digitalWrite(MOTOR_BW, 1);
			  printf("BW Go\n");
			 break;
	}
}

void dcStop()
{
	bcm2835_pwm_set_data(PWM_CHANNEL1,0);
	digitalWrite(MOTOR_PWM, 0);
	digitalWrite(MOTOR_FW, 0);
	digitalWrite(MOTOR_BW, 0);
	printf("Rear Motor Stop\n");
}

void InitGPIO()
{
	pinMode(MOTOR_PWM, OUTPUT);
	pinMode(SERVO_PWM, OUTPUT);
	pinMode(MOTOR_FW, OUTPUT);
	pinMode(MOTOR_BW, OUTPUT);
}

static int duty = 1700;

void ServoMove(int dir)
{
	if(duty < 700) duty = 700;
	if(duty > 2500) duty = 2500;

	if(dir == LEFT)
	{
		duty  = 1300;
		bcm2835_pwm_set_data(PWM_CHANNEL0, duty);
		printf("Left go\n");
	}
	else if(dir == RIGHT)
	{
		duty = 2000;
		bcm2835_pwm_set_data(PWM_CHANNEL0, duty);
		printf("Right go\n");
	}
}

void ServoStop()
{
	bcm2835_pwm_set_data(PWM_CHANNEL0, 1700);
	duty = 1700;
	printf("Servo Stop\n");
}

#define PELCO_STX 0xFF

void CommandProc(unsigned char cmd1, unsigned char cmd2, unsigned char data1, unsigned char data2)
{
	if((cmd2 & 0x01) == 0x00)
	{
		if(cmd1 == 0x88)
		{
			if(data1 == 0x01)
			{
				int rc;
				int t = 10;
				rc = pthread_create(&threads,NULL,
 				&thCamera,(void *)t);
				printf("Camera On\n");
				return;
			}
		}
		else if(cmd1 == 0x08)
		{
			if(data1 == 0x01)
			{
				//Camera Off
				//int pid;
				//char cmdd[19];
				//pid = system("sudo pidof gst-launch-0.10");
				//int ppid;
				//ppid = system("sudo pidof DropCar");
				//printf("id:%d",ppid);
				//sprintf(cmdd,"sudo kill -3 %d",pid);
				system("sudo killall -9 gst-launch-0.10");
				printf("Camera Off\n");
				return;
			}
		}

		if(cmd2 & 0x02) //Right
		{
			ServoMove(RIGHT);
		}
		else if(cmd2 & 0x04)
		{
			ServoMove(LEFT);
		}

		if(!(cmd2 & 0x06)) ServoStop();

		if(cmd2 & 0x08) // FW
		{
			switch(data2)
			{
				case 0x01 : dcMove(FW,150); break;
				case 0x02 : dcMove(FW,200); break;
				case 0x03 : dcMove(FW,250); break;
				case 0x04 : dcMove(FW,300); break;
				case 0x05 : dcMove(FW,350); break;
				case 0x06 : dcMove(FW,400); break;
				case 0x07 : dcMove(FW,450); break;
				case 0x08 : dcMove(FW,500); break;
				case 0x09 : dcMove(FW,800); break;
			}
		}
		else if(cmd2 & 0x10)
		{
			switch(data2)
			{
				case 0x01 : dcMove(BW,150); break;
				case 0x02 : dcMove(BW,200); break;
				case 0x03 : dcMove(BW,250); break;
				case 0x04 : dcMove(BW,300); break;
				case 0x05 : dcMove(BW,350); break;
				case 0x06 : dcMove(BW,400); break;
				case 0x07 : dcMove(BW,450); break;
				case 0x08 : dcMove(BW,500); break;
				case 0x09 : dcMove(BW,800); break;
			}
		}

		if(!(cmd2 & 0x18)) dcStop();
	}
}

char CheckSum(char *cmd, int len)
{
	char sum = 0;

	for(int i = 0; i < len; i++)
	{
		sum += cmd[i];
	}
	return sum;
}

void ParseCommand(char *cmd)
{
	if(cmd[0] == PELCO_STX)
	{
		if(cmd[6] == CheckSum(&cmd[1],5))
		{
			if(cmd[1] == 0x01)
			{
				CommandProc(cmd[2],cmd[3],cmd[4],cmd[5]);
			}
		}
		else
		{
			printf("CheckSum Error");
		}
	}

	//printf("%s",cmd);

	/*if(cmd[0] == '@')
	{
		if(cmd[1] == 'F')
		{
			if(cmd[2] == 'M')
			{
				if(cmd[3] == 'L')
				{
					printf("Move Left\n");
					ServoMove(LEFT);
				}
				else if(cmd[3] == 'R')
				{
					printf("Move Right\n");
					ServoMove(RIGHT);
				}
				else if(cmd[3] == 'S')
				{
					printf("Front Motor Stop\n");
					ServoStop();
				}
			}
		}
		else if(cmd[1] == 'R')
		{
			if(cmd[2] == 'M')
			{
				if(cmd[3] == 'F')
				{
					switch(cmd[4])
					{
						case '1' : dcMove(FW,150); break;
						case '2' : dcMove(FW,200); break;
						case '3' : dcMove(FW,250); break;
						case '4' : dcMove(FW,300); break;
						case '5' : dcMove(FW,350); break;
						case '6' : dcMove(FW,400); break;
						case '7' : dcMove(FW,450); break;
						case '8' : dcMove(FW,500); break;
						case '9' : dcMove(FW,800); break;
					}
					printf("Move Forward\n");
				}
				else if(cmd[3] == 'B')
				{
					switch(cmd[4])
					{
						case '1' : dcMove(BW,150); break;
						case '2' : dcMove(BW,200); break;
						case '3' : dcMove(BW,250); break;
						case '4' : dcMove(BW,300); break;
						case '5' : dcMove(BW,350); break;
						case '6' : dcMove(BW,400); break;
						case '7' : dcMove(BW,450); break;
						case '8' : dcMove(BW,500); break;
						case '9' : dcMove(BW,800); break;
					}
					printf("Move Backward\n");
				}
				else if(cmd[3] == 'S')
				{
					dcStop();
					printf("Rear Motor Stop\n");
				}
			}
		}
	}*/
}

void InitPWM()
{
	bcm2835_gpio_fsel(SERVO_PWM, BCM2835_GPIO_FSEL_ALT5);
	bcm2835_pwm_set_clock(PWM_CLOCK);
	bcm2835_pwm_set_mode(PWM_CHANNEL0,1,1);
	bcm2835_pwm_set_range(PWM_CHANNEL0,SERVO_RANGE);

	bcm2835_gpio_fsel(MOTOR_PWM, BCM2835_GPIO_FSEL_ALT5);
	bcm2835_pwm_set_clock(PWM_CLOCK);
	bcm2835_pwm_set_mode(PWM_CHANNEL1,1,1);
	bcm2835_pwm_set_range(PWM_CHANNEL1,MOTOR_RANGE);
}

int main()
{
	if(wiringPiSetup()==-1) return 1;
	InitGPIO();

	if(!bcm2835_init()) return 1;
	InitPWM();

	int c_socket, s_socket;
	struct sockaddr_in s_addr, c_addr;
	int len,n,i;
	char rcvbuffer[BUFSIZ];

	s_socket = socket(PF_INET, SOCK_STREAM, 0);

	memset(&s_addr, 0, sizeof(s_addr));

	s_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	s_addr.sin_family = AF_INET;
	s_addr.sin_port = htons(PORT);

	if(bind(s_socket, (struct sockaddr *) &s_addr, sizeof(s_addr)) == -1)
	{
		printf("Can not bind\n");
		return -1;
	}

	if(listen(s_socket, 5) == -1)
	{
		printf("listen Failed\n");
	}

	int rc;
	int t = 10;
	rc = pthread_create(&threads,NULL,&thCamera,(void *)t);

	while(1)
	{
		len = sizeof(c_addr);
		c_socket = accept(s_socket, (struct sockaddr *) &c_addr, &len);

		while((n = read(c_socket, rcvbuffer, sizeof(rcvbuffer))) != 0)
		{
			ParseCommand(rcvbuffer);
		}
		close(c_socket);
	}
	close(s_socket);
	bcm2835_close();

	return 0;
}


void *thCamera(void *arg)
{
	system("sudo sh /study/examples/gstreamer.sh &");
}
