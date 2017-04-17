import paho.mqtt.client as mqtt

def on_connect(client, userdata, rc):
	print("Connected with result code " + str(rc))
	client.subscribe("hello/world")
	
def on_message(client, userdata, msg):
	print("Topic: " + msg.topic + "  Message: " + str(msg.payload))


client = mqtt.Client("pub client")
client.on_connect = on_connect
client.on_message = on_message

client.connect("127.0.0.1", 1883, 60)

#client.loop_start()
client.loop_forever()
	


		

