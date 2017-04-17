<?php
 

$connect = mysql_connect("localhost", "root", "kutemsys");
mysql_query('SET NAMES utf8');
if (!$connect) {
    echo "Unable to connect to DB: " . mysql_error();
    exit;
}
 
if (!mysql_select_db("iot")) {
    echo "Unable to select mydbname: " . mysql_error();
    exit;
}
 

 
 
?>
