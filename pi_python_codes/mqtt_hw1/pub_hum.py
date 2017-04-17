# -*- coding: utf-8 -*-
#위의 코드는 한글주석의 사용이 가능하도록하는 코드임
import paho.mqtt.client as mqtt
import random
import time
client = mqtt.Client("pub client2") #MQTT 클라이언트 객체를 만듬

client.connect("127.0.0.1",1883,60) #로컬호스트 브로커에 접속

#무한반복
while True:
	data = random.randrange(20,95); #20~95사이의 습도데이터를 랜덤으로 생성
	print("Humidity: " + str(data)) #생성된 습도데이터를 화면에 출력
	client.publish("environment/humidity", data) #environment/humidity 토픽으로 데이터를 전송
	time.sleep(2) #2초간 sleep상태 진입
	client.loop(2)
