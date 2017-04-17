import spidev
import time

spi = spidev.SpiDev()
spi.open(0,0)

light_channel = 0
temp_channel = 1

def readChannel(channel):
	adc = spi.xfer2([1, (8 + channel) << 4, 0])
	adc_out = ((adc[1] & 3) << 8) + adc[2]
	return adc_out
	
def convert2volts(data, places):
	volts = (data * 3.3) / float(1023)
	volts = round(volts, places)
	return volts

def convert2temp(data, places):
	temp = ((data * 330) / float(1023)) - 50
	temp = round(temp, places)
	return temp

try:
	while True:
		light_level = readChannel(light_channel)
		light_volts = convert2volts(light_level, 2)
		
		temp_level = readChannel(temp_channel)
		temp_volts = convert2volts(temp_level, 2)
		temp = convert2temp(temp_level, 2)
		
		print("-------------------------------------")		
		print("Light: %d (%f V)" %(light_level, light_volts))
		print("Temp: %d (%f V) %f deg C" %(temp_level, temp_volts, temp))
		
		time.sleep(1)
		
except KeyboardInterrupt:
	print("Finished")
	spi.close()
	
