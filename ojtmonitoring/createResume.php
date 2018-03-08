<?php

require_once 'db_config.php';
$response = array();

if (isset($_POST['agent_id'])) {
error_log("Creating Resume Record For  id-".$_POST['agent_id']);
$name = $_POST['name'];
$address = $_POST['address'];
$phone_number = $_POST['phoneNumber'];
$email = $_POST['email'];
$agentId = $_POST['agent_id'];
$resumeeDetailsId= $_POST['resume_details_id'];

//work experience 1
$workPreferenceId1  = $_POST['workExperienceId1'];
$companyName1 = $_POST['companyName1'];
$companyAddress1 = $_POST['companyAddress1'];
$jobDescription1 = $_POST['jobDescription1'];
$dutiesResponsibilities1 = $_POST['dutiesResponsibilities1'];

//work experience 2
$workPreferenceId2  = $_POST['workExperienceId2'];
$companyName2 = $_POST['companyName2'];
$companyAddress2 = $_POST['companyAddress2'];
$jobDescription2 = $_POST['jobDescription2'];
$dutiesResponsibilities2 = $_POST['dutiesResponsibilities2'];

$accomplishments = $_POST['accomplishments'];
$interests = $_POST['interests'];

$elementary =  $_POST['elementary'];
$elementaryAddress =  $_POST['elementaryAddress'];


$highSchool =  $_POST['highSchool'];
$highSchoolAddress =  $_POST['highSchoolAddress'];

$college = $_POST['college'];
$collegeAddress = $_POST['collegeAddress'];


$referenceId1 = $_POST['referenceId1'];
$refName1 = $_POST['refName1'];
$refAddress1 = $_POST['refAddress1'];
$refPhone1 = $_POST['refPhone1'];
$refOccupation1 = $_POST['refOccupation1'];


$referenceId2 = $_POST['referenceId2'];
$refName2 = $_POST['refName2'];
$refAddress2 = $_POST['refAddress2'];
$refPhone2 = $_POST['refPhone2'];
$refOccupation2 = $_POST['refOccupation2'];


$referenceId3 = $_POST['referenceId3'];
$refName3 = $_POST['refName3'];
$refAddress3 = $_POST['refAddress3'];
$refPhone3 = $_POST['refPhone3'];
$refOccupation3 = $_POST['refOccupation3'];





$checkIfInsertOrUpdate = "SELECT count(*) cnt FROM resume_details WHERE user_id = {$agentId}";
$result_checker = mysqli_query($link,$checkIfInsertOrUpdate);
$checker = (int) mysqli_fetch_assoc($result_checker)["cnt"];


error_log("createResume.php check_result".print_r($result_checker,true));


$updateQueryStudentInfoQry = "UPDATE user SET name = '".$name."', address = '".$address."' , phonenumber = '".$phone_number."' ,  email = '".$email."' WHERE id = ".$agentId;

error_log($updateQueryStudentInfoQry);

	 $result=mysqli_query($link,
                $updateQueryStudentInfoQry);

	 error_log("update existing student info ".print_r($result_checker,true));


if($checker === 0){

	$insertResumeQry = "INSERT INTO resume_details(user_id,accomplishments,interests) VALUES ('$agentId','$accomplishments','$interests') ";
	$result=mysqli_query($link,
                $insertResumeQry);

	error_log("insert resume_details info ".print_r($result,true));	

	$insertedId = mysqli_insert_id($link);

	$response['resume_details_id'] = $insertedId;


	//redundant code here to be refactored in the future

	$workExperienceQry = "INSERT INTO work_experience(resume_details_id,company_name,address,job_description,duties_responsibilities) 
						  VALUES ('$insertedId','$companyName1','$companyAddress1','$jobDescription1','$dutiesResponsibilities1');
						 ";

	error_log("insert work_experience info ".$workExperienceQry);	


    $result=mysqli_query($link,$workExperienceQry);
    error_log("INSERTED RESULT".$result);



    $workExperienceQry1 = "INSERT INTO work_experience(resume_details_id,company_name,address,job_description,duties_responsibilities) 
						  VALUES ('$insertedId','$companyName2','$companyAddress2','$jobDescription2','$dutiesResponsibilities2');
						 ";

	error_log("insert work_experience info ".$workExperienceQry1);	
    $result=mysqli_query($link,$workExperienceQry1);
    error_log("INSERTED RESULT".$result);



    $educationalBackgroundElemQry = "INSERT INTO educational_background(resume_details_id,type,address,name)
    					 VALUES ('$insertedId',0,'$elementaryAddress','$elementary')
    					";

    error_log("insert elementary info ".$educationalBackgroundElemQry);	
	$result=mysqli_query($link,$educationalBackgroundElemQry);
    error_log("INSERTED RESULT".$result);


     $educationalBackgroundHSQry = "INSERT INTO educational_background(resume_details_id,type,address,name)
    					 VALUES ('$insertedId',1,'$highSchoolAddress','$highSchool')
    					";

    error_log("insert highSchool info ".$educationalBackgroundHSQry);	
	$result=mysqli_query($link,$educationalBackgroundHSQry);
    error_log("INSERTED RESULT".$result);


    $educationalBackgroundcollegeQry = "INSERT INTO educational_background(resume_details_id,type,address,name)
    					 VALUES ('$insertedId',2,'$collegeAddress','$college')
    					";

    error_log("insert highSchool info ".$educationalBackgroundcollegeQry);	
	$result=mysqli_query($link,$educationalBackgroundcollegeQry);
    error_log("INSERTED RESULT".$result);


    $reference1Qry = "INSERT INTO resume_references(resume_details_id,name,address,phone_number,occupation)
    					VALUES ('$insertedId','$refName1','$refAddress1','$refPhone1','$refOccupation1')
    				 ";

    error_log("insert reference 1 info ".$reference1Qry);

    $result=mysqli_query($link,$reference1Qry);
    error_log("INSERTED RESULT".$result);


    $reference2Qry = "INSERT INTO resume_references(resume_details_id,name,address,phone_number,occupation)
    					VALUES ('$insertedId','$refName2','$refAddress2','$refPhone2','$refOccupation2')
    				 ";

    error_log("insert reference 2 info ".$reference2Qry);

    $result=mysqli_query($link,$reference2Qry);
    error_log("INSERTED RESULT".$result);


    $reference3Qry = "INSERT INTO resume_references(resume_details_id,name,address,phone_number,occupation)
    					VALUES ('$insertedId','$refName3','$refAddress3','$refPhone3','$refOccupation3')
    				 ";

    error_log("insert reference 3 info ".$reference3Qry);

    $result=mysqli_query($link,$reference3Qry);
    error_log("INSERTED RESULT".$result);


	echo json_encode($response);


}else{
	error_log("existing resume_details info ");	

	
	$updateResumeQry = "UPDATE resume_details SET accomplishments = '".$accomplishments."', interests = '".$interests."' WHERE user_id=".$agentId;

	$result=mysqli_query($link,
	                $updateResumeQry);
	error_log($updateResumeQry);
	
	
	error_log($_POST['workExperienceId1']);
	$updateWorkExperience1Qry = "UPDATE work_experience SET company_name = '".$companyName1."', address = '".$companyAddress1."',job_description = '".$jobDescription1."',duties_responsibilities ='".$dutiesResponsibilities1."' WHERE id = ".$_POST['workExperienceId1'];

	 error_log($updateWorkExperience1Qry);
	 $result=mysqli_query($link,
                $updateWorkExperience1Qry);

	 error_log("update work_experience info 1".print_r($result,true));



	 error_log($_POST['workExperienceId2']);
	 $updateWorkExperience2Qry = "UPDATE work_experience SET company_name = '".$companyName2."', address = '".$companyAddress2."',job_description = '".$jobDescription2."',duties_responsibilities ='".$dutiesResponsibilities2."' WHERE id = ".$_POST['workExperienceId2'];

	 error_log($updateWorkExperience2Qry);

	 $result=mysqli_query($link,
                $updateWorkExperience2Qry);

	 error_log("update work_experience info 2".print_r($result,true));

	 $updateEducationalBckgroundElem = "UPDATE educational_background SET name = '".$elementary."',address = '".$elementaryAddress."' WHERE type = 0 AND resume_details_id = ".$_POST['resume_details_id'];

	 error_log($updateEducationalBckgroundElem);
	 $result=mysqli_query($link,
                $updateEducationalBckgroundElem);

	 error_log("update educational_background info elem".print_r($result,true));


	 $updateEducationalBckgroundHS = "UPDATE educational_background SET name = '".$highSchool."',address = '".$highSchoolAddress."' WHERE type = 1 AND resume_details_id = ".$_POST['resume_details_id'];

	 error_log($updateEducationalBckgroundHS);
	 $result=mysqli_query($link,
                $updateEducationalBckgroundHS);

	 error_log("update educational_background info HS".print_r($result,true));

	 $updateEducationalBckgroundCollege = "UPDATE educational_background SET name = '".$college."',address = '".$collegeAddress."' WHERE type = 2 AND resume_details_id = ".$_POST['resume_details_id'];

	 error_log($updateEducationalBckgroundCollege);
	 $result=mysqli_query($link,
                $updateEducationalBckgroundCollege);

	 error_log("update educational_background info College".print_r($result,true));


	 $updateRef1 = "UPDATE resume_references SET name = '".$refName1."', address = '".$refAddress1."',phone_number = '".$refPhone1."',occupation = '".$refOccupation1."' WHERE id = ".$_POST['referenceId1'];

	 error_log($updateRef1);
	 $result=mysqli_query($link,
                $updateRef1);

	 error_log("update resume_references 1 info".print_r($result,true));


	  $updateRef2 = "UPDATE resume_references SET name = '".$refName2."', address = '".$refAddress2."',phone_number = '".$refPhone2."',occupation = '".$refOccupation2."' WHERE id = ".$_POST['referenceId2'];

	 error_log($updateRef2);
	 $result=mysqli_query($link,
                $updateRef2);

	 error_log("update resume_references 2 info".print_r($result,true));


	 $updateRef3 = "UPDATE resume_references SET name = '".$refName3."', address = '".$refAddress3."',phone_number = '".$refPhone3."',occupation = '".$refOccupation3."' WHERE id = ".$_POST['referenceId3'];

	 error_log($updateRef3);
	 $result=mysqli_query($link,
                $updateRef3);

	 error_log("update resume_references 3 info".print_r($result,true));

}





}



?>