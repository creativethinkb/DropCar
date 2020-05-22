#include <stdio.h>
#include <stdio.h>
#include <bcm2835.h>

#define PIN 18
#define PWM_CHANNEL 0
#define PWM_CLOCK BCM2835_PWM_CLOCK_DIVIDER_16
#define RANGE 22500

int main()
{
	if(!bcm2835_init()) return 1;

	bcm2835_gpio_fsel(PIN, BCM2835_GPIO_FSEL_ALT5);
	bcm2835_pwm_set_clock(PWM_CLOCK);
	bcm2835_pwm_set_mode(PWM_CHANNEL, 1,1);
	bcm2835_pwm_set_range(PWM_CHANNEL, RANGE);

	bcm2835_pwm_set_data(PWM_CHANNEL, 1700);
	bcm2835_close();
	return 0;
}
