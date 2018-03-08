<?php

require_once 'db_config.php';
$response = array();

error_log("get company names list");
if(isset($_POST['agentId'])){

 	$companyNameQry = " SELECT CONCAT('name~',COALESCE(name,'')) FROM user WHERE accounttype = 3 order by 1 ";
    $companyItems = [];

    error_log($companyNameQry);

    $companyItemResults = mysqli_fetch_all(mysqli_query($link,$companyNameQry));
    if(sizeof($companyItemResults) > 0){
        for ($ctr = 0; $ctr < sizeof($companyItemResults); $ctr++){
            array_push($companyItems, $companyItemResults[$ctr]);
        }
    }

    error_log("7------>".json_encode($companyItems)."<------");
    if(sizeof($companyItems) > 0){
        $response["success"] = 1;
        $response["company_names"] = $companyItems;
        error_log(json_encode($response));
        //echo json_encode($response);
    }else {
        $response["success"] = 0;
        $response["company_names"] = "None";
        //echo json_encode($response);
    }

     echo json_encode($response);
}


?>