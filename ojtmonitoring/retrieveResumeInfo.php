<?php

require_once 'db_config.php';

$response = array();


error_log("retrieving resume details");

if (isset($_POST['agentId'])) {

	$agentId = $_POST['agentId'];
	error_log("log in -".$agentId);


	$basicInfoQry = "SELECT name,address,phonenumber,email FROM user where id = ".$agentId;

	error_log($basicInfoQry);

	$conn = new mysqli($host, $username, $password, $db_name,$port);


	$checker= 0;
    if ($result = $conn->query($basicInfoQry)) {
        while ($row = $result->fetch_assoc()) {
            $checker = $checker+1;
            
                foreach($row  as $key => $value){
                    $response[$key] = $value;
                }
         }
        $result->free();
    }



    $resumeDetailsQry = "SELECT id as resume_details_id,accomplishments,interests FROM resume_details WHERE user_id = ".$agentId;


    error_log("Resume Qry ".$resumeDetailsQry);

    $checker1= 0;
    $resumeDetailsId = 0;
     if ($result = $conn->query($resumeDetailsQry)) {
        while ($row = $result->fetch_assoc()) {
            $checker1 = $checker1+1;
            	foreach($row  as $key => $value){
            		error_log($key);
            		if($key == "resume_details_id"){
            			$resumeDetailsId= $value;
            		}
                    $response[$key] = $value;
                }
         }
        $result->free();
    }


	error_log("Resume Id ".$resumeDetailsId);

	$workExperienceQry = " SELECT concat('work_experience_id~',id),concat('company_name~',COALESCE(company_name,'')),CONCAT('address~',COALESCE(address,'')),CONCAT('job_description~',COALESCE(job_description,'')),CONCAT('duties_responsibilities~',COALESCE(duties_responsibilities,'')) FROM work_experience WHERE resume_details_id = ".$resumeDetailsId;

	error_log("Work Experience Query ".$workExperienceQry);



	$workExperienceList = [];

	
	$workExperienceResults = mysqli_fetch_all(mysqli_query($link,$workExperienceQry));

	error_log(sizeof($workExperienceResults));

    if(sizeof($workExperienceResults) > 0){
        for ($ctr = 0; $ctr < sizeof($workExperienceResults); $ctr++){
            array_push($workExperienceList, $workExperienceResults[$ctr]);
        }
    }

    if(sizeof($workExperienceList) > 0){
        $response["work_experience"] = $workExperienceList;
    }


    $educationalBackgroundQry = "SELECT CONCAT('name~',COALESCE(name,'')),CONCAT('address~',address),CONCAT('type~',type) from educational_background where resume_details_id = ".$resumeDetailsId;

    error_log("Work Experience Query ".$educationalBackgroundQry);

    $educationalBackgroundList = [];

    $educationalBackgroundResults = mysqli_fetch_all(mysqli_query($link,$educationalBackgroundQry));

    error_log(sizeof($educationalBackgroundResults));

    if(sizeof($educationalBackgroundResults) > 0){
        for ($ctr = 0; $ctr < sizeof($educationalBackgroundResults); $ctr++){
            array_push($educationalBackgroundList, $educationalBackgroundResults[$ctr]);
        }
    }

     if(sizeof($educationalBackgroundList) > 0){
        $response["educational_background"] = $educationalBackgroundList;
    }


    $referencesQry = "SELECT concat('references_id~',id),concat('name~',coalesce(name,'')),concat('address~',coalesce(address,'')),concat('phone_number~',coalesce(phone_number,'')),CONCAT('occupation~',coalesce(occupation,''))  FROM resume_references WHERE resume_details_id = ".$resumeDetailsId;


	error_log("References Query ".$referencesQry);

	$referencesList = [];

	$referencesResults = mysqli_fetch_all(mysqli_query($link,$referencesQry));

	error_log(sizeof($referencesResults));


	if(sizeof($referencesResults) > 0){
        for ($ctr = 0; $ctr < sizeof($referencesResults); $ctr++){
            array_push($referencesList, $referencesResults[$ctr]);
        }
    }

 	if(sizeof($referencesList) > 0){
        $response["references"] = $referencesList;
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


}



?>
