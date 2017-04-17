import spidev
import time

spi = spidev.SpiDev()
spi.open(0,0)

def readChannel(channel):
	adc = spi.xfer2([1, (8 + channel) << 4, 0])
	adc_out = ((adc[1] & 3) << 8) + adc[2]
	return adc_out
	
def convert2volts(data, places):
	volts = (data * 3.3) / float(1023)
	volts = round(volts, places)
	return volts
	
try:
	while True:
		light_level = readChannel(0)
		light_volts = convert2volts(light_level, 2)
		
		print("Light: %d (%f V)" %(light_level, light_volts))
		time.sleep(0.5)
		
except KeyboardInterrupt:
	print "Finished"
	spi.close()
	
