<?php

require_once 'db_config.php';
$response = array();

error_log("get ojt pending list");
if (isset($_POST['agentid'])) {
    $agent_id = $_POST['agentid'];
    
    $queryOjt = "SELECT CONCAT('company_id~',b.user_id),CONCAT('company_name~',COALESCE(d.name,'')) as company_name,CONCAT('college~',COALESCE(e.college,'')),CONCAT('resume_id~',c.id),CONCAT('student_name~',COALESCE(e.name,'')) as student_name,CONCAT('phone~',COALESCE(e.phonenumber,'')),CONCAT('email~',COALESCE(e.email,'')),CONCAT('approved~',approved), CONCAT('selected_company_id~',coalesce(co.company_id,0))
				FROM student_company_selected a
				LEFT JOIN company_profile b ON a.company_id = b.id
				LEFT JOIN resume_details c ON a.user_id = c.user_id
				LEFT JOIN user d ON d.id = b.user_id
				LEFT JOIN user e ON e.id = c.user_id
				LEFT JOIN company_ojt co ON co.company_id = d.id
				WHERE 1=1
		    ";

    if(isset($_POST['college'])){
    	$college = $_POST['college'];
		$queryOjt = $queryOjt ." AND e.college like '".$college."' ";

    }


    $queryOjt = $queryOjt ." GROUP BY 1,2,3,4,5,6,7,8,9
							 ORDER BY 1,2,3,4,5,6,7,8,9 ";



	 $items = [];
     error_log($queryOjt);

      $queryResults = mysqli_fetch_all(mysqli_query($link,$queryOjt));

       if(sizeof($queryResults) > 0){
	        for ($ctr = 0; $ctr < sizeof($queryResults); $ctr++){
	            array_push($items, $queryResults[$ctr]);
	        }
       }

         error_log("6------>".json_encode($items)."<------");
	    if(sizeof($items) > 0){
	        $response["success"] = 1;
	        $response["ojt_details"] = $items;
	        error_log(json_encode($response));
	        echo json_encode($response);
	    }else {
	        $response["success"] = 0;
	        $response["ojt_details"] = "None";
	        echo json_encode($response);
	    }

}


?>