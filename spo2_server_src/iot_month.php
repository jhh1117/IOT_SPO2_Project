<?php

    $response = array();

    include "connect_db.php";
    date_default_timezone_set("Asia/Seoul");

    mysql_query("set names utf8", $connect);
    $topic = $_POST['topic'];
    $sql = "select * from iot_project where topic = '$topic' and DATE_FORMAT(recent_time, '%Y-%m') = date_format(now(), '%Y-%m') order by recent_time asc;";

    $result = mysql_query($sql);
    $json = array();
    $time_arr = array();
    $spo2_arr = array();
    $spo2_avg = 0;
    $col_count = 0;
    $arr_count = 0;

    $today = strtotime("this month midnight");                //주 첫일 00 :00                일 7 개
    $tim = date("Ymd", mktime(0, 0, 0, intval(date('m')), 1, intval(date('Y'))  ));
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
                $i--;

                $tim = date("Y-m-d",strtotime($tim) + 60*1440);             //
        }
    }

    mysql_close($con);
    echo json_encode($json);
?>
