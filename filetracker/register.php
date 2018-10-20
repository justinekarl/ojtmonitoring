<?php

require_once 'db_config.php';
$response = array();
 
if (isset($_POST['user_name']) && isset($_POST['password'])) {
error_log("sign up -".$_POST['user_name']."-".$_POST['password']); 
    $user_name = $_POST['user_name'];
    $password = $_POST['password'];
    $student_number= $_POST['student_number'];
    $full_name= $_POST['full_name'];
	
    $result_checker = mysqli_query($link,"SELECT count(*) as checker FROM agent where user_name='".$user_name."' || student_number =".$student_number);
    
    
    $checker = (int) mysqli_fetch_assoc($result_checker)["checker"];
    
    error_log("register.php checker_restult".print_r($checker,true));
    
    if($checker == 0){
    	$result = mysqli_query($link,"INSERT INTO agent(user_name,password,student_number,full_name) VALUES('$user_name', '$password','$student_number','$full_name')");
    	if ($result) {
    		$response["success"] = 1;
    		$response["message"] = "Registration successful.";
    		echo json_encode($response);
    	} else {
    		$response["success"] = 0;
    		$response["message"] = "Oops! An error occurred.";
    		echo json_encode($response);
    	}
	}else{
		$response["success"] = 0;
		$response["message"] = "Username ".$user_name." or Student No.".$student_number." Aleady Exists";
		echo json_encode($response);
	}
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}
?>