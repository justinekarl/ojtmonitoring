<?php
 
require_once 'db_config.php';

$response = array();
 
// check for required fields
error_log("login");
if (isset($_POST['user_name']) && isset($_POST['password'])) {
error_log("log in -".$_POST['user_name']."-".$_POST['password']);
 
    $user_name = $_POST['user_name'];
    $password = $_POST['password'];
    
 
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
	
	
    $query = "SELECT * FROM agent where user_name='".$user_name."' and password ='".$password."'";
    error_log($query);
	$result_checker = mysqli_query($link,$query);
	
	$checker  = 0;
	while($continents =mysqli_fetch_assoc($result_checker)){
		$checker  = 1;
		foreach($continents  as $key => $value){
			$response[$key] = $value;
		}
	}
	
	if($checker > 0){
			$response["success"] = 1;
			error_log(json_encode($response));
			echo json_encode($response);
	}else{
		$response["success"] = 0;
		$response["message"] = "User does not exists";
		error_log(json_encode($response));
		echo json_encode($response);
	}
	
    
 
    // check if row inserted or not
    
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    error_log(json_encode($response));
    echo json_encode($response);
}
?>
