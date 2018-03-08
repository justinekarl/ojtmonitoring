<?php
require_once 'db_config.php';
$response = array();

error_log("update ojt requirements");

if(isset($_POST['college']) && isset($_POST['willProvideAllowance']) && isset($_POST['allowance']) 
	&& isset($_POST['ojtNumber']) && isset($_POST['agentid'])){
	
	
	error_log("update ojt requirements - ".$_POST['college']." - ".$_POST['willProvideAllowance']." - ".$_POST['allowance']
			." - ".$_POST['ojtNumber']." - ".$_POST['agentid']);
	

	$conn = new mysqli($host, $username, $password, $db_name,$port);


	$query = "select count(*) checker from company_profile where user_id = ".$_POST['agentid'];

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
		$sql = "update company_profile set does_provide_allowance =".$_POST['willProvideAllowance'].",allowance = '".$_POST['allowance']."',ojt_number = '".$_POST['ojtNumber']."',college = '".$_POST['college']."'    where user_id = ".$_POST['agentid'];
 

		error_log($sql);

		$conn->query($sql);

		$response['success'] = 1;
	}else{
		$response['success'] = 0;
	}
	error_log(json_encode($response));
	echo json_encode($response);
}



?>