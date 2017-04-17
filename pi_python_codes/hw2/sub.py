# -*- coding: utf-8 -*-
#위의 코드는 한글주석의 사용이 가능하도록 하는 코드임
import RPi.GPIO as gpio
import paho.mqtt.client as mqtt

#각종 사용 핀들을 정의.
led_red = 22
led_yellow =27
led_green = 17
gpio.setwarnings(False)
gpio.setmode(gpio.BCM)
gpio.setup(led_red, gpio.OUT)
gpio.setup(led_yellow, gpio.OUT)
gpio.setup(led_green, gpio.OUT)

ultrasonic = 0.0 #거리값을 저장하는 변수
temp = 0.0 #온도를 저장하는 변수
temp_cond = True #LED출력을 결정하는 변수
#mqtt broker와 연결 성공시 함수 정의
def on_connect(client, userdata, rc): 
	print("Connected with result coe" + str(rc))
	client.subscribe("environment/#");
	#environment로 시작하는 토픽의 모든 메세지들을 구독

#메세지가 브로커로 부터 수신된 경우 처리하는 함수
def on_message(client, userdata, msg):
	#온도와 거리가 전역변수임을 명시하는 정의가 필요.
	global ultrasonic
	global temp
	
	#토픽이 거리, 온도 각각의 경우에 맞게 변수에 저장 및 출력
	if msg.topic=="environment/ultrasonic":
		ultrasonic = float(msg.payload)
                print("Distance is " + str(ultrasonic) + "cm")
	elif msg.topic=="environment/temperature":
		temp = float(msg.payload)
                print("Temprature is " + str(temp))
	
	#두개의 데이타가 모두 들어온경우에만(0이 아닌경우)에만 처리 시작
	if temp != 0.0 and ultrasonic != 0.0:
                if temp < 28: #기준온도인 28도 미만이면 LED 출력 가능 상태로 정의
                        temp_cond = True
                else:
                        temp_cond = False
                if temp_cond == True: #LED가 출력가능 상태이면 거리에 맞추어 색상별 LED출력 및 화면에 출력
			if ultrasonic < 30:
				gpio.output(led_red, True)
				gpio.output(led_yellow, False)
				gpio.output(led_green, False)
				print("Red LED is ON")
			elif ultrasonic < 100:
				gpio.output(led_yellow, True)
				gpio.output(led_red, False)
				gpio.output(led_green, False)
				print("Yellow LED is ON")
			else:
				gpio.output(led_green, True)
				gpio.output(led_red, False)
				gpio.output(led_yellow, False)
				print("Green LED is ON")
		else: #출력이 가능하지 않은 상태이면 LED출력 불가능상태임을 LED및 화면에 출력
			print("LED is OFF")
			gpio.output(led_green, False)
			gpio.output(led_red, False)
			gpio.output(led_yellow, False)
		

gpio.output(led_red, False)
gpio.output(led_yellow, False)
gpio.output(led_green, False)
client = mqtt.Client("sub_client") #클라이언트 생성 
client.on_connect = on_connect #접속시 콜백함수 정의
client.on_message = on_message #메세지 수신시 콜백함수 정의

client.connect("127.0.0.1",1883,60) #로컬호스트의 브로커에 접속

client.loop_forever() #루프를 계속 돌아 지속적으로 데이터를 수신하도록 함
