import RPi.GPIO as gpio
import time

trig_pin = 13
echo_pin = 19

gpio.setmode(gpio.BCM)
gpio.setup(trig_pin, gpio.OUT)
gpio.setup(echo_pin, gpio.IN)

try:
	while True:
		gpio.output(trig_pin, False)
		time.sleep(0.5)
		
		gpio.output(trig_pin, True)
		time.sleep(0.00001)
		gpio.output(trig_pin, False)

		while gpio.input(echo_pin) == 0:
			pulse_start = time.time()

		while gpio.input(echo_pin) == 1:
			pulse_end = time.time()

		pulse_duration = pulse_end - pulse_start
		distance = pulse_duration * 17000
		distance = round(distance, 2)

		print "Distance : ", distance, "cm"

except KeyboardInterrupt as e:		
	gpio.cleanup()
