<?php

require_once 'db_config.php';
$response = array();

if (isset($_POST['agentId'])) {
	error_log("Saving selected OJT LIST -".$_POST['agentId']);

	$jsonSelectedIds = $_POST['jsonSelectedIds'];

	$jsonObjs = json_decode($jsonSelectedIds);

	
	foreach ($jsonObjs as $key => $value) {
		error_log("Company -->".$key);
		foreach ($value as $value[$key] => $value1) {
			error_log("Student -->".$value1);

			$updateApproveQry = "UPDATE resume_details SET approved = true ,updated_by_teacher_id = ".$_POST['agentId']." WHERE id = ".$value1
						;

			error_log($updateApproveQry);

			$result1=mysqli_query($link, $updateApproveQry);

			error_log("insert selected ojt info ".print_r($result1,true));	


			$existsQry = "SELECT COUNT(*) cnt FROM company_ojt WHERE user_id = ".$value1." AND  company_id = ".$key;

			$result_checker = mysqli_query($link,$existsQry);
    		$checker = (int) mysqli_fetch_assoc($result_checker)["cnt"];

    		if($checker == 0){

				$insertOJTQry = "INSERT INTO company_ojt(user_id,company_id,approved_by_teacher_id)
								SELECT ".$value1.",".$key.",".$_POST['agentId']."
								";
				error_log($insertOJTQry);

				$result2=mysqli_query($link, $insertOJTQry);

				error_log("insert company ojt info ".print_r($result2,true));	
			}



		}

	}

	$response['success'] = 1;



}else{
	$response['success'] = 0;	
}

echo json_encode($response);


?>