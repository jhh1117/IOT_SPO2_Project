
#include "eHealth.h" //Include eHealth library ���
#include <stdlib.h> 
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include "MQTTClient.h" //MQTT PAHO C Client ���

#define ADDRESS "tcp://218.150.182.58:1883" //MQTT ���� �ּ�
#define CLIENTID "Oximeter1" //MQTT CLNT ID
#define TOPIC "environment/user1" //MQTT USER TOPIC
#define QOS 2 //MQTT QOS
#define TIMEOUT 10000L
#define LED_RED 2 //LED �ɹ�ȣ ��ũ��
#define LED_GREEN 4

int cont = 0;
MQTTClient client; //MQTT Client ����
MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
MQTTClient_message pubmsg = MQTTClient_message_initializer;
MQTTClient_deliveryToken token;
int rc;
int last_token = -1; //���� ������ ��ū�� ���� �����ϴ� ����


void readPulsioximeter();



void setup() {  //�ʱ�ȭ �Լ�
	eHealth.initPulsioximeter(); //pulsioximeter �ʱ�ȭ
	attachInterrupt(6, readPulsioximeter, RISING);  //pulsioximeter rising interrupt ����
    pinMode(LED_RED,OUTPUT); //LED RED, GREEN �ʱ�ȭ �� �������� ����
	pinMode(LED_GREEN,OUTPUT);
	digitalWrite(LED_RED,LOW);
	digitalWrite(LED_GREEN,LOW);
}

void good_bye() {
	//���α׷� ����� ȣ��Ǵ� �Լ�
	digitalWrite(LED_RED,LOW); //LED ���� OFF
	digitalWrite(LED_GREEN,LOW);
	exit(1);
}

bool valid_check(int bpm, char spo2) { //�����ȭ���� �ɹڵ����� ���� ������ �ִ��� �Ǵ��ϴ� �Լ�
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

	MQTTClient_publishMessage(client, TOPIC, &pubmsg, &token); //MQTT�� �����͸� �ƾ ����

	rc = MQTTClient_waitForCompletion(client, token, TIMEOUT);
	
	if(last_token == token) { //���� �ֱٿ� ���� ��ū�� �������� ���� ��ū�� ������ ���(������ ����)
		printf("disconnected to server retry conncetion to server\n");
		digitalWrite(LED_RED,HIGH); //���� LED HIGH ����
		for(int i =0; i < 5; i++) {
			if((rc = MQTTClient_connect(client,&conn_opts)) == MQTTCLIENT_SUCCESS) {  //5ȸ ������ �õ�
				digitalWrite(LED_RED, LOW); //�����ÿ��� ���� LED LOW
				last_token = -1; //last_token -1�� �ʱ�ȭ
				break;
			}
			else {
				printf("retry count %d\n",i+1); //������ �õ� ���нÿ��� ���� ����Ƚ�� ���
				delay(10000); //10�� ���
				if(i == 4) { //���� �� ������ ��� ���α׷� ����
					printf("connect failure check internet connection or server\n");
					good_bye();
				}
			}
		}
	}
	else {	
		printf("Message with delivery token %d dlivered\n", token); //���������� ������ ���۽� ��ū ���
		last_token = token;
	}
}
void loop() {  //loop�Լ�
	int bpm = 0;
	char spo2 = 0;
	bpm = eHealth.getBPM(); //�ɹڵ����� �����ȭ���� �޾ƿ´�.
	spo2 = eHealth.getOxygenSaturation();
	
	if(valid_check(bpm, spo2)) { //���� �������� ���̶�� �ǴܵǸ�
		printf("bpm : %d || spo2 : %d%%\n",bpm,spo2); //ȭ�鿡 ���
		digitalWrite(LED_RED,HIGH); //���� LED�� ����
		delay(100);
		send_mqtt_payload(spo2,TOPIC);
		digitalWrite(LED_RED,LOW);//���� LED�� ���� (�����Ͱ� ���۽ø��� BLINK ȿ��)
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
	setup(); //�ʱ�ȭ �Լ� ȣ��
	
	
	MQTTClient_create(&client,ADDRESS,CLIENTID,MQTTCLIENT_PERSISTENCE_NONE,NULL); //MQTT client����
	conn_opts.keepAliveInterval = 20;
	conn_opts.cleansession = 1;

	//digitalWrite(LED_RED, HIGH);
	digitalWrite(LED_GREEN, HIGH); //��� LED ����
	
	unistd::sleep(5); //5�ʰ� sleep

	while((rc = MQTTClient_connect(client,&conn_opts)) != MQTTCLIENT_SUCCESS) { //MQTT ������ ������ �ɶ����� ���� �õ�
		printf("failed to connect\n");
		digitalWrite(LED_RED,HIGH); //���� ���н� ���� LED ����
		unistd::sleep(5);
	}

	digitalWrite(LED_RED,LOW);

	while(1){
		loop(); //�����Լ� �ݺ� ȣ��
	}

	good_bye();
}
