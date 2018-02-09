<?php
 
/*
 * All database connection variables
 */
 
/* define('DB_USER', "root"); // db user
define('DB_PASSWORD', "1234"); // db password (mention your db password here)
define('DB_DATABASE', "file_tracker"); // database name
define('DB_SERVER', "localhost"); // db server */


$host="localhost";
$username="root";
$password="1234";
$db_name="file_tracker";
$link=mysqli_connect($host, $username, $password, $db_name) or die("unable to connect"); 
?>
