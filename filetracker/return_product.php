<?php
 
require_once 'db_config.php';
$response = array();
 
// check for required fields
error_log("returned;");
if (isset($_POST['borrower']) && isset($_POST['item']) && isset($_POST['agent_id'])) {

 
    $borrower = $_POST['borrower'];
    $item = $_POST['item'];
	$agent_id = $_POST['agent_id'];

	$query = "select count(*) as checker from borrowed where (item LIKE '%".$item."%') and agent_id = ".$agent_id;
	
	$return_checker= mysqli_query($link,$query);
	$checker = (int) mysqli_fetch_assoc($return_checker)["checker"];

	if($checker > 0){
		$conn = new mysqli($host, $username, $password, $db_name);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		}
		
		$sql = "delete from borrowed where (item LIKE '%".$item."%') and agent_id = ".$agent_id;
		error_log($sql);
		$conn->query($sql);
		
		$sql = "INSERT INTO returned(borrower,item,agent_id) VALUES('$borrower', '$item',$agent_id)";
		error_log($sql);
		if($conn->query($sql) === TRUE) {
			echo "New record created successfully";
		} else {
			echo "Error: " . $sql . "<br>" . $conn->error;
		}
		
		$sql = "INSERT INTO transaction_logs(item,agent_id,borrowed) VALUES('$item','$agent_id',false)";
		error_log($sql);
		if($conn->query($sql) === TRUE) {
			echo "New record transaction_logs created successfully";
		}
		
		$conn->close();
		$response["success"] = 1;
		$response["message"] = "returned successfully.";
		error_log(json_encode($response));
		echo json_encode($response);
	}
 
    
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    error_log(json_encode($response));
    echo json_encode($response);
}
?>