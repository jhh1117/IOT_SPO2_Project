<?php

    $response = array();

    include "connect_db.php";
    date_default_timezone_set("Asia/Seoul");

    mysql_query("set names utf8", $connect);
    $topic = $_POST['topic'];
    $sql = "SELECT * FROM iot_project WHERE topic = '$topic' and recent_time BETWEEN subdate(curdate(),date_format(curdate(),'%w')-1) AND subdate(curdate(),date_format(curdate(),'%w')-8) order by recent_time asc;";

    $result = mysql_query($sql);
    $json = array();
    $time_arr = array();
    $spo2_arr = array();
    $spo2_avg = 0;
    $col_count = 0;
    $arr_count = 0;

    $today = strtotime("this week midnight");                //주 첫일 00 :00                일 7 개
    $tim = date( 'Y-m-d', $today );
    $huehue = strtotime($tim);
    $tim = date("Y-m-d",strtotime($tim) + 60*1440);

    if(mysql_num_rows($result)){
        while($row=mysql_fetch_assoc($result)){
            $spo2_arr[] = $row['spo2'];
            $time_arr[] = $row['recent_time'];
        }
    }

    for ($i=0; $i < count($time_arr); $i++) {
        $temp = $time_arr[$i];
        $timeString = date( 'Y-m-d', strtotime($temp));

        if ( $tim > $timeString ) {                                     //tim보다 이전 시간대이면
                $spo2_avg += (int)$spo2_arr[$i];
                $col_count++;
          
        } else {
            $spo2_avg = $spo2_avg / $col_count;
                if($spo2_avg == '')
                        $spo2_avg = 'null';
                $huehue = strtotime($tim);
                $json['data'][$arr_count]['spo2'] = $spo2_avg;
                $json['data'][$arr_count]['day'] = date('d', $huehue);
                $arr_count++;
                $spo2_avg = 0;
                $col_count = 0;

                $tim = date("Y-m-d",strtotime($tim) + 60*1440);             //
        }
    }

    mysql_close($con);
    echo json_encode($json);
?>
