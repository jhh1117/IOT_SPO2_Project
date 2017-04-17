<?php
		session_start();

		$m_id = $_POST['m_id'];
		$m_pass = $_POST['m_pass'];

		$_SESSION['ses_userid'] = $m_id;
		$_SESSION['ses_pass'] = $m_pass;

		include "connect_db.php";
		$sql = "select *from member where m_id = '$m_id' and m_pass = '$m_pass' ";

		$res = mysql_query($sql, $connect);
		$list = mysql_num_rows($res);

		if($list)
		{
			$row = mysql_fetch_array($res);
			$ses_userid = $row[userid];
			$ses_pass = $row[passwd];
			$haha = $row['topic'];
			echo $haha;
		}
		else{
			echo "fail";
			die;
		}

		mysql_close($connect);
	?>
