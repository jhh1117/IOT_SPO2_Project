import RPi.GPIO as gpio
import time
import dht11
import datetime

trig_pin = 13 #사용하는 각종 핀들의 번호를 정의
echo_pin = 19

led_red = 22
led_yellow =27
led_green = 17

gpio.setwarnings(False)
gpio.setmode(gpio.BCM)
gpio.setup(trig_pin, gpio.OUT) #각 핀의 입출력 설정
gpio.setup(led_red, gpio.OUT)
gpio.setup(led_yellow, gpio.OUT)
gpio.setup(led_green, gpio.OUT)
gpio.setup(echo_pin, gpio.IN)

instance = dht11.DHT11(pin = 5) #dht11를 5번핀으로 활성화
last_temp = 25 #가장 최근의 온도를 저장하는 변수
temp_cond = True #온도에 따른 LED 점등 여부를 판정하는 변수

try:
	while True:
		gpio.output(trig_pin, False) #초음파 센서 출력을 위한 부분
		time.sleep(0.5)
		
		gpio.output(trig_pin, True)
		#time.sleep(0.00001)
		gpio.output(trig_pin, False)

		while gpio.input(echo_pin) == 0: #에코가 들어올때까지 start를 갱신
			pulse_start = time.time()

		while gpio.input(echo_pin) == 1: #에코가 그만 들어올때까지 end를 갱신
			pulse_end = time.time()

		pulse_duration = pulse_end - pulse_start #start와 end의 차이를 구하고 공식을 통해 거리를 계산
		
		distance = pulse_duration * 17000
		distance = round(distance, 2)
		
		print("Distance %f cm"  %distance) #거리 출력


                result = instance.read() #dht11로 부터 데이터를 읽어들임
                if result.is_valid(): #유효한 값이면 시간 및 온도를 출력하고 최근 온도값을 갱신
                        print("Last valid input: " + str(datetime.datetime.now()))
                        print("Temperature: %d C" % result.temperature)
              		last_temp = result.temperature
          
			if result.temperature < 28: #온도가 28도 이내이면 LED를 출력하는 조건 활성화
				temp_cond = True
			else:
				temp_cond = False
		else:
			print("Result was not valid last_temperature: " + str(last_temp)) 
			#유효치 않으면 가장 최근의 값으로 LED 출력 조건 판단
			if last_temp < 28:
				temp_cond = True
			else:
				temp_cond = False
			
		
		if temp_cond == True: #LED 출력이 가능한 상태이면
			if distance < 30: #거리에 따라 해당하는 LED를 출력하고 화면에 출력한다.
				gpio.output(led_red, True)
				gpio.output(led_yellow, False)
				gpio.output(led_green, False)
				print("Red LED is ON")
			elif distance < 100:
				gpio.output(led_yellow, True)
				gpio.output(led_red, False)
				gpio.output(led_green, False)
				print("Yellow LED is ON")
			else:
				gpio.output(led_green, True)
				gpio.output(led_red, False)
				gpio.output(led_yellow, False)
				print("Green LED is ON")
		else: #LED출력이 불가능한 상태이면 LED를 모두 끄고 LED가 꺼져있음을 화면에 출력
			gpio.output(led_green, False)
			gpio.output(led_red, False)
			gpio.output(led_yellow, False)
			print("LED is OFF")


except KeyboardInterrupt as e:		
	gpio.cleanup()
