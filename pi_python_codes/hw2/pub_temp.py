# -*- coding: utf-8 -*-
#위의 코드는 한글주석의 사용이 가능하도록 하는 코드임
import RPi.GPIO as gpio
import paho.mqtt.client as mqtt
import time
import dht11
import datetime

gpio.cleanup()
gpio.setwarnings(False)
gpio.setmode(gpio.BCM)

last_temp = 25 #가장 최근의 온도를 저장(초기값은 기준치보다 아랫값을 넣어두어 무조건 True로 가도록)
instance = dht11.DHT11(pin = 5) #5번핀으로 dht11을 활성화
client = mqtt.Client("pub client") #MQTT 클라이언트 객체를 만듬

client.connect("127.0.0.1",1883,60) #로컬호스트 브로커에 접속

#무한 반복
while True:
        result = instance.read() #dht11로 부터 값을 읽어들임
        if result.is_valid(): #유효한 값이면 시간및 온도를 출력하고 최근의 온도값을 갱신
                print("Last valid input: " + str(datetime.datetime.now()))
                print("Temperature: %d C" % result.temperature)
                last_temp = result.temperature
        else: #유효치 않다면 유효하지 않음을 출력하고 가장 최근의 온도값을 출력
                print("Result was not valid last_temperature: " + str(last_temp))

        data = last_temp #data에 최근 온도를 집어넣음
	print("Temperature: " + str(data))  #생성된 온도데이터를 화면에 출력
	client.publish("environment/temperature", data) #environment/temparature 토픽으로 데이터를 전송
	time.sleep(1) #1초간 sleep상태 진입
	client.loop(1)


