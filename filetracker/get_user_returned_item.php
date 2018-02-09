<?php

require_once 'db_config.php';
$response = array();

error_log("get returned list");
if (isset($_POST['agent_id'])) {
	$agent_id = $_POST['agent_id'];
	if($agent_id =='all'){
		$query = "SELECT concat('ITEM: ',returned.item,'\n','Borrower: ',agent.full_name,'\n','Date Borrowed: ',DATE_FORMAT(returned.date_created,'%m-%d-%y %r' )) as item ";
		$query .=" FROM returned ";
		$query .=" left join agent on agent.id_agent = returned.agent_id order by date_created desc";
		
		
	}else{
		$query = "SELECT concat(item,'\n','Date Returned: ', DATE_FORMAT(returned.date_created,'%m-%d-%y %r')) as item FROM returned where agent_id=".$agent_id." order by date_created desc";
	}
	
	
	$items = [];
	error_log($query);
	
	$itemResults = mysqli_fetch_all(mysqli_query($link,$query));
	if(sizeof($itemResults) > 0){
		for ($ctr = 0; $ctr < sizeof($itemResults); $ctr++){
			array_push($items, $itemResults[$ctr][0]);
		}
	}
	
	
	error_log("------>".json_encode($items)."<------");
	if(sizeof($items) > 0){
		$response["success"] = 1;
		$response["data_needed"] = $items;
		error_log(json_encode($response));
		echo json_encode($response);
	}
	
	else {
	$response["success"] = 0;
	$response["data_needed"] = "None";
	echo json_encode($response);
	}
} else {
	// required field is missing
	$response["success"] = 0;
	$response["data_needed"] = "None";
	echo json_encode($response);
}
?>
