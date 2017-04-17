# IoT SpO2 Project
본 프로젝트는 사용자의 산소포화도 값을 실시간으로 측정하고 분석하여 최적의 시간에 학습자에게 휴식을 제안하고  
적정 산소포화도로 돌아오면 학습을 권유하는 학습 페이스메이커 시스템이다. 
  
  
[Demo Video(HIGH QUALITY) 링크](https://www.youtube.com/watch?v=3DdKXlKkUwc&feature=youtu.be)


## spo2_app_src
- 안드로이드 스튜디오로 만든 네이티브 앱이다.
- 서버에 지속적으로 최신 spo2 데이터를 요청한다.
- 서버에게 일, 주, 월 통계 자료 및 집중했던 시간을 요청한다.

## spo2_server_src
- 안드로이드 어플리케이션과 http 통신한다.
- spo2 데이터(realtime, saved)를 전송한다.
- 클라이언트에서 통계 자료 요청 시 서버에서 처리하여 유용한 데이터를 전송한다.

## spo2_sensor_src
- 사용자 산소포화도 측정 노드에 탑재되는 프로그램의 소스코드이다.  
- 사용자의 산소포화도를 센서를 통해 측정하고 MQTT 프로토콜을 이용하여 측정한 데이터를 전송한다.
- eHealth Library 및 MQTT Paho C library가 컴파일시 요구된다.

## spo2_mqtt_server_src
- 노드로 부터 전송되는 spo2 데이터를 수신하여 DB에 저장하는 파이선 기반의 소스코드

##demo_video(low_quality)
- 저화질의 데모 비디오이다.

##IoT_final_report
- 최종 보고서이다.

# iotlearning
Repository for example codes to learn IoT programming based on Raspberry Pi
- Sensors
 - Ultrasonic sensor
 - DHT11 Temperature/Humidity sensor
 - Analog Light/Temperature sensor (MCP3008 ADC over SPI)
- Actuators
 - LED
- MQTT Paho client examples
 - publish client
 - subscribe client

### References
- Python Programming - E-learning (by Prof. Youn-Hee Han): http://link.koreatech.ac.kr/courses2/SPE/syllabus.html
- 점프 투 파이썬: https://wikidocs.net/book/1
- 왕초보를 위한 Python 2.7: https://wikidocs.net/book/2
- raspberry-gpio-python: http://sourceforge.net/p/raspberry-gpio-python/wiki/Examples/
- MQTT mosquitto broker: http://mosquitto.org/download/
- MQTT Paho Python client: 
 - https://pypi.python.org/pypi/paho-mqtt/1.1
 - https://www.eclipse.org/paho/clients/python/

---
Seungwoo Kang, Computer Science and Engineering, KOREATECH


