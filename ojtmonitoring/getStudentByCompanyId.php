<?php

require_once 'db_config.php';
$response = array();


error_log("get students by company list");
if (isset($_POST['agentid'])) {
    $agent_id = $_POST['agentid'];
    
    $queryOjt = " SELECT CONCAT('student_name~',COALESCE(b.name,'')),CONCAT('college~',COALESCE(b.college,'')),CONCAT('student_id~',b.id),CONCAT('accepted~',accepted)
					FROM company_ojt a
					LEFT JOIN user b ON a.user_id = b.id

					WHERE company_id= ".$agent_id."
					ORDER BY 1,2,3,4
		    ";

	 $students = [];
     error_log($queryOjt);

      $queryResults = mysqli_fetch_all(mysqli_query($link,$queryOjt));

       if(sizeof($queryResults) > 0){
	        for ($ctr = 0; $ctr < sizeof($queryResults); $ctr++){
	            array_push($students, $queryResults[$ctr]);
	        }
       }

         error_log("6------>".json_encode($students)."<------");
	    if(sizeof($students) > 0){
	        $response["success"] = 1;
	        $response["student_lists"] = $students;
	        error_log(json_encode($response));
	        echo json_encode($response);
	    }else {
	        $response["success"] = 0;
	        $response["student_lists"] = "None";
	        echo json_encode($response);
	    }

}

?>