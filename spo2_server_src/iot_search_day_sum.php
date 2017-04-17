<?php
//일자 니가 집중한 시간 
        $response = array();

        include "connect_db.php";
        date_default_timezone_set("Asia/Seoul");

        mysql_query("set names utf8", $connect);
        $topic = $_POST['topic'];
        $search_day = $_POST['select_day'];
     
        $json = array();
        $time_arr = array();
        $spo2_arr = array();
        $spo2_avg = 0;
        $col_count = 0;
        $arr_count = 0;
        $sum_hour = 0;
        $sum_minitue = 0;
        $first_time = 0;

        $today = strtotime("today midnight");
        $tim = date( 'Y-m-d H:i:s', $today );

        $old_time = strtotime("today midnight");        //이게 signdate임
        $time = date( 'd', $today);

        $time = $time - $search_day;


        $tim = date( 'Y-m-d H:i:s', $today - 86400 * $time);
        $temp_tim = date( 'Y-m-d', $today - 86400 * $time);

        $sql = "select * from iot_project where topic = '$topic' and recent_time like '$temp_tim%' order by recent_time asc;";
        $result = mysql_query($sql);
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

                if($diff_minitue < 3 and (int)$spo2_arr[$i] > 96 and $diff_hour < 1) {

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

