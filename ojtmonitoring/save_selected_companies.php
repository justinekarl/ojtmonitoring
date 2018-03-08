<?php

require_once 'db_config.php';
$response = array();


if (isset($_POST['agentId'])) {
	error_log("Saving selected companies For  id-".$_POST['agentId']);

$selectCompanyIds = $_POST['selectedCompanyIds'];
$agentId = $_POST['agentId'];

error_log($selectCompanyIds);


$selCompIds = explode(",", $selectCompanyIds);

foreach ($selCompIds as $key => $companyId) {

	$compId = intval($companyId);

	$insertSelectedCompanyQry = "INSERT INTO student_company_selected(user_id,company_id) VALUES ('$agentId','$compId')";

	error_log($insertSelectedCompanyQry);

	$result=mysqli_query($link,
                $insertSelectedCompanyQry);

	error_log("insert selected company info ".print_r($result,true));	




	}


	$response['success'] = 1;


}else{
	$response['success'] = 0;	
}


echo json_encode($response);


?>