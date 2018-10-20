<?php
require_once 'db_config.php';
$response = array();

error_log("clear log;");
if (isset($_POST['id'])) {
	$ids = $_POST['id'];
	
	$conn = new mysqli($host, $username, $password, $db_name);
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	}
	$sql = "delete from transaction_logs where id in (".$ids.")";
	error_log($sql);
	$conn->query($sql);
	/* if($conn->query($sql) === TRUE) {
		$response["success"] = 1;
	} else {
		$response["success"] = 0;
	}
	$conn->close();
	error_log(json_encode($response));
	echo json_encode($response); */
	
	$query =" SELECT * FROM transaction_logs LEFT JOIN agent ON agent.id_agent = transaction_logs.agent_id ORDER BY date_created DESC";
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