<?php
require_once 'db_config.php';
$response = array();

error_log("update company");
if(isset($_POST['newCompanyName']) && isset($_POST['newCompanyType']) && isset($_POST['newAddress']) 
	&& isset($_POST['newPhoneNumber']) && isset($_POST['newDescription']) && isset($_POST['isMoaCertified']) && isset($_POST['agentid'])){
	
	
	error_log("update company - ".$_POST['newCompanyName']." - ".$_POST['newCompanyType']." - ".$_POST['newAddress']
			." - ".$_POST['newPhoneNumber']." - ".$_POST['newDescription']." - ".$_POST['isMoaCertified']." - ".$_POST['agentid']);
	

	$conn = new mysqli($host, $username, $password, $db_name,$port);
		
	$query = "select count(*) checker from user where id = ".$_POST['agentid'];

	$checker= 0;

	error_log($query);

	if ($result = $conn->query($query)) {
		while ($row = $result->fetch_assoc()) {
			$checker = (int)$row['checker'];
		}
		$result->free();
	}

	error_log($checker);
	
	if($checker > 0){
		$sql = "update user set name ='".$_POST['newCompanyName']."',address = '".$_POST['newAddress']."',phonenumber = '".$_POST['newPhoneNumber']."',department = '".$_POST['newCompanyType']."' where id = ".$_POST['agentid'];

		$sql2 = "update company_profile set description = '".$_POST['newDescription']."',moa_certified = ".$_POST['isMoaCertified']." WHERE user_id = ".$_POST['agentid'];


		error_log($sql);
		error_log($sql2);
		$conn->query($sql);
		$conn->query($sql2);

		$response['success'] = 1;
	}else{
		$response['success'] = 0;
	}
	error_log(json_encode($response));
	echo json_encode($response);
	
}else{
	$response['success'] = 0;
	echo json_encode($response);
}

?>