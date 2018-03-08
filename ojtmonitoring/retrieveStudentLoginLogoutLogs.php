<?php

require_once 'db_config.php';
$response = array();

error_log("get student login logout list");
if(isset($_POST['agentId'])){
	$agentId = $_POST['agentId'];



	$mainQry = "SELECT CONCAT('student_name~',COALESCE(b.name,'')) as student_name,CONCAT('company_name~',COALESCE(c.name,'')) as company_name,CONCAT('login_date~',COALESCE(a.login_date,'')),CONCAT('logout_date~',COALESCE(a.logout_date,''))
					FROM student_ojt_attendance_log a
				LEFT JOIN user b ON a.student_id = b.id AND b.accounttype = 1
				LEFT JOIN user c ON c.id = a.company_id AND c.accounttype = 3
					WHERE b.id IN (SELECT user_id FROM resume_details WHERE approved )";

	error_log($mainQry);
	if(isset($_POST['studentName'])){
		$studentName = $_POST['studentName'];
		if(strlen($studentName) > 0){
			$mainQry = $mainQry ." AND b.name like '%".$studentName."%'";
		}

		error_log($mainQry);

	}

	if(isset($_POST['companyName'])){
		$companyName = $_POST['companyName'];
		if(strlen($companyName) > 0){
			$mainQry = $mainQry ." AND c.name like '%".$companyName."%' ";
		}
	}


	if(isset($_POST['from']) && isset($_POST['thru'])){
		$mainQry = $mainQry ." AND cast(login_date as date) >= '".$_POST['from']."' AND cast(logout_date as date) <= '".$_POST['thru']."'  ";	
	}



	if(isset($_POST['isCompany'])){
		$mainQry = $mainQry ." AND c.id = ".$agentId;
	}

	if(isset($_POST['college'])){
		$college = $_POST['college'];
		if(strlen($college) > 0){
			$mainQry = $mainQry ." AND b.college like '".$college."' ";	
		}

		error_log($mainQry);
	}


	$mainQry = $mainQry ." ORDER BY 3 desc ,4 desc ";



	$items = [];
	$itemResults = mysqli_fetch_all(mysqli_query($link,$mainQry));

	if(sizeof($itemResults) > 0){
        for ($ctr = 0; $ctr < sizeof($itemResults); $ctr++){
            array_push($items, $itemResults[$ctr]);
        }
    }

    error_log("6------>".json_encode($items)."<------");
    if(sizeof($items) > 0){
        $response["success"] = 1;
        $response["student_list"] = $items;
        error_log(json_encode($response));
        //echo json_encode($response);
    }else {
        $response["success"] = 0;
        $response["student_list"] = "None";
        //echo json_encode($response);
    }


   

        echo json_encode($response);
}


?>