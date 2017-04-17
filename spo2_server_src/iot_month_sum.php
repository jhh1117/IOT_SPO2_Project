<?php
//달	
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
    $sum_hour = 0;
        $sum_minitue = 0;
        $first_time = 0;

    $today = strtotime("this month midnight");                //주 첫일 00 :00                일 7 개
    $tim = date("Ymd", mktime(0, 0, 0, intval(date('m')), 1, intval(date('Y'))  ));
    $huehue = strtotime($tim);
    $tim = date("Y-m-d",strtotime($tim) + 60*1440);

    $old_time = strtotime("this month midnight");        //이게 signdate임

    if(mysql_num_rows($result)){
        while($row=mysql_fetch_assoc($result)){
            $spo2_arr[] = $row['spo2'];
            $time_arr[] = $row['recent_time'];
        }
    }

    $temp = $time_arr[0];
    $old_time = date( 'Y-m-d H:i:s', strtotime($temp));             //최초 시간 대입
    $first_time = date( 'Y-m-d H:i:s', strtotime($temp));             //최초 시간 대입

    for ($i=0; $i < count($time_arr); $i++) {
        $temp = $time_arr[$i];
        $timeString = date( 'Y-m-d H:i:s', strtotime($temp));           //이게 this 타임임

        $someTime=strtotime($timeString)-strtotime("$old_time GMT");
        $diff_minitue = date('i', $someTime);
        $diff_hour = date('H', $someTime);
        $diff_day = date('d', $someTime);

        if($diff_minitue < 3 and (int)$spo2_arr[$i] > 96 and $diff_hour < 1 and $diff_day < 2) {

        } else{
            $someTime=strtotime($old_time)-strtotime("$first_time GMT");
            $diff_minitue = date('i', $someTime);
            $diff_hour = date('H', $someTime);
            $sum_minitue += $diff_minitue;
            $sum_hour += $diff_hour;
            $first_time = $timeString;
        }
        $old_time = $timeString;
    }
    $json['time'][0]['sum_hour'] = $sum_hour;
        $json['time'][0]['sum_minitue'] = $sum_minitue;

        mysql_close($con);
        echo json_encode($json);
?>

