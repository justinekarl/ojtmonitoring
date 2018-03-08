<?php

require_once 'db_config.php';
$response = array();

if(isset($_POST['agent_id'])){
	error_log("INSERT LOGIN LOGS ..");

	$studentId = $_POST['agent_id'];
	$logInDate = $_POST['login_date'];
	$logOutDate = $_POST['logout_date'];
	$companyId = $_POST['company_id'];

	$checkIfInsertOrUpdate = "SELECT count(*) cnt FROM student_ojt_attendance_log WHERE student_id = {$studentId} AND CAST(scan_date AS date) = current_date ";
	$result_checker = mysqli_query($link,$checkIfInsertOrUpdate);
	$checker = (int) mysqli_fetch_assoc($result_checker)["cnt"];

	error_log("processLoginLogout.php check_result".print_r($checkIfInsertOrUpdate,true));

	if($checker == 0){
		$inserLogin = "INSERT INTO student_ojt_attendance_log(student_id,company_id,login_date,login)
					   VALUES ('$studentId','$companyId','$logInDate',true)
					";

		error_log("insert student_ojt_attendance_log info ".$inserLogin);	
		$result=mysqli_query($link,$inserLogin);
	    error_log("INSERTED RESULT".$result);

	    $response['login_logout_action'] = "Login Successful";
					
	}else{
		$updateLogout = "UPDATE student_ojt_attendance_log SET logout_date = '".$logOutDate."',login = false
						 WHERE student_id = '".$studentId."' AND company_id = '".$companyId."' AND CAST(scan_date as date) = current_date
 						";

		error_log("update student_ojt_attendance_log info ".$updateLogout);	
		$result=mysqli_query($link,$updateLogout);
	    error_log("UPDATE RESULT".$result);

	    $response['login_logout_action'] = "Logout Successful";
	}

echo json_encode($response);
error_log("RESULT".print_r($response, true));

}

?>