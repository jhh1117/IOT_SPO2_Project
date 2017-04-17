
#include "eHealth.h" //Include eHealth library 헤더
#include <stdlib.h> 
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include "MQTTClient.h" //MQTT PAHO C Client 헤더

#define ADDRESS "tcp://218.150.182.58:1883" //MQTT 서버 주소
#define CLIENTID "Oximeter1" //MQTT CLNT ID
#define TOPIC "environment/user1" //MQTT USER TOPIC
#define QOS 2 //MQTT QOS
#define TIMEOUT 10000L
#define LED_RED 2 //LED 핀번호 매크로
#define LED_GREEN 4

int cont = 0;
MQTTClient client; //MQTT Client 세팅
MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
MQTTClient_message pubmsg = MQTTClient_message_initializer;
MQTTClient_deliveryToken token;
int rc;
int last_token = -1; //가장 마지막 토큰의 값을 저장하는 변수


void readPulsioximeter();



void setup() {  //초기화 함수
	eHealth.initPulsioximeter(); //pulsioximeter 초기화
	attachInterrupt(6, readPulsioximeter, RISING);  //pulsioximeter rising interrupt 설정
    pinMode(LED_RED,OUTPUT); //LED RED, GREEN 초기화 및 꺼짐으로 설정
	pinMode(LED_GREEN,OUTPUT);
	digitalWrite(LED_RED,LOW);
	digitalWrite(LED_GREEN,LOW);
}

void good_bye() {
	//프로그램 종료시 호출되는 함수
	digitalWrite(LED_RED,LOW); //LED 전부 OFF
	digitalWrite(LED_GREEN,LOW);
	exit(1);
}

bool valid_check(int bpm, char spo2) { //산소포화도와 심박동수가 정상 범위에 있는지 판단하는 함수
	if(bpm < 60 || bpm >200)
		return false;

	if(spo2 <93 || spo2 >100)
		return false;
	
	return true;
}

void send_mqtt_payload(char data, const char* topic) {
	pubmsg.payload = &data;
	pubmsg.payloadlen = sizeof(char);
	pubmsg.qos = QOS;
	pubmsg.retained = 0;

	MQTTClient_publishMessage(client, TOPIC, &pubmsg, &token); //MQTT에 데이터를 싣어서 보냄

	rc = MQTTClient_waitForCompletion(client, token, TIMEOUT);
	
	if(last_token == token) { //만약 최근에 받은 토큰이 마지막에 받은 토큰과 동일한 경우(서버와 접속)
		printf("disconnected to server retry conncetion to server\n");
		digitalWrite(LED_RED,HIGH); //적색 LED HIGH 점등
		for(int i =0; i < 5; i++) {
			if((rc = MQTTClient_connect(client,&conn_opts)) == MQTTCLIENT_SUCCESS) {  //5회 재접속 시도
				digitalWrite(LED_RED, LOW); //성공시에는 적색 LED LOW
				last_token = -1; //last_token -1로 초기화
				break;
			}
			else {
				printf("retry count %d\n",i+1); //재접속 시도 실패시에는 현재 실패횟수 출력
				delay(10000); //10초 대기
				if(i == 4) { //만약 다 실패한 경우 프로그램 종료
					printf("connect failure check internet connection or server\n");
					good_bye();
				}
			}
		}
	}
	else {	
		printf("Message with delivery token %d dlivered\n", token); //정상적으로 데이터 전송시 토큰 출력
		last_token = token;
	}
}
void loop() {  //loop함수
	int bpm = 0;
	char spo2 = 0;
	bpm = eHealth.getBPM(); //심박동수와 산소포화도를 받아온다.
	spo2 = eHealth.getOxygenSaturation();
	
	if(valid_check(bpm, spo2)) { //만약 정상적인 값이라고 판단되면
		printf("bpm : %d || spo2 : %d%%\n",bpm,spo2); //화면에 출력
		digitalWrite(LED_RED,HIGH); //적색 LED를 점등
		delay(100);
		send_mqtt_payload(spo2,TOPIC);
		digitalWrite(LED_RED,LOW);//적색 LED를 끈다 (데이터가 전송시마다 BLINK 효과)
		delay(100);
	}
	delay(1000);

	
  	/*printf("PRbpm : %d",eHealth.getBPM()); 

	  printf("    %%SPo2 : %d\n", eHealth.getOxygenSaturation());
	
 	 printf("=============================\n");
  
 	 delay(500);*/
  
}


void readPulsioximeter(){  

  cont ++;
  
  if (cont == 50) { //Get only of one 50 measures to reduce the latency
    eHealth.readPulsioximeter();  
    cont = 0;
  }
}



int main (){
	setup(); //초기화 함수 호출
	
	
	MQTTClient_create(&client,ADDRESS,CLIENTID,MQTTCLIENT_PERSISTENCE_NONE,NULL); //MQTT client생성
	conn_opts.keepAliveInterval = 20;
	conn_opts.cleansession = 1;

	//digitalWrite(LED_RED, HIGH);
	digitalWrite(LED_GREEN, HIGH); //녹색 LED 점등
	
	unistd::sleep(5); //5초간 sleep

	while((rc = MQTTClient_connect(client,&conn_opts)) != MQTTCLIENT_SUCCESS) { //MQTT 서버에 접속이 될때까지 접속 시도
		printf("failed to connect\n");
		digitalWrite(LED_RED,HIGH); //접속 실패시 적색 LED 점등
		unistd::sleep(5);
	}

	digitalWrite(LED_RED,LOW);

	while(1){
		loop(); //루프함수 반복 호출
	}

	good_bye();
}
