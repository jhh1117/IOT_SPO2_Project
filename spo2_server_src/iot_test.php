<?php
 
	$response = array();
 
	include "connect_db.php"; 
 
 
	 	mysql_query("set names utf8", $connect);
		$topic = $_POST['topic']; 
		$sql = "SELECT spo2 FROM iot_project WHERE topic = '$topic' and `recent_time` >= DATE_ADD(NOW(), INTERVAL -1 MINUTE) order by recent_time desc limit 1";
 
		$result = mysql_query($sql);
		$json = array();
 
		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$json['data'][]=$row;
			}
		}
		mysql_close($con);
		echo json_encode($json);
?>
