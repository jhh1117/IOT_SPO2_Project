# -*- coding: utf-8 -*-
#위의 코드는 한글주석의 사용이 가능하도록 하는 코드임
import paho.mqtt.client as mqtt

#온도와 습도를 저장하는 전역 변수 정의
hum = 0.0
temp = 0.0

#mqtt broker와 연결 성공시 함수 정의
def on_connect(client, userdata, rc): 
	print("Connected with result coe" + str(rc))
	client.subscribe("environment/#");
	#environment로 시작하는 토픽의 모든 메세지들을 구독

#메세지가 브로커로 부터 수신된 경우 처리하는 함수
def on_message(client, userdata, msg):
	#온도와 습도가 전역변수임을 명시하는 정의가 필요.
	global hum
	global temp
	ratio = 0.0
	#토픽이 습도, 온도 각각의 경웨 맞게 변수에 저장
	if msg.topic=="environment/humidity":
		hum = float(msg.payload)
	elif msg.topic=="environment/temperature":
		temp = float(msg.payload)
	
	#두개의 데이타가 모두 들어온경우에만(0이 아닌경우) 온,습도 및 불쾌지수를 출력
	if hum != 0.0 and temp != 0.0:
		print("Temperature: " + str(temp) + " Humidity: " + str(hum))
		ratio = (9.0/5.0)*temp - 0.55*(1-hum/100)*(9.0/5.0*temp-26) + 32
		print("ratio:" + str(ratio));
		#불쾌지수에 정도에 따른 단계 출력
		if ratio < 68:
			print("Discomport Index : LOW")
		elif ratio >= 68 and ratio < 75:
			print("Discomport Index : NORMAL")
		elif ratio >= 75 and ratio < 80:
			print("Discomport Index : HIGH")
		elif ratio >= 80:
			print("Discomport Index : VERY HIGH")
		
		print("")

client = mqtt.Client("sub_client") #클라이언트 생성 
client.on_connect = on_connect #접속시 콜백함수 정의
client.on_message = on_message #메세지 수신시 콜백함수 정의

client.connect("127.0.0.1",1883,60) #로컬호스트의 브로커에 접속

client.loop_forever() #루프를 계속 돌아 지속적으로 데이터를 수신하도록 함
