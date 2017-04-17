import paho.mqtt.client as mqtt

mqttc = mqtt.Client("python_pub")
mqttc.connect("127.0.0.1", 1883)
#mqttc.connect("218.150.181.117", 1883)
mqttc.publish("hello/world", "Hello")
mqttc.loop(2)

