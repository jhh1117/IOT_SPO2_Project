# -*- coding: utf-8 -*-
#위의 코드는 한글주석의 사용이 가능하도록하는 코드임
import paho.mqtt.client as mqtt
import RPi.GPIO as gpio
import time


trig_pin = 13 #trigger 및 echo pin 번호 설정
echo_pin = 19
gpio.setwarnings(False)
gpio.setmode(gpio.BCM)
gpio.setup(trig_pin, gpio.OUT) 
gpio.setup(echo_pin, gpio.IN)
client = mqtt.Client("pub client2") #MQTT 클라이언트 객체를 만듬

client.connect("127.0.0.1",1883,60) #로컬호스트 브로커에 접속

#무한반복
while True:
        gpio.output(trig_pin, False) #트리거에 False신호를 보내고 0.5초 대기 후 True신호 전송후 다시 False신호 전송
	time.sleep(0.5)
		
	gpio.output(trig_pin, True) 
	#time.sleep(0.00001)
	gpio.output(trig_pin, False) 

	while gpio.input(echo_pin) == 0: #에코에서 응답이 올때까지 start시간을 기록
		pulse_start = time.time()

	while gpio.input(echo_pin) == 1: #에코에서 온 응답이 사라질때까지 end시간을 기록
		pulse_end = time.time()

	pulse_duration = pulse_end - pulse_start #시작시간과 끝시간의 차를 계산
		
	distance = pulse_duration * 17000 #공식을 통해 거리를 계산
	distance = round(distance, 2)
		
	print("Distance %f cm"  %distance)
	data = distance #거리 값을 data에 저장
	client.publish("environment/ultrasonic", data) #environment/ultrasonic 토픽으로 데이터를 전송
	time.sleep(0.5) #0.5초간 sleep상태에 돌입
	client.loop(0.5)
