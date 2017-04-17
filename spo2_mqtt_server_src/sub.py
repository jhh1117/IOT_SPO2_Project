import paho.mqtt.client as mqtt
import binascii
import MySQLdb



topic = ""
hu = 0	#temperature variable ini

def on_connect(client, userdata, rc):			#if connect client
	print ("Connected with result coe " + str(rc))	#connect two topic
	client.subscribe("environment/+")	#temperature topic

def on_message(client, userdata, msg):			#if receive message
	# Open database connection
	db = MySQLdb.connect("localhost","root","kutemsys","iot" )

	# prepare a cursor object using cursor() method
	cursor = db.cursor()

	global hu	
	global topic
	if(msg.topic == "environment/user1"):	#if receive from humidity
		print "topic is ", msg.topic
		topic = str(msg.topic)
		hu = int(ord(msg.payload))
		print hu
		sql = """insert into iot_project(topic, spo2) values(%s, %s)"""
		cursor.execute("""insert into iot_project(topic, spo2) values(%s, %s)""",(topic, hu,))
		db.commit()
	if(msg.topic == "environment/user2"):	#if receive from humidity
		print "topic is ", msg.topic
		topic = str(msg.topic)
		hu = int(ord(msg.payload))
		print hu
		sql = """insert into iot_project(topic, spo2) values(%s, %s)"""
		cursor.execute("""insert into iot_project(topic, spo2) values(%s, %s)""",(topic, hu,))
		db.commit()
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect("127.0.0.1", 1883, 60)	#connenct localhost

client.loop_forever()
