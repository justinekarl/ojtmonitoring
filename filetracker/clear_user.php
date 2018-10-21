<?php

require_once 'db_config.php';
$response = array();

error_log("clear user;");
error_log($_POST['agent_ids']);

if (isset($_POST['agent_ids'])) {
	$ids = $_POST['agent_ids'];
	
	$conn = new mysqli($host, $username, $password, $db_name);
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	}
	$sql = "delete from agent where id_agent in (".$ids.")";
	$conn->query($sql);
	$sql = "delete from borrowed where agent_id in (".$ids.")";
	$conn->query($sql);
	$sql = "delete from returned where agent_id in (".$ids.")";
	$conn->query($sql);
	$sql = "delete from transaction_logs where agent_id in (".$ids.")";
	$conn->query($sql);
	
	/* if($conn->query($sql) === TRUE) {
	 $response["success"] = 1;
	 } else {
	 $response["success"] = 0;
	 }
	 $conn->close();
	 error_log(json_encode($response));
	 echo json_encode($response); */
	
	$query =" SELECT * FROM agent order by full_name ASC";
	
	$query =" SELECT agent.*,borrowed.not_clear FROM agent ";
	$query .=" left join (select agent_id,count(borrowed.agent_id) > 0 as not_clear from borrowed group by borrowed.agent_id) as borrowed on agent.id_agent = borrowed.agent_id ";
	$query .=" order by full_name ASC ";
	
	$items = array();
	
	$result = mysqli_query($link,$query);
	
	while($continents =mysqli_fetch_assoc($result)){
		$transactionLogs = array();
		foreach($continents  as $key => $value){
			array_push($transactionLogs, array($key => $value));
		}
		array_push($items, $transactionLogs);
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
	
}

?>
