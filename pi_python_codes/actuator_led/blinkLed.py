import RPi.GPIO as gpio
import time

led_pin = 5

gpio.setmode(gpio.BCM)
gpio.setup(led_pin, gpio.OUT)

def blinkLED(numTimes, speed):
	for i in range(0, numTimes):
		print "Iteration " + str(i+1)
		gpio.output(led_pin, True)
		time.sleep(speed)
		gpio.output(led_pin, False)
		time.sleep(speed)
	print "Blink Finished"
	gpio.cleanup()

try:
	iterations = raw_input("Enter total number of times to blink: ")
	speed = raw_input("Enter length of each blink(seconds): ")

	blinkLED(int(iterations), float(speed))
	
except KeyboardInterrupt:
	gpio.cleanup()
	

